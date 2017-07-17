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
 * 课题组长采购审核通过
 * Created by wjj on 2017/4/12.
 */
public class LeaderBuyReviewPassActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_leaderbuyreview_pass;
    private Spinner sp_teamName_leaderbuyreviewpass;
    private EditText et_projFeeFrom_leaderbuyreviewpass;
    private Button btn_ok_leaderbuyreviewpass;

    private List<String> list_teamName=new ArrayList<>();
    private List<Integer> list_teamId=new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private int teamId;
    private long caasOrderId;
    private int status;

    private SharedPreferences sp;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderbuyreviewpass);

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caasOrderId = bundle.getLong("caasOrderId");
        status = bundle.getInt("status");
        Log.i("123","caasOrderId: "+caasOrderId+",status: "+status);

        initView();

        queryTeam(access_token);//获取课题组
        sp_teamName_leaderbuyreviewpass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("123","选择的是："+ list_teamName.get(i));
                teamId=list_teamId.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }

    private void initView() {
        toolbar_leaderbuyreview_pass= (Toolbar) findViewById(R.id.toolbar_leaderbuyreview_pass);
        setSupportActionBar(toolbar_leaderbuyreview_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_leaderbuyreview_pass.setNavigationIcon(R.drawable.toolbar_back2);

        sp_teamName_leaderbuyreviewpass= (Spinner) findViewById(R.id.sp_teamName_leaderbuyreviewpass);
        et_projFeeFrom_leaderbuyreviewpass= (EditText) findViewById(R.id.et_projFeeFrom_leaderbuyreviewpass);
        btn_ok_leaderbuyreviewpass= (Button) findViewById(R.id.btn_ok_leaderbuyreviewpass);
        btn_ok_leaderbuyreviewpass.setOnClickListener(this);
    }

    //查询课题组
    private void queryTeam(String access_token){
        Log.i("123", Constants.TEAMNAME_URL + "access_token=" + access_token);
        OkhttpUtil.get(Constants.TEAMNAME_URL + "access_token=" + access_token, new OkhttpUtil.Listener(){
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json_team = response.body().string();
                    Log.i("123","json_team: "+json_team);
                    JSONObject jsonObject=new JSONObject(json_team);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        String deptName = data.getString("deptName");
                        int teamId = data.getInt("id");
                        list_teamName.add(deptName);
                        list_teamId.add(teamId);
                    }
                    adapter=new ArrayAdapter<>(LeaderBuyReviewPassActivity.this,R.layout.spinnner_item,R.id.tv_team, list_teamName);
                    adapter.setDropDownViewResource(R.layout.spinnner_item);
                    handler.sendEmptyMessage(2);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //课题组长审核通过
    private void leaderBuyReviewPass(long orderId,int teamId,String projFeeFrom,String token){
        VolleyUtil.get(Constants.LEADER_PASS + "access_token=" + token + "&orderId=" + orderId + "&teamId=" + teamId + "&projFeeFrom=" + projFeeFrom, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String prompt = jsonObject.optString("prompt");
                boolean success = jsonObject.optBoolean("success");
                final AlertDialog.Builder builder=new AlertDialog.Builder(LeaderBuyReviewPassActivity.this);
                if (success ==false){
                    Log.i("123","审核不通过，prompt: "+prompt);
                    builder.setTitle("提示")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("123","审核不通过后，刷新数据");
                                    builder.setCancelable(true);
                                    Intent intent=new Intent();
                                    setResult(ResultCode.LEADER_BUY_PASS,intent);
                                    LeaderBuyReviewPassActivity.this.finish();
                                }
                            })
                            .setMessage(prompt)
                            .create();
                            builder.setCancelable(false);
                            builder.show();
                } else{
                    Log.i("123","审核通过，prompt: "+prompt);
                    builder.setTitle("提示")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("123","审核通过后，刷新数据");
                                    builder.setCancelable(true);
                                    Intent intent=new Intent();
                                    setResult(ResultCode.LEADER_BUY_PASS,intent);
                                    LeaderBuyReviewPassActivity.this.finish();
                                }
                            })
                            .setMessage("审批通过")
                            .create();
                            builder.setCancelable(false);
                            builder.show();
                }

                //课题组长采购审核通过之后，发送广播
                Intent intent=new Intent(MyAction.LEADER_BUY_PASS);
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
            case R.id.btn_ok_leaderbuyreviewpass:
                String projFeeFrom = et_projFeeFrom_leaderbuyreviewpass.getText().toString().trim();
                //经费来源是可以为空的
//                if (projFeeFrom.equals("")){
//                    toast("经费来源不能为空！");
//                    return ;
//                }
                leaderBuyReviewPass(caasOrderId,teamId,projFeeFrom,access_token);
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
                    sp_teamName_leaderbuyreviewpass.setAdapter(adapter);
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
