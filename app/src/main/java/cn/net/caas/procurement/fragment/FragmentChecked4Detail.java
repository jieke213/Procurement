package cn.net.caas.procurement.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.net.caas.procurement.CheckPassActivity;
import cn.net.caas.procurement.CheckedActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.scan.MipcaActivityCapture;
import cn.net.caas.procurement.util.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 验货员订单（扫码查询订单）
 * Created by wjj on 2017/4/10.
 */
public class FragmentChecked4Detail extends Fragment implements View.OnClickListener{
    private View view;

    private CheckedActivity checkedActivity;

    private LinearLayout layout_scan_checked4detail;
    private Button btn_pass_checked4detail;
    private LinearLayout layout_checked4detail;
    private LinearLayout layout_progress_checked4detail;
    private TextView tv_status_checked4detail;
    private TextView tv_sellerNick_checked4detail;
    private LinearLayout layout_goods_checked4detail;
    private TextView tv_supplierOrderId_checked4detail;
    private TextView tv_createTime_checked4detail;
    private TextView tv_amountStr_checked4detail;
    private LinearLayout layout_checked4detail_orderlog;

    private SharedPreferences sp;
    private String access_token;

    private String json;

    private final static int SCANNIN_GREQUEST_CODE = 1;

    private int status;
    private long caasOrderId;
    private long supplierOrderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view=inflater.inflate(R.layout.fragment_checked4detail,container,false);
        }

        checkedActivity= (CheckedActivity) getActivity();

        initView();

        //从本地获取access_token
        sp=checkedActivity.getSharedPreferences(Constants.LOGIN_INFO,checkedActivity.MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        return view;
    }

    private void initView() {
        layout_scan_checked4detail= (LinearLayout) view.findViewById(R.id.layout_scan_checked4detail);
        layout_scan_checked4detail.setOnClickListener(this);
        layout_scan_checked4detail.getBackground().setAlpha(30);
        btn_pass_checked4detail= (Button) view.findViewById(R.id.btn_pass_checked4detail);
        btn_pass_checked4detail.setOnClickListener(this);
        btn_pass_checked4detail.getBackground().setAlpha(30);
        btn_pass_checked4detail.setVisibility(View.GONE);
        layout_checked4detail= (LinearLayout) view.findViewById(R.id.layout_checked4detail);
        layout_progress_checked4detail= (LinearLayout) view.findViewById(R.id.layout_progress_checked4detail);
        tv_status_checked4detail= (TextView) view.findViewById(R.id.tv_status_checked4detail);
        tv_sellerNick_checked4detail= (TextView) view.findViewById(R.id.tv_sellerNick_checked4detail);
        layout_goods_checked4detail= (LinearLayout) view.findViewById(R.id.layout_goods_checked4detail);
        tv_supplierOrderId_checked4detail= (TextView) view.findViewById(R.id.tv_supplierOrderId_checked4detail);
        tv_createTime_checked4detail= (TextView) view.findViewById(R.id.tv_createTime_checked4detail);
        tv_amountStr_checked4detail= (TextView) view.findViewById(R.id.tv_amountStr_checked4detail);

        layout_checked4detail_orderlog= (LinearLayout) view.findViewById(R.id.layout_checked4detail_orderlog);

        layout_progress_checked4detail.setVisibility(View.INVISIBLE);
        layout_checked4detail.setVisibility(View.INVISIBLE);
    }

    public static FragmentChecked4Detail newInstance(){
        FragmentChecked4Detail fragmentChecked4Detail=new FragmentChecked4Detail();
        return fragmentChecked4Detail;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_scan_checked4detail:
                Intent intent = new Intent();
                intent.setClass(checkedActivity, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.btn_pass_checked4detail:
                toast("验货通过");
                Intent intent2=new Intent(checkedActivity, CheckPassActivity.class);
                Bundle bundle=new Bundle();
                bundle.putLong("caasOrderId",caasOrderId);
                bundle.putLong("supplierOrderId",supplierOrderId);
                bundle.putInt("status",status);
                intent2.putExtras(bundle);
                startActivityForResult(intent2,0011);
                break;
            default:
                break;
        }
    }

    //扫描二维码之后，返回扫描结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == checkedActivity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    String result = bundle.getString("result");

                    showChecked4Detail(access_token,result);
                }
                break;
        }
    }

    //获取扫描二维码后的数据
    private void showChecked4Detail(String token,String subOrderId){
        layout_progress_checked4detail.setVisibility(View.VISIBLE);
        layout_checked4detail.setVisibility(View.INVISIBLE);
        Map<String,String> map=new HashMap<>();
        map.put("access_token",token);
        map.put("subOrderId",subOrderId);
        OkhttpUtil.post(Constants.ORDER_DETAIL, map,new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    json = response.body().string();
                    handler.sendEmptyMessage(0);
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
                    parseJson();
                    break;
            }
        }
    };

    private void parseJson(){
        try {
            Log.i("123","json: "+json);
            JSONObject jsonObject=new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            final JSONObject orderCaas = data.optJSONObject("orderCaas");
            int ID = orderCaas.optInt("id");//ID
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
                status = supplierOrder.optInt("status");
                supplierOrderId = supplierOrder.optLong("supplierOrderId");//单号
                final String sellerNick = supplierOrder.optString("sellerNick");//经销商
                final String amountStr = supplierOrder.optString("amountStr");//总价

                initData(status);//刷新数据

                //商品信息
                layout_goods_checked4detail.removeAllViews();
                JSONArray orderItemList = supplierOrder.optJSONArray("orderItemList");
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

                    LinearLayout layout_row= (LinearLayout) LayoutInflater.from(checkedActivity).inflate(R.layout.layout_goods_orderdetail,null);
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

                    layout_goods_checked4detail.addView(layout_row);
                }
                tv_sellerNick_checked4detail.setText(sellerNick);
                tv_supplierOrderId_checked4detail.setText(String.valueOf(supplierOrderId));
                tv_createTime_checked4detail.setText(createTime);
                tv_amountStr_checked4detail.setText("¥ "+amountStr);

                layout_progress_checked4detail.setVisibility(View.INVISIBLE);
                layout_checked4detail.setVisibility(View.VISIBLE);

                //订单操作日志
                JSONObject orderLogs = data.optJSONObject("orderLogs");
                JSONArray jsonArray4supplier = orderLogs.optJSONArray(String.valueOf(supplierOrderId));
                JSONArray jsonArray4caas = orderLogs.optJSONArray(String.valueOf(caasOrderId));
                layout_checked4detail_orderlog.removeAllViews();
                if (jsonArray4supplier != null){
                    for (int i = 0; i < jsonArray4supplier.length(); i++) {
                        JSONObject orderLog = jsonArray4supplier.optJSONObject(i);
                        String operator = orderLog.optString("operator");
                        String log = orderLog.optString("log");
                        String createTime4log = orderLog.optString("createTime");
                        LinearLayout layout_orderLog = (LinearLayout) LayoutInflater.from(checkedActivity).inflate(R.layout.layout_orderlog, null);
                        TextView tv_orderlog_operator = (TextView) layout_orderLog.findViewById(R.id.tv_orderlog_operator);
                        TextView tv_orderlog_log = (TextView) layout_orderLog.findViewById(R.id.tv_orderlog_log);
                        TextView tv_orderlog_createTime = (TextView) layout_orderLog.findViewById(R.id.tv_orderlog_createTime);
                        tv_orderlog_operator.setText(operator);
                        tv_orderlog_log.setText(log);
                        tv_orderlog_createTime.setText(createTime4log);
                        layout_checked4detail_orderlog.addView(layout_orderLog);
                    }
                }
                if (jsonArray4caas != null){
                    for (int i = 0; i < jsonArray4caas.length(); i++) {
                        JSONObject orderLog = jsonArray4caas.optJSONObject(i);
                        String operator = orderLog.optString("operator");
                        String log = orderLog.optString("log");
                        String createTime4log = orderLog.optString("createTime");
                        LinearLayout layout_orderLog = (LinearLayout) LayoutInflater.from(checkedActivity).inflate(R.layout.layout_orderlog, null);
                        TextView tv_orderlog_operator = (TextView) layout_orderLog.findViewById(R.id.tv_orderlog_operator);
                        TextView tv_orderlog_log = (TextView) layout_orderLog.findViewById(R.id.tv_orderlog_log);
                        TextView tv_orderlog_createTime = (TextView) layout_orderLog.findViewById(R.id.tv_orderlog_createTime);
                        tv_orderlog_operator.setText(operator);
                        tv_orderlog_log.setText(log);
                        tv_orderlog_createTime.setText(createTime4log);
                        layout_checked4detail_orderlog.addView(layout_orderLog);
                    }
                }
            }
        }  catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData(int status) {
        switch (status){
            case Constants.LEADER_REVIEW:
                tv_status_checked4detail.setText("课题组长审核中");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.INSTIUTE_REVIEW:
                tv_status_checked4detail.setText("研究所审核中");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_PASS:
                tv_status_checked4detail.setText("审核通过");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.CONFIRM_ORDER:
                tv_status_checked4detail.setText("订单确认");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.DELIVERED:
                tv_status_checked4detail.setText("已发货");
                btn_pass_checked4detail.setVisibility(View.VISIBLE);
                break;
            case Constants.CHECKING:
                tv_status_checked4detail.setText("验货中");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.ORDER_DONE:
                tv_status_checked4detail.setText("订单完成");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.LEADER_REVIEW_EXPENSES_CLAIMS:
                tv_status_checked4detail.setText("课题组长报销审核中");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.INSTIUTE_REVIEW_EXPENSES_CLAIMS:
                tv_status_checked4detail.setText("研究所报销审核中");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.CLAIMING:
                tv_status_checked4detail.setText("报销中");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.DONE_EXPENSES_CLAIMS:
                tv_status_checked4detail.setText("完成报销");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_NOT_PASS:
                tv_status_checked4detail.setText("审核未通过");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.CANCEL_ORDER:
                tv_status_checked4detail.setText("取消订单");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.REFUNDED_PRODUCTS:
                tv_status_checked4detail.setText("已退货");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            case Constants.REVIEW_EXPENSES_CLAIMS_NOT_PASS:
                tv_status_checked4detail.setText("报销未通过");
                btn_pass_checked4detail.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void toast(String str){
        Toast.makeText(checkedActivity, str, Toast.LENGTH_SHORT).show();
    }
}
