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

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.RequestCode;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.util.VolleyUtil;

/**
 * Created by wjj on 2017/1/17.
 */
public class MyOrderDetailActivity4Caiwu extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar=null;
    private TextView tv_status_detail_caiwu;
    private TextView tv_leaderNick_detail_caiwu;
    private TextView tv_sellerNick_detail_caiwu;
    private LinearLayout layout_goods_detail_caiwu;
    private TextView tv_supplierOrderId_detail_caiwu;
    private TextView tv_createTime_detail_caiwu;
    private TextView tv_amountStr_detail_caiwu;

    private LinearLayout layout_progress_detail_caiwu;
    private LinearLayout scrollview_detail_caiwu;

    private Button btn_pass_caiwu;
    private Button btn_nopass_caiwu;

    private int status;
    private int ID;
    private int ID2;
    private long supplierOrderId;
    private long caasOrderId;

    private SharedPreferences sp;
    private String access_token;

    private int status_fromorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorderdetail4caiwu);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ID = bundle.getInt("mainOrderId");
        ID2 = bundle.getInt("ID2");
        status_fromorder = bundle.getInt("status");
        Log.i("123","ID："+ID+"，ID2: "+ID2+",status_fromorder: "+status_fromorder);

        initView();

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        showOrderDetail4All(access_token,ID,ID2);
    }


    private void showOrderDetail4All(String access_token, final int mainOrderId, final int childOrderId) {
        layout_progress_detail_caiwu.setVisibility(View.VISIBLE);
        scrollview_detail_caiwu.setVisibility(View.INVISIBLE);

        Log.i("123",Constants.ORDER_SUB_DEPT + "access_token=" + access_token);
        VolleyUtil.get(Constants.ORDER_SUB_DEPT + "access_token=" + access_token, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        final JSONObject orderCaas = data.optJSONObject("orderCaas");
                        int ID = orderCaas.optInt("id");//ID
                        if (ID == mainOrderId){
                            caasOrderId = orderCaas.optLong("caasOrderId");//单号
                            final String buyerNick = orderCaas.optString("buyerNick");//采购人
                            final String teamName = orderCaas.optString("teamName");//课题组
                            final int teamId = orderCaas.optInt("teamId");//课题组id
                            final String projFeeFrom = orderCaas.optString("projFeeFrom");//经费来源
                            final String createTime = orderCaas.optString("createTime");//采购时间
                            final String leaderNick = orderCaas.optString("leaderNick");//审批人

                            //经销商信息
                            JSONArray supplierOrders = data.optJSONArray("supplierOrders");
                            for (int j = 0; j < supplierOrders.length(); j++) {
                                JSONObject supplierOrder = supplierOrders.optJSONObject(j);
                                int ID2 = supplierOrder.optInt("id");

                                if (ID2 == childOrderId){
                                    status = supplierOrder.optInt("status");
                                    supplierOrderId = supplierOrder.optLong("supplierOrderId");//单号
                                    final String sellerNick = supplierOrder.optString("sellerNick");//经销商
                                    final String amountStr = supplierOrder.optString("amountStr");//总价

                                    initData(status);//刷新数据

                                    //商品信息
                                    JSONArray orderItemList = supplierOrder.optJSONArray("orderItemList");
                                    layout_goods_detail_caiwu.removeAllViews();
                                    for (int k = 0; k < orderItemList.length(); k++) {
                                        JSONObject orderItem = orderItemList.optJSONObject(k);
                                        final int id = orderItem.optInt("id");//id
                                        final String supplierPartId = orderItem.optString("supplierPartId");//货号
                                        final String itemName = orderItem.optString("itemName");//商品名称
                                        final String brandName = orderItem.optString("brandName");//品牌
                                        final String sku = orderItem.optString("sku");//单位
                                        final String specifications = orderItem.optString("specifications");//规格
                                        final String cycle = orderItem.optString("cycle");//货期
                                        final String unitPiceStr = orderItem.optString("unitPriceStr");//单价
                                        final int quantity = orderItem.optInt("quantity");//数量
                                        final String relAmountStr = orderItem.optString("relAmountStr");//合计
                                        final String featureMap = orderItem.optString("featureMap");//备注

                                        LinearLayout layout_row= (LinearLayout) LayoutInflater.from(MyOrderDetailActivity4Caiwu.this).inflate(R.layout.layout_goods_orderdetail,null);
                                        ImageView image_goods = (ImageView) layout_row.findViewById(R.id.image_goods);
                                        TextView tv_goodsName = (TextView) layout_row.findViewById(R.id.tv_goodsName);
                                        TextView tv_supplierPartId = (TextView) layout_row.findViewById(R.id.tv_supplierPartId);
                                        TextView tv_brandName = (TextView) layout_row.findViewById(R.id.tv_brandName);
                                        TextView tv_sku = (TextView) layout_row.findViewById(R.id.tv_sku);
                                        TextView tv_specifications = (TextView) layout_row.findViewById(R.id.tv_specifications);
                                        TextView tv_cycle = (TextView) layout_row.findViewById(R.id.tv_cycle);
                                        TextView tv_unitPriceStr = (TextView) layout_row.findViewById(R.id.tv_unitPriceStr);
                                        TextView tv_quantity = (TextView) layout_row.findViewById(R.id.tv_quantity);
                                        image_goods.setImageResource(R.mipmap.image_goods);
                                        tv_goodsName.setText(itemName);
                                        tv_supplierPartId.setText(supplierPartId);
                                        tv_brandName.setText(brandName);
                                        tv_sku.setText(sku);
                                        tv_specifications.setText(specifications);
                                        tv_cycle.setText(cycle);
                                        tv_unitPriceStr.setText("¥ "+unitPiceStr);
                                        tv_quantity.setText("x"+String.valueOf(quantity));

                                        layout_goods_detail_caiwu.addView(layout_row);
                                    }
                                    tv_leaderNick_detail_caiwu.setText(leaderNick);
                                    tv_sellerNick_detail_caiwu.setText(sellerNick);
                                    tv_supplierOrderId_detail_caiwu.setText(String.valueOf(supplierOrderId));
                                    tv_createTime_detail_caiwu.setText(createTime);
                                    tv_amountStr_detail_caiwu.setText("¥ "+amountStr);

                                    layout_progress_detail_caiwu.setVisibility(View.INVISIBLE);
                                    scrollview_detail_caiwu.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
    }


    private void initView() {
        //Toolbar
        toolbar= (Toolbar) findViewById(R.id.toolbar_myorderdetail_caiwu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_status_detail_caiwu= (TextView) findViewById(R.id.tv_status_detail_caiwu);
        tv_leaderNick_detail_caiwu= (TextView) findViewById(R.id.tv_leaderNick_detail_caiwu);
        tv_sellerNick_detail_caiwu= (TextView) findViewById(R.id.tv_sellerNick_detail_caiwu);
        layout_goods_detail_caiwu= (LinearLayout) findViewById(R.id.layout_goods_detail_caiwu);
        tv_supplierOrderId_detail_caiwu= (TextView) findViewById(R.id.tv_supplierOrderId_detail_caiwu);
        tv_createTime_detail_caiwu= (TextView) findViewById(R.id.tv_createTime_detail_caiwu);
        tv_amountStr_detail_caiwu= (TextView) findViewById(R.id.tv_amountStr_detail_caiwu);

        layout_progress_detail_caiwu= (LinearLayout) findViewById(R.id.layout_progress_detail_caiwu);
        scrollview_detail_caiwu= (LinearLayout) findViewById(R.id.scrollview_detail_caiwu);

        btn_pass_caiwu= (Button) findViewById(R.id.btn_pass_caiwu);
        btn_nopass_caiwu= (Button) findViewById(R.id.btn_nopass_caiwu);
        btn_pass_caiwu.setOnClickListener(this);
        btn_nopass_caiwu.setOnClickListener(this);
        btn_pass_caiwu.setVisibility(View.GONE);
        btn_nopass_caiwu.setVisibility(View.GONE);
    }

    private void initData(int status) {
        switch (status){
            case Constants.LEADER_REVIEW:
                tv_status_detail_caiwu.setText("课题组长审核中");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.INSTIUTE_REVIEW:
                tv_status_detail_caiwu.setText("研究所审核中");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_PASS:
                tv_status_detail_caiwu.setText("审核通过");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.CONFIRM_ORDER:
                tv_status_detail_caiwu.setText("订单确认");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.DELIVERED:
                tv_status_detail_caiwu.setText("已发货");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.CHECKING:
                tv_status_detail_caiwu.setText("验货中");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.ORDER_DONE:
                tv_status_detail_caiwu.setText("订单完成");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.LEADER_REVIEW_EXPENSES_CLAIMS:
                tv_status_detail_caiwu.setText("课题组长报销审核中");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.INSTIUTE_REVIEW_EXPENSES_CLAIMS:
                tv_status_detail_caiwu.setText("研究所报销审核中");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.CLAIMING:
                tv_status_detail_caiwu.setText("报销中");
                btn_pass_caiwu.setVisibility(View.VISIBLE);
                btn_nopass_caiwu.setVisibility(View.VISIBLE);
                break;
            case Constants.DONE_EXPENSES_CLAIMS:
                tv_status_detail_caiwu.setText("完成报销");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_NOT_PASS:
                tv_status_detail_caiwu.setText("审核未通过");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.CANCEL_ORDER:
                tv_status_detail_caiwu.setText("取消订单");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.REFUNDED_PRODUCTS:
                tv_status_detail_caiwu.setText("已退货");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_EXPENSES_CLAIMS_NOT_PASS:
                tv_status_detail_caiwu.setText("报销未通过");
                btn_pass_caiwu.setVisibility(View.GONE);
                btn_nopass_caiwu.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pass_caiwu:
                Intent intent=new Intent(this, CaiwuToclimsPassActivity.class);
                Bundle bundle=new Bundle();
                bundle.putLong("caasOrderId",caasOrderId);
                bundle.putLong("supplierOrderId",supplierOrderId);
                bundle.putInt("status",status_fromorder);
                intent.putExtras(bundle);
                startActivityForResult(intent,RequestCode.CAIWU_TOCLAIME_PASS);
                break;
            case R.id.btn_nopass_caiwu:
                Intent intent3=new Intent(this, CaiwuToclimsNotPassActivity.class);
                Bundle bundle3=new Bundle();
                bundle3.putLong("caasOrderId",caasOrderId);
                bundle3.putLong("supplierOrderId",supplierOrderId);
                bundle3.putInt("status",status_fromorder);
                intent3.putExtras(bundle3);
                startActivityForResult(intent3,RequestCode.CAIWU_TOCLAIME_REFUSE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RequestCode.CAIWU_TOCLAIME_PASS && resultCode== ResultCode.CAIWU_TOCLAIME_PASS){
            showOrderDetail4All(access_token,ID,ID2);
        }
        if (requestCode== RequestCode.CAIWU_TOCLAIME_REFUSE && resultCode== ResultCode.CAIWU_TOCLAIME_REFUSE){
            showOrderDetail4All(access_token,ID,ID2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
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
