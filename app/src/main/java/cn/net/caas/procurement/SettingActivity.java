package cn.net.caas.procurement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.net.caas.procurement.constant.Constants;

/**
 * Created by wjj on 2017/4/6.
 */
public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar_setting;
    private Button btn_logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                OrderOkDialog dialog=new OrderOkDialog(SettingActivity.this);
//                dialog.show();

                SharedPreferences sp = getSharedPreferences(Constants.LOGIN_INFO, MODE_PRIVATE);
                boolean isCommit = sp.edit().clear().commit();
                if (isCommit) {
                    toast("已退出当前登录！");
                    Intent intent = new Intent();
                    setResult(004, intent);
                    finish();
                }
            }
        });
    }

    private void initView() {
        toolbar_setting = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_logout = (Button) findViewById(R.id.btn_logout);
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

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
