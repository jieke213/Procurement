package cn.net.caas.procurement;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.net.caas.procurement.adapter.OrderAdapter4PartArrival;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.MyAction;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.entity.ArrivalItem;
import cn.net.caas.procurement.entity.MyPartArrivalGoods;
import cn.net.caas.procurement.entity.ViewHolder;
import cn.net.caas.procurement.util.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wjj on 2017/4/13.
 */
public class PartArrivalActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_partarrival;
    private Button btn_partarrival;
    private TextView tv_sellerNick_partarrival;

    private List<ArrivalItem> list_arrivalItem;

    private OrderAdapter4PartArrival adapter;
    private ListView lv_goods_partarrival;
    private List<MyPartArrivalGoods> list_goods;

    private HashMap<Integer,ViewHolder> hashMap=new HashMap<>();

    private SharedPreferences sp;
    private String access_token;

    private long caasOrderId_fromorder;
    private long supplierOrderId_fromorder;
    private int status_fromorder;

    private int checkNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partarrival);

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        initView();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caasOrderId_fromorder = bundle.getLong("caasOrderId");
        supplierOrderId_fromorder = bundle.getLong("supplierOrderId");
        status_fromorder = bundle.getInt("status");
        Log.i("123","caasOrderId: "+caasOrderId_fromorder+",supplierOrderId: "+supplierOrderId_fromorder+",status: "+status_fromorder);

        showAllOrder();

        lv_goods_partarrival.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.i("123","listview的项被点击");

                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ViewHolder viewHolder= (ViewHolder) view.getTag();

                // 改变CheckBox的状态
                viewHolder.checkbox_partarrival.toggle();

                // 将CheckBox的选中状况记录下来
                adapter.getMap().put(i, viewHolder.checkbox_partarrival.isChecked());


                if (viewHolder.checkbox_partarrival.isChecked()){
                    checkNum++;
                    hashMap.put(i,viewHolder);
                }else{
                    checkNum--;
                    hashMap.remove(i);
                }

                Log.i("123","已选中："+checkNum+"项");
            }
        });

    }

    private void initView() {
        toolbar_partarrival= (Toolbar) findViewById(R.id.toolbar_partarrival);
        setSupportActionBar(toolbar_partarrival);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_partarrival.setNavigationIcon(R.drawable.toolbar_back2);

        btn_partarrival= (Button) findViewById(R.id.btn_partarrival);
        btn_partarrival.setOnClickListener(this);

        tv_sellerNick_partarrival= (TextView) findViewById(R.id.tv_sellerNick_partarrival);
        lv_goods_partarrival= (ListView) findViewById(R.id.lv_goods_partarrival);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_partarrival:
                Set<Map.Entry<Integer, ViewHolder>> entries = hashMap.entrySet();
                Iterator<Map.Entry<Integer, ViewHolder>> iterator = entries.iterator();
                if (!iterator.hasNext()){
                    toast("请选择到货的商品");
                }else{
                    list_arrivalItem =new ArrayList<>();
                    int index=0;
                    while (iterator.hasNext()){
                        Map.Entry<Integer, ViewHolder> entry = iterator.next();
                        ViewHolder viewHolder = entry.getValue();

                        int itemId = viewHolder.itemId;
                        int arrivalQuantity = Integer.parseInt(viewHolder.tv_num_partarrival.getText().toString());
                        list_arrivalItem.add(index++,new ArrivalItem(itemId,arrivalQuantity));
                    }
                    Log.i("123","list_arrivalItem.size()："+ list_arrivalItem.size());
                    for (int j = 0; j < list_arrivalItem.size(); j++) {
                        Log.i("123","itemId: "+ list_arrivalItem.get(j).getItemId());
                        Log.i("123","arrivalQuantity: "+ list_arrivalItem.get(j).getArrivalQuantity());
                    }

//                    Gson gson=new Gson();
//                    String myPartArrival4Json = gson.toJson(new MyPartArrival4Json(access_token,(long)supplierOrderId_fromorder, list_arrivalItem));
//                    Log.i("123","myPartArrival4Json: "+myPartArrival4Json);
//                    partArrival(myPartArrival4Json);

                    partArrival(access_token,supplierOrderId_fromorder,list_arrivalItem);
                }
                break;
            default:
                break;
        }
    }

    //部分到货（方法一）
    private void partArrival(String token,long supplierOrderId,List<ArrivalItem> list_arrivalItem){
        Map<String,String> map=new HashMap<>();
        map.put("access_token",token);
        map.put("supplierOrderId",String.valueOf(supplierOrderId));
        String key4itemId="";
        String value4itemId="";
        String key4arrivalQuantity="";
        String value4arrivalQuantity="";
        for (int i = 0; i < list_arrivalItem.size(); i++) {
            key4itemId="arrivalItems["+i+"].itemId";
            value4itemId=String.valueOf(list_arrivalItem.get(i).getItemId());
            key4arrivalQuantity="arrivalItems["+i+"].arrivalQuantity";
            value4arrivalQuantity=String.valueOf(list_arrivalItem.get(i).getArrivalQuantity());
        }
        map.put(key4itemId,value4itemId);
        map.put(key4arrivalQuantity,value4arrivalQuantity);

        OkhttpUtil.post(Constants.ORDER_OP_PARTARRIVAL, map, new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Log.i("123","json: "+json);
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

    private void parseJson4partArrival(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            String prompt = jsonObject.optString("prompt");
            boolean success = jsonObject.optBoolean("success");
            final AlertDialog.Builder builder=new AlertDialog.Builder(PartArrivalActivity.this);
            if (success==false){
                Log.i("123","部分到货失败，prompt: "+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("123","部分到货失败后，刷新数据");
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.PARTARRIVAL,intent);
                                PartArrivalActivity.this.finish();
                            }
                        })
                        .setMessage(prompt)
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            } else{
                Log.i("123","部分到货成功，prompt"+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("123","部分到货成功后，刷新数据");
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.PARTARRIVAL,intent);
                                PartArrivalActivity.this.finish();
                            }
                        })
                        .setMessage("部分到货成功")
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            }

            //部分到货之后，发送广播
            Intent intent=new Intent(MyAction.PARTARRIVAL);
            sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //全部订单
    private void showAllOrder(){
        OkhttpUtil.get(Constants.ORDER_SUB_DEPT+ "access_token=" + access_token, new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Log.i("123","json: "+json);
                    Message msg = Message.obtain();
                    msg.obj=json;
                    msg.what=1;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void parseJson4Tocheck(String json_tocheck,final long caasOrderId_fromorder, final long supplierOrderId_fromorder){
        try {
            JSONObject jsonObject=new JSONObject(json_tocheck);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            if (jsonArray==null){
                Log.i("123","jsonArray为空");
            }else{
                Log.i("123","jsonArray不为空");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.optJSONObject(i);
                    JSONObject orderCaas = data.optJSONObject("orderCaas");
                    int ID = orderCaas.optInt("id");
                    long caasOrderId = orderCaas.optLong("caasOrderId");
                    String buyerNick = orderCaas.optString("buyerNick");//采购人
                    String teamName = orderCaas.optString("teamName");//课题组
                    String leaderNick = orderCaas.optString("leaderNick");//审批人
                    String projFeeFrom = orderCaas.optString("projFeeFrom");//经费来源

                    if (caasOrderId==caasOrderId_fromorder){
                        JSONArray supplierOrders = data.optJSONArray("supplierOrders");
                        for (int j = 0; j < supplierOrders.length(); j++) {
                            JSONObject supplierOrder = supplierOrders.optJSONObject(j);
                            int ID2 = supplierOrder.optInt("id");
                            long supplierOrderId = supplierOrder.optLong("supplierOrderId");
                            int status4supplierOrder = supplierOrder.optInt("status");//供应商里的status
                            String sellerNick = supplierOrder.optString("sellerNick");
                            String amountStr = supplierOrder.optString("amountStr");
                            JSONArray orderItemList = supplierOrder.optJSONArray("orderItemList");
                            int allQuantity=0;
                            if (supplierOrderId==supplierOrderId_fromorder){
                                list_goods=new ArrayList<>();
                                for (int k = 0; k < orderItemList.length(); k++) {
                                    JSONObject orderItem = orderItemList.optJSONObject(k);
                                    final int itemId = orderItem.optInt("itemId");
                                    String itemName = orderItem.optString("itemName");
                                    String supplierPartId = orderItem.optString("supplierPartId");
                                    String brandName = orderItem.optString("brandName");
                                    String sku = orderItem.optString("sku");
                                    String specifications = orderItem.optString("specifications");
                                    String cycle = orderItem.optString("cycle");
                                    final int quantity = orderItem.optInt("quantity");
                                    String unitPriceStr = orderItem.optString("unitPriceStr");
                                    int arrivalQuantity = orderItem.optInt("arrivalQuantity");
                                    allQuantity=allQuantity+quantity;
                                    list_goods.add(new MyPartArrivalGoods(itemId,R.mipmap.image_goods,itemName,supplierPartId,
                                            brandName,sku,specifications,cycle,unitPriceStr,quantity,arrivalQuantity,status4supplierOrder));
                                }
                                tv_sellerNick_partarrival.setText(sellerNick);
                                adapter=new OrderAdapter4PartArrival(PartArrivalActivity.this,list_goods);
                                lv_goods_partarrival.setAdapter(adapter);
                            }
                        }
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String json= (String) msg.obj;
                    parseJson4partArrival(json);
                    break;
                case 1:
                    String json_tocheck= (String) msg.obj;
                    parseJson4Tocheck(json_tocheck,caasOrderId_fromorder,supplierOrderId_fromorder);
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
