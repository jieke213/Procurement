package cn.net.caas.procurement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.MyAction;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.util.VolleyUtil;

/**
 * 研究所报销审核通过
 * Created by wjj on 2017/4/12.
 */
public class InstiuteToclaimeReviewPassActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_instiutetoclaimereview_pass;
    private Button btn_ok_instiutetoclaimereviewpass;

    private long caasOrderId;
    private long supplierOrderId;
    private int status;

    private SharedPreferences sp;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instiutetoclaimereviewpass);

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
        toolbar_instiutetoclaimereview_pass= (Toolbar) findViewById(R.id.toolbar_instiutetoclaimereview_pass);
        setSupportActionBar(toolbar_instiutetoclaimereview_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_instiutetoclaimereview_pass.setNavigationIcon(R.drawable.toolbar_back2);

        btn_ok_instiutetoclaimereviewpass= (Button) findViewById(R.id.btn_ok_instiutetoclaimereviewpass);
        btn_ok_instiutetoclaimereviewpass.setOnClickListener(this);
    }

    //所领导报销通过
    private void instiuteToclaimeReviewPass(String token,long supplierOrderId){
        Log.i("123","-------------------------------------------------------------------------------");
        Log.i("123",Constants.ORDER_OP_INSTIUTECLAIMSPASS + "access_token=" + token + "&subOrderId=" + supplierOrderId);
        VolleyUtil.get(Constants.ORDER_OP_INSTIUTECLAIMSPASS + "access_token=" + token + "&subOrderId=" + supplierOrderId, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String prompt = jsonObject.optString("prompt");
                boolean success = jsonObject.optBoolean("success");
                final AlertDialog.Builder builder=new AlertDialog.Builder(InstiuteToclaimeReviewPassActivity.this);
                if (success ==false){
                    Log.i("123","操作失败，prompt: "+prompt);
                    builder.setTitle("提示")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("123","操作失败后，刷新数据");
                                    builder.setCancelable(true);
                                    Intent intent=new Intent();
                                    setResult(ResultCode.INSTIUTE_TOCLAIME_PASS,intent);
                                    InstiuteToclaimeReviewPassActivity.this.finish();
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
                                    setResult(ResultCode.INSTIUTE_TOCLAIME_PASS,intent);
                                    InstiuteToclaimeReviewPassActivity.this.finish();
                                }
                            })
                            .setMessage("报销通过")
                            .create();
                            builder.setCancelable(false);
                            builder.show();
                }

                //研究所报销审核通过之后，发送广播
                Intent intent=new Intent(MyAction.INSTIUTE_TOCLAIME_PASS);
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
            case R.id.btn_ok_instiutetoclaimereviewpass:
                instiuteToclaimeReviewPass(access_token,supplierOrderId);
                break;
            default:
                break;
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
