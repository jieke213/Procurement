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
 * 研究所采购审核拒绝
 * Created by wjj on 2017/4/12.
 */
public class InstiuteBuyReviewRefuseActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_instiutebuyreview_refuse;
    private EditText et_msg_instiutebuyreviewrefuse;
    private Button btn_cancel_instiutebuyreviewrefuse;

    private long caasOrderId;
    private int status;

    private SharedPreferences sp;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instiutebuyreviewrefuse);

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
        toolbar_instiutebuyreview_refuse= (Toolbar) findViewById(R.id.toolbar_instiutebuyreview_refuse);
        setSupportActionBar(toolbar_instiutebuyreview_refuse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_instiutebuyreview_refuse.setNavigationIcon(R.drawable.toolbar_back2);

        et_msg_instiutebuyreviewrefuse= (EditText) findViewById(R.id.et_msg_instiutebuyreviewrefuse);
        btn_cancel_instiutebuyreviewrefuse= (Button) findViewById(R.id.btn_cancel_instiutebuyreviewrefuse);
        btn_cancel_instiutebuyreviewrefuse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel_instiutebuyreviewrefuse:
                String msg = et_msg_instiutebuyreviewrefuse.getText().toString().trim();
                if (msg.equals("")){
                    toast("请填写拒绝的原因");
                    return ;
                }
                instiuteBuyReviewRefuse(caasOrderId,msg,access_token);
                break;
            default:
                break;
        }
    }

    //研究所拒绝
    private void instiuteBuyReviewRefuse(long orderId,String refuseMsg,String token) {
        OkhttpUtil.get(Constants.INSTIUTE_REFUSE + "access_token=" + token + "&orderId=" + orderId + "&refuseMsg=" + refuseMsg, new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Log.i("123", "json: " + json);
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
                    try {
                        String json= (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(json);
                        String prompt = jsonObject.optString("prompt");
                        boolean success = jsonObject.optBoolean("success");
                        final AlertDialog.Builder builder=new AlertDialog.Builder(InstiuteBuyReviewRefuseActivity.this);
                        if (success ==false){
                            builder.setTitle("提示")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            builder.setCancelable(true);
                                            Intent intent=new Intent();
                                            setResult(ResultCode.INSTIUTE_BUY_REFUSE,intent);
                                            InstiuteBuyReviewRefuseActivity.this.finish();
                                        }
                                    })
                                    .setMessage(prompt)
                                    .create();
                                    builder.setCancelable(false);
                                    builder.show();
                        } else{
                            builder.setTitle("提示")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            builder.setCancelable(true);
                                            Intent intent=new Intent();
                                            setResult(ResultCode.INSTIUTE_BUY_REFUSE,intent);
                                            InstiuteBuyReviewRefuseActivity.this.finish();
                                        }
                                    })
                                    .setMessage("取消订单成功")
                                    .create();
                                    builder.setCancelable(false);
                                    builder.show();
                        }

                        //研究所采购审核拒绝之后，发送广播
                        Intent intent=new Intent(MyAction.INSTIUTE_BUY_REFUSE);
                        sendBroadcast(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
