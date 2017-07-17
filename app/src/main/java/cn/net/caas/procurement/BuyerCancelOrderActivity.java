package cn.net.caas.procurement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.MyAction;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.util.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 采购员取消订单
 * Created by wjj on 2017/4/13.
 */
public class BuyerCancelOrderActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_buyercancelorder;
    private Button btn_buyercancelorder;
    private EditText et_msg_buyercancelorder;

    private SharedPreferences sp;
    private String access_token;

    private long caasOrderId;
    private long supplierOrderId;
    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyercancelorder);

        initView();

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caasOrderId = bundle.getLong("caasOrderId");
        supplierOrderId = bundle.getLong("supplierOrderId");
        status = bundle.getInt("status");
        Log.i("123","caasOrderId: "+caasOrderId+",supplierOrderId: "+supplierOrderId+",status: "+status);
    }

    private void initView() {
        toolbar_buyercancelorder= (Toolbar) findViewById(R.id.toolbar_buyercancelorder);
        setSupportActionBar(toolbar_buyercancelorder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_buyercancelorder.setNavigationIcon(R.drawable.toolbar_back2);

        btn_buyercancelorder= (Button) findViewById(R.id.btn_buyercancelorder);
        btn_buyercancelorder.setOnClickListener(this);

        et_msg_buyercancelorder= (EditText) findViewById(R.id.et_msg_buyercancelorder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_buyercancelorder:
                String refuseMsg = et_msg_buyercancelorder.getText().toString().trim();
                if (refuseMsg.equals("")){
                    toast("订单取消的原因不能为空！");
                    return;
                }
                cancelOrder(access_token,supplierOrderId,refuseMsg);
                break;
            default:
                break;
        }
    }

    private void cancelOrder(String token,long supplierOrderId,String refuseMsg){
        Log.i("123",Constants.ORDER_OP_BUYERCANCEL + "access_token=" + token + "&orderId=" + supplierOrderId+"&refuseMsg="+refuseMsg);
        OkhttpUtil.get(Constants.ORDER_OP_BUYERCANCEL + "access_token=" + token + "&orderId=" + supplierOrderId+"&refuseMsg="+refuseMsg, new OkhttpUtil.Listener() {
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

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String json= (String) msg.obj;
                    parseJson(json);
                    break;
                default:
                    break;
            }
        }
    };

    private void parseJson(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            String prompt = jsonObject.optString("prompt");
            boolean success = jsonObject.optBoolean("success");
            final AlertDialog.Builder builder=new AlertDialog.Builder(BuyerCancelOrderActivity.this);
            if (success==false){
                Log.i("123","操作失败，prompt: "+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("123","操作失败后，刷新数据");
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.CANCEL_ORDER,intent);
                                BuyerCancelOrderActivity.this.finish();
                            }
                        })
                        .setMessage(prompt)
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            } else{
                Log.i("123","操作成功，prompt"+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("123","操作成功后，刷新数据");
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.CANCEL_ORDER,intent);
                                BuyerCancelOrderActivity.this.finish();
                            }
                        })
                        .setMessage("操作成功")
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            }

            //取消订单之后，发送广播
            Intent intent=new Intent(MyAction.CANCEL_ORDER);
            sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
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

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
