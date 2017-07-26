package cn.net.caas.procurement.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.LeaderBuyReviewActivity;
import cn.net.caas.procurement.MyOrderDetailActivity4LeaderBuyReview;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.adapter.OrderAdapter4BuyReview;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.MyAction;
import cn.net.caas.procurement.entity.MyAllOrder;
import cn.net.caas.procurement.entity.MyGoods;
import cn.net.caas.procurement.entity.MyOrder4BuyReview;
import cn.net.caas.procurement.util.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 课题组长采购审核（待审核订单）
 * Created by wjj on 2017/4/7.
 */
public class FragmentLeaderBuyReview extends Fragment {
    private View view;

    private LeaderBuyReviewActivity leaderBuyReviewActivity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView lv_leaderbuyreview;
    private LinearLayout layout_progress_leadebuyreview;
    private TextView tv_NoOrder_leaderbuyreview;

    private List<MyOrder4BuyReview> list;
    private List<MyAllOrder> list_leaderbuyreview = null;
    private OrderAdapter4BuyReview adapter = null;

    private boolean isVisible;//是否可见
    private boolean isPrepared;//标志位，View是否已经初始化完成
    public boolean isFirstLoad=true;//是否是第一次加载

    private SharedPreferences sp;
    private String access_token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view=inflater.inflate(R.layout.fragment_leaderbuyreview,container,false);
        }

        isPrepared=true;//view初始化完成

        leaderBuyReviewActivity= (LeaderBuyReviewActivity) getActivity();

        initView();

        //从本地获取access_token
        sp=leaderBuyReviewActivity.getSharedPreferences(Constants.LOGIN_INFO,leaderBuyReviewActivity.MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        lazyLoad();

        //点击进入订单详情
        lv_leaderbuyreview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("123", "主订单状态：" + list.get(i).getStatus());
                Intent intent = new Intent(leaderBuyReviewActivity, MyOrderDetailActivity4LeaderBuyReview.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mainOrderId", list.get(i).getID());
                bundle.putInt("status",list.get(i).getStatus());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout_leaderbuyreview);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
        //刷新课题组长审核订单
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        delay(1000);
                        handler.sendEmptyMessage(2);
                    }
                }.start();
            }
        });
        lv_leaderbuyreview= (ListView) view.findViewById(R.id.lv_leaderbuyreview);
        layout_progress_leadebuyreview= (LinearLayout) view.findViewById(R.id.layout_progress_leadebuyreview);
        tv_NoOrder_leaderbuyreview= (TextView) view.findViewById(R.id.tv_NoOrder_leaderbuyreview);
    }

    private void delay(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static FragmentLeaderBuyReview newInstance(){
        FragmentLeaderBuyReview fragmentLeaderBuyReview=new FragmentLeaderBuyReview();
        return fragmentLeaderBuyReview;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible=true;
            lazyLoad();
        }else{
            isVisible=false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //注册课题组长采购审核通过的广播接收器
        IntentFilter intentFilter=new IntentFilter(MyAction.LEADER_BUY_PASS);
        leaderBuyReviewActivity.registerReceiver(myReceiver,intentFilter);

        //注册课题组长采购审核拒绝的广播接收器
        IntentFilter intentFilter2=new IntentFilter(MyAction.LEADER_BUY_REFUSE);
        leaderBuyReviewActivity.registerReceiver(myReceiver,intentFilter2);
    }

    //广播接收器
    private BroadcastReceiver myReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("123","---------------onReceive---------------");
            String action = intent.getAction();
            if (action.equals(MyAction.LEADER_BUY_PASS)){
                Log.i("123","接收到课题组长采购审核通过后的广播");
                Log.i("123","action: "+action);
                showLeaderBuyReview(access_token);
            }
            if (action.equals(MyAction.LEADER_BUY_REFUSE)){
                Log.i("123","接收到课题组长采购审核拒绝后的广播");
                Log.i("123","action: "+action);
                showLeaderBuyReview(access_token);
            }
        }
    };

    //懒加载
    public void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad){
            lv_leaderbuyreview.setVisibility(View.INVISIBLE);
            layout_progress_leadebuyreview.setVisibility(View.VISIBLE);
            tv_NoOrder_leaderbuyreview.setVisibility(View.INVISIBLE);
            showLeaderBuyReview(access_token);
        }else if(isVisible() && !isFirstLoad){
            layout_progress_leadebuyreview.setVisibility(View.INVISIBLE);
            lv_leaderbuyreview.setVisibility(View.VISIBLE);
        }
    }

    private void showLeaderBuyReview(String token){
        tv_NoOrder_leaderbuyreview.setVisibility(View.INVISIBLE);
        Log.i("123",Constants.ORDER_LEADER_REVIEW + "access_token=" + token);
        OkhttpUtil.get(Constants.ORDER_LEADER_REVIEW + "access_token=" + token, new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Message msg = Message.obtain();
                    msg.obj=json;
                    msg.what=0;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String json= (String) msg.obj;
                    parseJson(json);
                    break;
                case 2:
                    swipeRefreshLayout.setRefreshing(false);
                    showLeaderBuyReview(access_token);
                    break;
            }
        }
    };

    private void parseJson(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            int leaderbuyreviewNum=0;
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            if (jsonArray==null){
                Log.i("123","jsonArray为空");
                layout_progress_leadebuyreview.setVisibility(View.INVISIBLE);
                tv_NoOrder_leaderbuyreview.setVisibility(View.VISIBLE);
                lv_leaderbuyreview.setVisibility(View.INVISIBLE);
                isFirstLoad=false;
            }else{
                Log.i("123","jsonArray不为空");
                list=new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.optJSONObject(i);
                    JSONObject orderCaas = data.optJSONObject("orderCaas");
                    int ID = orderCaas.optInt("id");
                    long caasOrderId = orderCaas.optLong("caasOrderId");
                    String buyerNick = orderCaas.optString("buyerNick");//采购人
                    String teamName = orderCaas.optString("teamName");//课题组
                    String leaderNick = orderCaas.optString("leaderNick");//审批人
                    String projFeeFrom = orderCaas.optString("projFeeFrom");//经费来源
                    int status = orderCaas.optInt("status");//状态

                    JSONArray supplierOrders = data.optJSONArray("supplierOrders");
                    list_leaderbuyreview = new ArrayList<>();
                    for (int j = 0; j < supplierOrders.length(); j++) {
                        JSONObject supplierOrder = supplierOrders.optJSONObject(j);
                        int ID2 = supplierOrder.optInt("id");
                        long supplierOrderId = supplierOrder.optLong("supplierOrderId");
                        String sellerNick = supplierOrder.optString("sellerNick");
                        String amountStr = supplierOrder.optString("amountStr");
                        JSONArray orderItemList = supplierOrder.optJSONArray("orderItemList");
                        List<MyGoods> list_goods = new ArrayList<>();
                        int allQuantity=0;
                        for (int k = 0; k < orderItemList.length(); k++) {
                            JSONObject orderItem = orderItemList.optJSONObject(k);
                            String itemName = orderItem.optString("itemName");
                            int quantity = orderItem.optInt("quantity");
                            String unitPriceStr = orderItem.optString("unitPriceStr");
                            allQuantity=allQuantity+quantity;
                            Log.i("123","itemName:"+itemName+",quantity:"+quantity+",unitPriceStr:"+unitPriceStr+",allQuantity:"+allQuantity);
                            list_goods.add(new MyGoods(buyerNick, teamName, leaderNick, projFeeFrom, itemName,unitPriceStr,quantity));
                        }
                        leaderbuyreviewNum++;
                        list_leaderbuyreview.add(new MyAllOrder(ID,caasOrderId,ID2,supplierOrderId,sellerNick,status,list_goods,allQuantity,amountStr));
                    }
                    list.add(new MyOrder4BuyReview(list_leaderbuyreview,status,ID,caasOrderId));
                }
                adapter = new OrderAdapter4BuyReview(leaderBuyReviewActivity,list);
                adapter.isShowLeaderButton=true;
                lv_leaderbuyreview.setAdapter(adapter);
                layout_progress_leadebuyreview.setVisibility(View.INVISIBLE);
                lv_leaderbuyreview.setVisibility(View.VISIBLE);
                tv_NoOrder_leaderbuyreview.setVisibility(View.INVISIBLE);
                isFirstLoad=false;

                if (leaderbuyreviewNum==0){
                    layout_progress_leadebuyreview.setVisibility(View.INVISIBLE);
                    lv_leaderbuyreview.setVisibility(View.INVISIBLE);
                    tv_NoOrder_leaderbuyreview.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("123","onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("123","onDestroy");
        leaderBuyReviewActivity.unregisterReceiver(myReceiver);
    }
}
