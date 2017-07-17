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
 * 验货通过
 * Created by wjj on 2017/4/13.
 */
public class CheckPassActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_check_pass;
    private Button btn_check_pass;

    private SharedPreferences sp;
    private String access_token;

    private long caasOrderId;
    private long supplierOrderId;
    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpass);

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
        toolbar_check_pass= (Toolbar) findViewById(R.id.toolbar_check_pass);
        setSupportActionBar(toolbar_check_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_check_pass.setNavigationIcon(R.drawable.toolbar_back2);

        btn_check_pass= (Button) findViewById(R.id.btn_check_pass);
        btn_check_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_check_pass:
                checkPass(access_token,supplierOrderId);
                break;
            default:
                break;
        }
    }

    private void checkPass(String token,long supplierOrderId){
        Log.i("123",Constants.ORDER_OP_CHECKPASS + "access_token=" + token + "&supplierOrderId=" + supplierOrderId);
        OkhttpUtil.get(Constants.ORDER_OP_CHECKPASS + "access_token=" + token + "&supplierOrderId=" + supplierOrderId, new OkhttpUtil.Listener() {
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
            final AlertDialog.Builder builder=new AlertDialog.Builder(CheckPassActivity.this);
            if (success==false){
                Log.i("123","验货不通过，prompt: "+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("123","验货不通过后，刷新数据");
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.CHECKPASS,intent);
                                CheckPassActivity.this.finish();
                            }
                        })
                        .setMessage(prompt)
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            } else{
                Log.i("123","验货通过，prompt"+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("123","验货通过后，刷新数据");
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.CHECKPASS,intent);
                                CheckPassActivity.this.finish();
                            }
                        })
                        .setMessage("验货通过")
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            }

            //验货通过之后，发送广播
            Intent intent=new Intent(MyAction.CHECK_PASS);
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
