package cn.net.caas.procurement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.adapter.MineAdapter;
import cn.net.caas.procurement.adapter.OrderAdapter;
import cn.net.caas.procurement.adapter.OrderAdapter4BuyReview;
import cn.net.caas.procurement.adapter.ReviewAdapter;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.entity.Mine;
import cn.net.caas.procurement.entity.MyAllOrder;
import cn.net.caas.procurement.entity.MyGoods;
import cn.net.caas.procurement.entity.MyOrder4BuyReview;
import cn.net.caas.procurement.entity.MyReview;
import cn.net.caas.procurement.util.VolleyUtil;

/**
 * Created by wjj on 2017/4/6.
 */
public class OrderManagerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Toolbar toolbar_ordermanager;
    private GridView gv_ordermanager;

    private ArrayList<Integer> list_childId;
    private ArrayList<String> list_childName;

    private List<Mine> list;
    private MineAdapter adapter;


    private List<MyReview> list_myreview;
    private ReviewAdapter adapter_review;

    private SharedPreferences sp;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordermanager);

        initView();

        Intent intent = getIntent();
        list_childId = intent.getIntegerArrayListExtra("list_childId4OrderManager");
        list_childName = intent.getStringArrayListExtra("list_childName4OrderManager");
        list_myreview = new ArrayList<>(list_childId.size());

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

//        showMenu();
        showMenu2();
    }

    //显示菜单（第一种方法：不显示消息数）
    private void showMenu(){
        list =new ArrayList<>();
        for (int i = 0; i < list_childName.size(); i++) {
            list.add(new Mine(list_childName.get(i),R.drawable.pic_order_up));
        }
        adapter=new MineAdapter(OrderManagerActivity.this,list);
        gv_ordermanager.setAdapter(adapter);
    }

    //显示菜单（第二种方式：显示消息数）
    private void showMenu2(){
        //菜单项中的第一项（课题组长账号）
        showMyOder();

        //菜单中的第一项（所领导账号）
        showInstiuteBuyReview(access_token);

    }

    //我的订单
    private void showMyOder(){
        for (int i = 0; i < list_childName.size(); i++) {
            if (list_childId.get(i) == Constants.ORDER_MANAGER_WDDD){
                list_myreview.add(0,new MyReview(list_childName.get(i),R.drawable.pic_order_up,0));
                showLeaderBuyReviewNum(access_token);
            }
        }
    }

    //组长/首席采购审核
    private void showLeaderBuyReviewNum(String token){
        VolleyUtil.get(Constants.ORDER_LEADER_REVIEW + "access_token=" + token, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int leaderBuyReview_num = 0;
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray==null){

                }else{
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        JSONObject orderCaas = data.optJSONObject("orderCaas");
                        int status = orderCaas.optInt("status");//状态
                        if (status == Constants.LEADER_REVIEW){
                            leaderBuyReview_num++;
                        }
                    }
                }
                for (int i = 0; i < list_childName.size(); i++) {
                    if (list_childId.get(i) == Constants.ORDER_MANAGER_ZZCGSH){
                        Log.i("123","leaderBuyReview_num: "+leaderBuyReview_num);
                        list_myreview.add(1,new MyReview(list_childName.get(i),R.drawable.pic_order_up,leaderBuyReview_num));
                        showLeadertoClaimeReviewNum(access_token);
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
    }

    //组长/首席报销审核
    private void showLeadertoClaimeReviewNum(String token){
        VolleyUtil.get(Constants.ORDER_LEADER_TOCLAIME + "access_token=" + token, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int leadertoClaimeReviewNum = 0;
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray==null){

                }else{
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        JSONObject orderCaas = data.optJSONObject("orderCaas");
                        int status = orderCaas.optInt("status");//状态

                        if (status == Constants.LEADER_REVIEW){
                            leadertoClaimeReviewNum++;
                        }
                    }
                }
                for (int i = 0; i < list_childName.size(); i++) {
                    if (list_childId.get(i) == Constants.ORDER_MANAGER_ZZBXSH){
                        list_myreview.add(2,new MyReview(list_childName.get(i),R.drawable.pic_order_up,leadertoClaimeReviewNum));
                    }
                }
                adapter_review=new ReviewAdapter(OrderManagerActivity.this,list_myreview);
                gv_ordermanager.setAdapter(adapter_review);
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
    }

    //所领导采购审核
    private void showInstiuteBuyReview(String token){
        VolleyUtil.get(Constants.ORDER_BOSS_REVIEW + "access_token=" + token, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int instiutebuyreviewNum=0;
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray==null){

                }else{
                    list=new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        JSONArray supplierOrders = data.optJSONArray("supplierOrders");
                        for (int j = 0; j < supplierOrders.length(); j++) {
                            instiutebuyreviewNum++;
                        }
                    }
                }
                for (int i = 0; i < list_childName.size(); i++) {
                    if (list_childId.get(i) == Constants.ORDER_MANAGER_SLDCGSH){
                        Log.i("123","instiutebuyreviewNum: "+instiutebuyreviewNum);
                        list_myreview.add(0,new MyReview(list_childName.get(i),R.drawable.pic_order_up,instiutebuyreviewNum));
                        showInstiuteToclaimeReview(access_token);
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
    }

    //所领导报销审核
    private void showInstiuteToclaimeReview(String token){
        VolleyUtil.get(Constants.ORDER_BOSS_TOCLAIME + "access_token=" + token, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int instiutetoclaimereviewNum=0;
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray==null){

                }else{
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        JSONArray supplierOrders = data.optJSONArray("supplierOrders");
                        for (int j = 0; j < supplierOrders.length(); j++) {
                            JSONObject supplierOrder = supplierOrders.optJSONObject(j);
                            int status4supplierOrder = supplierOrder.optInt("status");
                            //供应商里的status
                            if (status4supplierOrder == Constants.INSTIUTE_REVIEW_EXPENSES_CLAIMS) {
                                instiutetoclaimereviewNum++;
                            }
                        }
                    }
                }
                for (int i = 0; i < list_childName.size(); i++) {
                    if (list_childId.get(i) == Constants.ORDER_MANAGER_SLDBXSH){
                        list_myreview.add(1,new MyReview(list_childName.get(i),R.drawable.pic_order_up,instiutetoclaimereviewNum));
                    }
                }
                adapter_review=new ReviewAdapter(OrderManagerActivity.this,list_myreview);
                gv_ordermanager.setAdapter(adapter_review);
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
    }

    private void initView() {
        toolbar_ordermanager= (Toolbar) findViewById(R.id.toolbar_ordermanager);
        setSupportActionBar(toolbar_ordermanager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_ordermanager.setNavigationIcon(R.drawable.toolbar_back2);

        gv_ordermanager= (GridView) findViewById(R.id.gv_ordermanager);
        gv_ordermanager.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (list_childId.get(i)){
            case Constants.ORDER_MANAGER_WDDD:
                Intent intent0=new Intent(OrderManagerActivity.this,MyOrderActivity4Buyer4Leader.class);
                startActivity(intent0);
                break;
            case Constants.ORDER_MANAGER_ZZCGSH:
                Intent intent1=new Intent(OrderManagerActivity.this,LeaderBuyReviewActivity.class);
                startActivity(intent1);
                break;
            case Constants.ORDER_MANAGER_ZZBXSH:
                Intent intent2=new Intent(OrderManagerActivity.this,LeaderToclaimeReviewActivity.class);
                startActivity(intent2);
                break;
            case Constants.ORDER_MANAGER_SLDCGSH:
                Intent intent3=new Intent(OrderManagerActivity.this,InstiuteBuyReviewActivity.class);
                startActivity(intent3);
                break;
            case Constants.ORDER_MANAGER_SLDBXSH:
                Intent intent4=new Intent(OrderManagerActivity.this,InstiuteToclaimeReviewActivity.class);
                startActivity(intent4);
                break;
            case Constants.ORDER_MANAGER_YHRDD:
                Intent intent5=new Intent(OrderManagerActivity.this,CheckedActivity.class);
                startActivity(intent5);
                break;
            case Constants.ORDER_MANAGER_CWBX:
                Intent intent6=new Intent(OrderManagerActivity.this,CaiwuActivity.class);
                startActivity(intent6);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String str){
        Toast.makeText(OrderManagerActivity.this, str, Toast.LENGTH_SHORT).show();
    }

}
