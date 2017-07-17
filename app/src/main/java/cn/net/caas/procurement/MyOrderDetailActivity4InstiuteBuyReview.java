package cn.net.caas.procurement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.RequestCode;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.util.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wjj on 2017/1/17.
 */
public class MyOrderDetailActivity4InstiuteBuyReview extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_myorderdetail_instiutebuyreview=null;
    private TextView tv_status_myorderdetail_instiutebuyreview;
    private TextView tv_leaderNick_myorderdetail_instiutebuyreview;
    private LinearLayout layout_goods_myorderdetail_instiutebuyreview;
    private TextView tv_createTime_myorderdetail_instiutebuyreview;
    private TextView tv_caasOrderId_myorderdetail_instiutebuyreview;

    private LinearLayout layout_progress_myorderdetail_instiutebuyreview;
    private LinearLayout scrollview_myorderdetail_instiutebuyreview;

    private Button btn_ok_myorderdetail_instiutebuyreview;
    private Button btn_cancel_myorderdetail_instiutebuyreview;

    private int status;
    private int ID;
    private int mOrderId_detail;
    private int mOrderId2_detail;
    private int teamId_detail;
    private String projFeeFrom_detail;

    private SharedPreferences sp;
    private String access_token;

    private int status_fromorder;
    private long caasOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorderdetail4instiutebuyreview);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ID = bundle.getInt("mainOrderId");
        status_fromorder = bundle.getInt("status");
        Log.i("123","ID："+ID+",status: "+status_fromorder);

        initView();

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        if (status_fromorder==Constants.INSTIUTE_REVIEW){
            showOrderDetail4InstiuteBuyReview(access_token,ID);
        }else{
            showOrderDetail4AllBuyReview(access_token,ID);
        }

    }

    private void initView() {
        //Toolbar
        toolbar_myorderdetail_instiutebuyreview= (Toolbar) findViewById(R.id.toolbar_myorderdetail_instiutebuyreview);
        setSupportActionBar(toolbar_myorderdetail_instiutebuyreview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_status_myorderdetail_instiutebuyreview= (TextView) findViewById(R.id.tv_status_myorderdetail_instiutebuyreview);
        tv_leaderNick_myorderdetail_instiutebuyreview= (TextView) findViewById(R.id.tv_leaderNick_myorderdetail_instiutebuyreview);
        layout_goods_myorderdetail_instiutebuyreview= (LinearLayout) findViewById(R.id.layout_goods_myorderdetail_instiutebuyreview);
        tv_createTime_myorderdetail_instiutebuyreview= (TextView) findViewById(R.id.tv_createTime_myorderdetail_instiutebuyreview);
        tv_caasOrderId_myorderdetail_instiutebuyreview= (TextView) findViewById(R.id.tv_caasOrderId_myorderdetail_instiutebuyreview);

        layout_progress_myorderdetail_instiutebuyreview= (LinearLayout) findViewById(R.id.layout_progress_myorderdetail_instiutebuyreview);
        scrollview_myorderdetail_instiutebuyreview= (LinearLayout) findViewById(R.id.scrollview_myorderdetail_instiutebuyreview);

        btn_ok_myorderdetail_instiutebuyreview= (Button) findViewById(R.id.btn_ok_myorderdetail_instiutebuyreview);
        btn_cancel_myorderdetail_instiutebuyreview= (Button) findViewById(R.id.btn_cancel_myorderdetail_instiutebuyreview);
        btn_ok_myorderdetail_instiutebuyreview.setOnClickListener(this);
        btn_cancel_myorderdetail_instiutebuyreview.setOnClickListener(this);
        btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
        btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
    }

    private void initData(int status) {
        switch (status){
            case Constants.LEADER_REVIEW:
                tv_status_myorderdetail_instiutebuyreview.setText("课题组长审核中");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.INSTIUTE_REVIEW:
                tv_status_myorderdetail_instiutebuyreview.setText("研究所审核中");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.VISIBLE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.VISIBLE);
                break;
            case Constants.REVIEW_PASS:
                tv_status_myorderdetail_instiutebuyreview.setText("审核通过");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.CONFIRM_ORDER:
                tv_status_myorderdetail_instiutebuyreview.setText("订单确认");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.DELIVERED:
                tv_status_myorderdetail_instiutebuyreview.setText("已发货");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.CHECKING:
                tv_status_myorderdetail_instiutebuyreview.setText("验货中");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.ORDER_DONE:
                tv_status_myorderdetail_instiutebuyreview.setText("订单完成");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.LEADER_REVIEW_EXPENSES_CLAIMS:
                tv_status_myorderdetail_instiutebuyreview.setText("课题组长报销审核中");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.INSTIUTE_REVIEW_EXPENSES_CLAIMS:
                tv_status_myorderdetail_instiutebuyreview.setText("研究所报销审核中");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.CLAIMING:
                tv_status_myorderdetail_instiutebuyreview.setText("报销中");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.DONE_EXPENSES_CLAIMS:
                tv_status_myorderdetail_instiutebuyreview.setText("完成报销");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_NOT_PASS:
                tv_status_myorderdetail_instiutebuyreview.setText("审核未通过");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.CANCEL_ORDER:
                tv_status_myorderdetail_instiutebuyreview.setText("取消订单");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.REFUNDED_PRODUCTS:
                tv_status_myorderdetail_instiutebuyreview.setText("已退货");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_EXPENSES_CLAIMS_NOT_PASS:
                tv_status_myorderdetail_instiutebuyreview.setText("报销未通过");
                btn_ok_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                btn_cancel_myorderdetail_instiutebuyreview.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok_myorderdetail_instiutebuyreview:
                Intent intent=new Intent(this, InstiuteBuyReviewPassActivity.class);
                Bundle bundle=new Bundle();
                bundle.putLong("caasOrderId",caasOrderId);
                bundle.putInt("status",status_fromorder);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCode.INSTIUTE_BUY_PASS);
                break;
            case R.id.btn_cancel_myorderdetail_instiutebuyreview:
                Intent intent2=new Intent(this, InstiuteBuyReviewRefuseActivity.class);
                Bundle bundle2=new Bundle();
                bundle2.putLong("caasOrderId",caasOrderId);
                bundle2.putInt("status",status_fromorder);
                intent2.putExtras(bundle2);
                startActivityForResult(intent2,RequestCode.INSTIUTE_BUY_REFUSE);
                break;
            default:
                break;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //组长/首席采购审批-待审批订单详情
    private void showOrderDetail4InstiuteBuyReview(String token, final int mainOrderId) {
        layout_progress_myorderdetail_instiutebuyreview.setVisibility(View.VISIBLE);
        scrollview_myorderdetail_instiutebuyreview.setVisibility(View.INVISIBLE);
        OkhttpUtil.get(Constants.ORDER_BOSS_REVIEW + "access_token=" + token, new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Message msg = Message.obtain();
                    Bundle bundle=new Bundle();
                    bundle.putString("json",json);
                    bundle.putInt("mainOrderId",mainOrderId);
                    msg.setData(bundle);
                    msg.what=0;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //组长/首席采购审批-全部订单详情
    private void showOrderDetail4AllBuyReview(String token, final int mainOrderId) {
        layout_progress_myorderdetail_instiutebuyreview.setVisibility(View.VISIBLE);
        scrollview_myorderdetail_instiutebuyreview.setVisibility(View.INVISIBLE);
        OkhttpUtil.get(Constants.ORDER_MAIN_DEPT + "access_token=" + token, new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Message msg = Message.obtain();
                    Bundle bundle=new Bundle();
                    bundle.putString("json",json);
                    bundle.putInt("mainOrderId",mainOrderId);
                    msg.setData(bundle);
                    msg.what=1;
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
            if (msg.what==0){
                Bundle bundle = msg.getData();
                String json = bundle.getString("json");
                int mainOrderId = bundle.getInt("mainOrderId");
                parseJson(json,mainOrderId);
            }
            if (msg.what==1){
                Bundle bundle = msg.getData();
                String json = bundle.getString("json");
                int mainOrderId = bundle.getInt("mainOrderId");
                parseJson(json,mainOrderId);
            }
        }
    };

    private void parseJson(String json,int mainOrderId){
        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            if (jsonArray==null){
                Log.i("123","jsonArray为空");
            }else{
                Log.i("123","jsonArray不为空");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.optJSONObject(i);
                    JSONObject orderCaas = data.optJSONObject("orderCaas");
                    int ID = orderCaas.optInt("id");
                    String buyerNick = orderCaas.optString("buyerNick");//采购人
                    String teamName = orderCaas.optString("teamName");//课题组
                    String leaderNick = orderCaas.optString("leaderNick");//审批人
                    String createTime = orderCaas.optString("createTime");//订单创建时间
                    String projFeeFrom = orderCaas.optString("projFeeFrom");//经费来源
                    int status = orderCaas.optInt("status");//状态

                    if (mainOrderId == ID){
                        caasOrderId = orderCaas.optLong("caasOrderId");

                        initData(status_fromorder);
                        tv_leaderNick_myorderdetail_instiutebuyreview.setText(leaderNick);
                        tv_createTime_myorderdetail_instiutebuyreview.setText(createTime);
                        tv_caasOrderId_myorderdetail_instiutebuyreview.setText(String.valueOf(caasOrderId));

                        JSONArray supplierOrders = data.optJSONArray("supplierOrders");
                        layout_goods_myorderdetail_instiutebuyreview.removeAllViews();
                        for (int j = 0; j < supplierOrders.length(); j++) {
                            JSONObject supplierOrder = supplierOrders.optJSONObject(j);
                            int ID2 = supplierOrder.optInt("id");
                            String sellerNick = supplierOrder.optString("sellerNick");
                            String amountStr = supplierOrder.optString("amountStr");
                            JSONArray orderItemList = supplierOrder.optJSONArray("orderItemList");
                            int allQuantity=0;

                            LinearLayout layout_row= (LinearLayout) LayoutInflater.from(MyOrderDetailActivity4InstiuteBuyReview.this).inflate(R.layout.layout_buyreview,null);
                            TextView tv_sellerNick_buyreview = (TextView) layout_row.findViewById(R.id.tv_sellerNick_buyreview);
                            LinearLayout layout_goods_buyreview = (LinearLayout) layout_row.findViewById(R.id.layout_goods_buyreview);
                            TextView tv_AllQuantity_buyreview = (TextView) layout_row.findViewById(R.id.tv_AllQuantity_buyreview);
                            TextView tv_amountStr_buyreview = (TextView) layout_row.findViewById(R.id.tv_amountStr_buyreview);
                            layout_goods_buyreview.removeAllViews();
                            for (int k = 0; k < orderItemList.length(); k++) {
                                JSONObject orderItem = orderItemList.optJSONObject(k);
                                String itemName = orderItem.optString("itemName");
                                int quantity = orderItem.optInt("quantity");
                                String unitPriceStr = orderItem.optString("unitPriceStr");
                                allQuantity=allQuantity+quantity;
                                String supplierPartId = orderItem.optString("supplierPartId");//货号
                                String brandName = orderItem.optString("brandName");//品牌
                                String sku = orderItem.optString("sku");//单位
                                String specifications = orderItem.optString("specifications");//规格
                                String cycle = orderItem.optString("cycle");//货期
                                String unitPiceStr = orderItem.optString("unitPriceStr");//单价

                                LinearLayout layout_row2= (LinearLayout) LayoutInflater.from(MyOrderDetailActivity4InstiuteBuyReview.this).inflate(R.layout.layout_goods_orderdetail,null);
                                ImageView image_goods = (ImageView) layout_row2.findViewById(R.id.image_goods);
                                TextView tv_goodsName = (TextView) layout_row2.findViewById(R.id.tv_goodsName);
                                TextView tv_supplierPartId = (TextView) layout_row2.findViewById(R.id.tv_supplierPartId);
                                TextView tv_brandName = (TextView) layout_row2.findViewById(R.id.tv_brandName);
                                TextView tv_sku = (TextView) layout_row2.findViewById(R.id.tv_sku);
                                TextView tv_specifications = (TextView) layout_row2.findViewById(R.id.tv_specifications);
                                TextView tv_cycle = (TextView) layout_row2.findViewById(R.id.tv_cycle);
                                TextView tv_unitPriceStr = (TextView) layout_row2.findViewById(R.id.tv_unitPriceStr);
                                TextView tv_quantity = (TextView) layout_row2.findViewById(R.id.tv_quantity);
                                image_goods.setImageResource(R.mipmap.image_goods);
                                tv_goodsName.setText(itemName);
                                tv_supplierPartId.setText(supplierPartId);
                                tv_brandName.setText(brandName);
                                tv_sku.setText(sku);
                                tv_specifications.setText(specifications);
                                tv_cycle.setText(cycle);
                                tv_unitPriceStr.setText("¥ "+unitPiceStr);
                                tv_quantity.setText("x"+String.valueOf(quantity));

                                layout_goods_buyreview.addView(layout_row2);
                            }

                            tv_sellerNick_buyreview.setText(sellerNick);
                            tv_AllQuantity_buyreview.setText("共"+String.valueOf(allQuantity)+"件商品");
                            tv_amountStr_buyreview.setText("合计:¥"+amountStr);

                            layout_goods_myorderdetail_instiutebuyreview.addView(layout_row);
                        }
                    }
                }
                layout_progress_myorderdetail_instiutebuyreview.setVisibility(View.INVISIBLE);
                scrollview_myorderdetail_instiutebuyreview.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RequestCode.INSTIUTE_BUY_PASS && resultCode== ResultCode.INSTIUTE_BUY_PASS){
//            if (status_fromorder==Constants.INSTIUTE_REVIEW){
//                showOrderDetail4InstiuteBuyReview(access_token,ID);
//            }else{
//                showOrderDetail4AllBuyReview(access_token,ID);
//            }
            showOrderDetail4AllBuyReview(access_token,ID);
        }
        if (requestCode==RequestCode.INSTIUTE_BUY_REFUSE && resultCode== ResultCode.INSTIUTE_BUY_REFUSE){
//            if (status_fromorder==Constants.INSTIUTE_REVIEW){
//                showOrderDetail4InstiuteBuyReview(access_token,ID);
//            }else{
//                showOrderDetail4AllBuyReview(access_token,ID);
//            }
            showOrderDetail4AllBuyReview(access_token,ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.order_account:
                Intent intent=new Intent(this,MyPersonalDataActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
