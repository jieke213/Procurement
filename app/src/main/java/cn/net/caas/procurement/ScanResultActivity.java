package cn.net.caas.procurement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by wjj on 2017/3/31.
 */
public class ScanResultActivity extends AppCompatActivity {
    private TextView tv_scanResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanresult);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String scanResult = bundle.getString("scanResult");

        tv_scanResult= (TextView) findViewById(R.id.tv_scanResult);
        tv_scanResult.setText(scanResult);
    }
}
