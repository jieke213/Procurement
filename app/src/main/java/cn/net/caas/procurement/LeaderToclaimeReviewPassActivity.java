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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.MyAction;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.util.OkhttpUtil;
import cn.net.caas.procurement.util.VolleyUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 课题组长报销审核通过
 * Created by wjj on 2017/4/12.
 */
public class LeaderToclaimeReviewPassActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_leadertoclaimereview_pass;
    private Button btn_ok_leadertoclaimereviewpass;

    private long caasOrderId;
    private long supplierOrderId;
    private int status;

    private SharedPreferences sp;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadertoclaimereviewpass);

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caasOrderId = bundle.getLong("caasOrderId");
        supplierOrderId = bundle.getLong("supplierOrderId");
        status = bundle.getInt("status");
        Log.i("123","caasOrderId: "+caasOrderId+",status: "+status);

        initView();

    }

    private void initView() {
        toolbar_leadertoclaimereview_pass= (Toolbar) findViewById(R.id.toolbar_leadertoclaimereview_pass);
        setSupportActionBar(toolbar_leadertoclaimereview_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_leadertoclaimereview_pass.setNavigationIcon(R.drawable.toolbar_back2);

        btn_ok_leadertoclaimereviewpass= (Button) findViewById(R.id.btn_ok_leadertoclaimereviewpass);
        btn_ok_leadertoclaimereviewpass.setOnClickListener(this);
    }

    //课题组长报销通过
    private void leaderToclaimeReviewPass(String token,long supplierOrderId){
        Log.i("123",Constants.ORDER_OP_LEADERCLAIMSPASS + "access_token=" + token + "&subOrderId=" + supplierOrderId);
        VolleyUtil.get(Constants.ORDER_OP_LEADERCLAIMSPASS + "access_token=" + token + "&subOrderId=" + supplierOrderId, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String prompt = jsonObject.optString("prompt");
                boolean success = jsonObject.optBoolean("success");
                final AlertDialog.Builder builder=new AlertDialog.Builder(LeaderToclaimeReviewPassActivity.this);
                if (success ==false){
                    Log.i("123","操作失败，prompt: "+prompt);
                    builder.setTitle("提示")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("123","操作失败后，刷新数据");
                                    builder.setCancelable(true);
                                    Intent intent=new Intent();
                                    setResult(ResultCode.LEADER_TOCLAIME_PASS,intent);
                                    LeaderToclaimeReviewPassActivity.this.finish();
                                }
                            })
                            .setMessage(prompt)
                            .create();
                            builder.setCancelable(false);
                            builder.show();
                } else{
                    Log.i("123","报销通过，prompt: "+prompt);
                    builder.setTitle("提示")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("123","报销通过后，刷新数据");
                                    builder.setCancelable(true);
                                    Intent intent=new Intent();
                                    setResult(ResultCode.LEADER_TOCLAIME_PASS,intent);
                                    LeaderToclaimeReviewPassActivity.this.finish();
                                }
                            })
                            .setMessage("报销通过")
                            .create();
                            builder.setCancelable(false);
                            builder.show();
                }

                //课题组长报销审核通过之后，发送广播
                Intent intent=new Intent(MyAction.LEADER_TOCLAIME_PASS);
                sendBroadcast(intent);

            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok_leadertoclaimereviewpass:
                leaderToclaimeReviewPass(access_token,supplierOrderId);
                break;
            default:
                break;
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:

                    break;
                default:
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
