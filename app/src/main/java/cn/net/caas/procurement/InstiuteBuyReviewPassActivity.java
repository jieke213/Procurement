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
 * 研究所采购审核通过
 * Created by wjj on 2017/4/12.
 */
public class InstiuteBuyReviewPassActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_instiutebuyreview_pass;
    private Button btn_ok_instiutebuyreviewpass;

    private long caasOrderId;
    private int status;

    private SharedPreferences sp;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instiutebuyreviewpass);

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caasOrderId = bundle.getLong("caasOrderId");
        status = bundle.getInt("status");
        Log.i("123","caasOrderId: "+caasOrderId+",status: "+status);

        initView();

    }

    private void initView() {
        toolbar_instiutebuyreview_pass= (Toolbar) findViewById(R.id.toolbar_instiutebuyreview_pass);
        setSupportActionBar(toolbar_instiutebuyreview_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_instiutebuyreview_pass.setNavigationIcon(R.drawable.toolbar_back2);

        btn_ok_instiutebuyreviewpass= (Button) findViewById(R.id.btn_ok_instiutebuyreviewpass);
        btn_ok_instiutebuyreviewpass.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok_instiutebuyreviewpass:
                instiuteReview(caasOrderId,access_token);
                break;
            default:
                break;
        }
    }

    //研究所审核
    private void instiuteReview(long orderId,String token){
        VolleyUtil.get(Constants.INSTIUTE_PASS + "access_token=" + token + "&orderId=" + orderId, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String prompt = jsonObject.optString("prompt");
                boolean success = jsonObject.optBoolean("success");
                final AlertDialog.Builder builder=new AlertDialog.Builder(InstiuteBuyReviewPassActivity.this);
                if (success==false){
                    Log.i("123","研究所审核不通过，prompt: "+prompt);
                    builder.setTitle("提示")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("123","研究所审核不通过后，刷新数据");
                                    builder.setCancelable(true);
                                    Intent intent=new Intent();
                                    setResult(ResultCode.INSTIUTE_BUY_PASS,intent);
                                    InstiuteBuyReviewPassActivity.this.finish();
                                }
                            })
                            .setMessage(prompt)
                            .create();
                            builder.setCancelable(false);
                            builder.show();
                } else{
                    Log.i("123","研究所审核通过，prompt"+prompt);
                    builder.setTitle("提示")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("123","研究所审核通过后，刷新数据");
                                    builder.setCancelable(true);
                                    Intent intent=new Intent();
                                    setResult(ResultCode.INSTIUTE_BUY_PASS,intent);
                                    InstiuteBuyReviewPassActivity.this.finish();
                                }
                            })
                            .setMessage("审批通过")
                            .create();
                            builder.setCancelable(false);
                            builder.show();
                }

                //研究所采购审核通过之后，发送广播
                Intent intent=new Intent(MyAction.INSTIUTE_BUY_PASS);
                sendBroadcast(intent);
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
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
