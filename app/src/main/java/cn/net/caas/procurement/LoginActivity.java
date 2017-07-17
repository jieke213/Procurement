package cn.net.caas.procurement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.util.OkhttpUtil;
import cn.net.caas.procurement.util.VolleyUtil;
import cn.net.caas.procurement.view.MyTextView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_login=null;
    private TextView tv_register=null;
    private Toolbar toolbar=null;
    private ImageView image_login=null;
    private CheckBox checkBox=null;
    private EditText et_login_username=null;
    private EditText et_login_password=null;
    private Button btn_login_pwd=null;
    private MyTextView tv_welcome=null;

    private SharedPreferences sp=null;

    private String username=null;
    private String password=null;
    private boolean isPwdVisible=false;

    private static final long TOKEN_SAVE_TIME=20; //7天=7*24*60*60

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();

        setAccountImage();

        updateAccessToken();//如果本地保存的access_token超过7天，则去服务端重新获取access_token
    }

    //每七天更新一次access_token
    private void updateAccessToken(){
        long period = getTokenSaveTime();//获得token在本地保存的时间
        //token在本地保存超过了7天
        if(period>TOKEN_SAVE_TIME){
            username = sp.getString(Constants.USERNAME, "error");
            password = sp.getString(Constants.PASSWORD, "error");
            OkhttpUtil.get(Constants.LOGIN_BASE_URL+"userName=" + username + "&passwd=" + password, new OkhttpUtil.Listener() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onReponse(Call call, Response response) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject=new JSONObject(json);
                        String access_token = jsonObject.optString("data");
                        Log.i("123",access_token);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Constants.ACCESS_TOKEN,access_token);
                        editor.putLong(Constants.TOKEN_SAVE_TIME,System.currentTimeMillis());
                        editor.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //获得token在本地保存的时间
    private long getTokenSaveTime(){
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        long startSaveTime = sp.getLong(Constants.TOKEN_SAVE_TIME, 0);
        if (startSaveTime!=0){
            long period=(System.currentTimeMillis()-startSaveTime)/1000;
            Log.i("123","token在本地保存了"+period+"秒");
            return period;
        }
        return -1;
    }

    //初始化控件
    private void initView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.toolbar_back2);

        btn_login= (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_register= (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        image_login= (ImageView) findViewById(R.id.image_login);
        checkBox= (CheckBox) findViewById(R.id.cb_save_login);
        et_login_username= (EditText) findViewById(R.id.et_login_username);
        et_login_password= (EditText) findViewById(R.id.et_login_password);
        btn_login_pwd= (Button) findViewById(R.id.btn_login_pwd);
        btn_login_pwd.setOnClickListener(this);
        tv_welcome= (MyTextView) findViewById(R.id.tv_welcome);
        tv_welcome.setText("欢  迎  登  录  农  科  院  采  购  平  台");
    }

    //从服务端获取用户头像
    private void setAccountImage(){
        WindowManager wm = (WindowManager) this.getSystemService(this.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        //华为：width: 1080,height: 1812 模拟器：width: 480,height: 752
        ViewGroup.LayoutParams layoutParams = image_login.getLayoutParams();
        layoutParams.width=width/6;
        layoutParams.height=height/6;
        image_login.setLayoutParams(layoutParams);//设置头像指定的宽和高为屏幕宽度的1/6

        //获取用户头像
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_default);
        Bitmap errorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_error);
        VolleyUtil.loadImage(this,Constants.ACCOUNT_URL,image_login,defaultBitmap,errorBitmap,width/6,width/6);

    }


    //是否保存用户信息
    private boolean isSaveLoginInfo(){
        if (checkBox.isChecked()){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                username=et_login_username.getText().toString().trim();
                password=et_login_password.getText().toString().trim();
                if (username.equals("")){
                    toast("用户名为空！");
                    return;
                }
                if (password.equals("")){
                    toast("密码为空！");
                    return;
                }

                String access_token = sp.getString(Constants.ACCESS_TOKEN, "error");
                //本地有token则直接从本地获取，如果没有则通过用户名和密码从服务端获取token，并把token传给订单界面
                if (!access_token.equals("error")){
                    //本地有token但是token已经过期，也从服务端重新获取
                    if (getTokenSaveTime()<TOKEN_SAVE_TIME){
                        toast("登录成功");
                        Intent intent = new Intent();
                        intent.putExtra("token",access_token);
                        setResult(002,intent);
                        finish();
                    } else{
                        getTokenFromServerToLogin();
                    }
                } else{
                    getTokenFromServerToLogin();

                }
                break;
            case R.id.btn_login_pwd:
                if (!isPwdVisible){
                    btn_login_pwd.setBackgroundResource(R.drawable.password_visible);
                    et_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPwdVisible=true;
                }else{
                    btn_login_pwd.setBackgroundResource(R.drawable.password_invisible);
                    et_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    isPwdVisible=false;
                }
                et_login_password.setSelection(et_login_password.getText().length());//使光标时刻在最后面
                break;
            case R.id.tv_register:
                toast("注册");
                break;
            default:
                break;
        }
    }

    private void getTokenFromServerToLogin(){
        VolleyUtil.get(Constants.LOGIN_BASE_URL + "userName=" + username + "&passwd=" + password, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String access_token = jsonObject.optString("data");
                boolean success = jsonObject.optBoolean("success");
                Log.i("123",access_token);

                if (success==false){
                    toast("登录失败，用户名或密码有误");
                    return;
                }
//                if (isSaveLoginInfo()){
//                    long startSaveTime = System.currentTimeMillis();
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("username",username);
//                    editor.putString("password",password);
//                    editor.putString("access_token",access_token);
//                    editor.putLong("startSaveTime",startSaveTime);
//                    editor.commit();
//                }
                long startSaveTime = System.currentTimeMillis();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username",username);
                editor.putString("password",password);
                editor.putString("access_token",access_token);
                editor.putLong("startSaveTime",startSaveTime);
                editor.commit();

                Intent intent = new Intent();
                intent.putExtra("token",access_token);
                setResult(002,intent);
                toast("登录成功");
                finish();
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
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


    @Override
    protected void onStart() {
        super.onStart();
//        if (JPushInterface.isPushStopped(this)){
//            JPushInterface.resumePush(this);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (!JPushInterface.isPushStopped(this)){
//            JPushInterface.stopPush(this);
//        }
    }

    private void toast(String str){
        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
