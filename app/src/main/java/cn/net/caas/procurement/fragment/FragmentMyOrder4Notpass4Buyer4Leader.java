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

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.MyOrderActivity4Buyer4Leader;
import cn.net.caas.procurement.MyOrderDetailActivity4Buyer4Leader;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.adapter.OrderAdapter;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.MyAction;
import cn.net.caas.procurement.entity.MyAllOrder;
import cn.net.caas.procurement.entity.MyGoods;
import cn.net.caas.procurement.util.VolleyUtil;

/**
 * 我的订单（失败订单）
 * Created by wjj on 2017/4/9.
 */
public class FragmentMyOrder4Notpass4Buyer4Leader extends Fragment {
    private View view;

    private MyOrderActivity4Buyer4Leader myOrderActivity4Leader;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView lv_myorder4notpass;
    private LinearLayout layout_progress_myorder4notpass;
    private TextView tv_NoOrder_myorder4notpass;

    private List<MyAllOrder> list_myorder4notpass = null;
    private OrderAdapter adapter = null;

    private boolean isVisible;//是否可见
    private boolean isPrepared;//标志位，View是否已经初始化完成
    public boolean isFirstLoad=true;//是否是第一次加载

    private SharedPreferences sp;
    private String access_token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view=inflater.inflate(R.layout.fragment_myorder4notpass,container,false);
        }

        isPrepared=true;//view初始化完成

        myOrderActivity4Leader= (MyOrderActivity4Buyer4Leader) getActivity();

        initView();

        //从本地获取access_token
        sp=myOrderActivity4Leader.getSharedPreferences(Constants.LOGIN_INFO,myOrderActivity4Leader.MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        lazyLoad();

        //点击进入订单详情
        lv_myorder4notpass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("123", "供应商订单状态：" + list_myorder4notpass.get(i).getStatus());
                Intent intent = new Intent(myOrderActivity4Leader, MyOrderDetailActivity4Buyer4Leader.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID",list_myorder4notpass.get(i).getID());
                bundle.putInt("ID2",list_myorder4notpass.get(i).getID2());
                bundle.putInt("status",list_myorder4notpass.get(i).getStatus());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    public static FragmentMyOrder4Notpass4Buyer4Leader newInstance(){
        FragmentMyOrder4Notpass4Buyer4Leader fragmentMyOrder4Notpass=new FragmentMyOrder4Notpass4Buyer4Leader();
        return fragmentMyOrder4Notpass;
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout_myorder4notpass);
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
        lv_myorder4notpass= (ListView) view.findViewById(R.id.lv_myorder4notpass);
        layout_progress_myorder4notpass= (LinearLayout) view.findViewById(R.id.layout_progress_myorder4notpass);
        tv_NoOrder_myorder4notpass= (TextView) view.findViewById(R.id.tv_NoOrder_myorder4notpass);
    }

    private void delay(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        //注册申请报销的广播接收器
        IntentFilter intentFilter=new IntentFilter(MyAction.APPLYCLAIMS);
        myOrderActivity4Leader.registerReceiver(myReceiver,intentFilter);

        //注册取消订单的广播接收器
        IntentFilter intentFilter2=new IntentFilter(MyAction.CANCEL_ORDER);
        myOrderActivity4Leader.registerReceiver(myReceiver,intentFilter2);
    }

    //广播接收器
    private BroadcastReceiver myReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("123","---------------onReceive---------------");
            String action = intent.getAction();
            if (action.equals(MyAction.APPLYCLAIMS)){
                Log.i("123","接收到申请报销后的广播");
                Log.i("123","action: "+action);
                showMyOrder4Notpass(access_token);
            }
            if (action.equals(MyAction.CANCEL_ORDER)){
                Log.i("123","接收到取消订单后的广播");
                Log.i("123","action: "+action);
                showMyOrder4Notpass(access_token);
            }
        }
    };

    //懒加载
    public void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad){
            lv_myorder4notpass.setVisibility(View.INVISIBLE);
            layout_progress_myorder4notpass.setVisibility(View.VISIBLE);
            tv_NoOrder_myorder4notpass.setVisibility(View.INVISIBLE);
            showMyOrder4Notpass(access_token);
        }else if(isVisible() && !isFirstLoad){
            layout_progress_myorder4notpass.setVisibility(View.INVISIBLE);
            lv_myorder4notpass.setVisibility(View.VISIBLE);
        }
    }

    private void showMyOrder4Notpass(String token){
        tv_NoOrder_myorder4notpass.setVisibility(View.INVISIBLE);
        Log.i("123",Constants.ORDER_BUYER_NOTPASS + "access_token=" + token);
        VolleyUtil.get(Constants.ORDER_BUYER_NOTPASS + "access_token=" + token, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int leadertoclaimerevieallNum=0;
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray==null){
                    Log.i("123","jsonArray为空");
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(3);
                }else{
                    Log.i("123","jsonArray不为空");
                    list_myorder4notpass = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        JSONObject orderCaas = data.optJSONObject("orderCaas");
                        int ID = orderCaas.optInt("id");
                        long caasOrderId = orderCaas.optLong("caasOrderId");
                        String buyerNick = orderCaas.optString("buyerNick");//采购人
                        String teamName = orderCaas.optString("teamName");//课题组
                        String leaderNick = orderCaas.optString("leaderNick");//审批人
                        String projFeeFrom = orderCaas.optString("projFeeFrom");//经费来源

                        JSONArray supplierOrders = data.optJSONArray("supplierOrders");
                        for (int j = 0; j < supplierOrders.length(); j++) {
                            JSONObject supplierOrder = supplierOrders.optJSONObject(j);
                            int ID2 = supplierOrder.optInt("id");
                            long supplierOrderId = supplierOrder.optLong("supplierOrderId");
                            int status4supplierOrder = supplierOrder.optInt("status");//供应商里的status
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
                            leadertoclaimerevieallNum++;
                            list_myorder4notpass.add(new MyAllOrder(ID,caasOrderId,ID2,supplierOrderId,sellerNick,status4supplierOrder,list_goods,allQuantity,amountStr));
                        }
                    }
                    adapter = new OrderAdapter(myOrderActivity4Leader, list_myorder4notpass);
                    handler.sendEmptyMessage(1);
                    if (leadertoclaimerevieallNum==0){
                        handler.sendEmptyMessage(3);
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    lv_myorder4notpass.setAdapter(adapter);
                    layout_progress_myorder4notpass.setVisibility(View.INVISIBLE);
                    lv_myorder4notpass.setVisibility(View.VISIBLE);
                    tv_NoOrder_myorder4notpass.setVisibility(View.INVISIBLE);
                    isFirstLoad=false;
                    break;
                case 2:
                    swipeRefreshLayout.setRefreshing(false);
                    showMyOrder4Notpass(access_token);
                    break;
                case 3:
                    tv_NoOrder_myorder4notpass.setVisibility(View.VISIBLE);
                    layout_progress_myorder4notpass.setVisibility(View.INVISIBLE);
                    lv_myorder4notpass.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    isFirstLoad=false;
                    break;
            }
        }
    };
}
