package cn.net.caas.procurement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.constant.RequestCode;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.fragment.FragmentMyOrder4All4Buyer4Leader;
import cn.net.caas.procurement.fragment.FragmentMyOrder4Delivered4Buyer4Leader;
import cn.net.caas.procurement.fragment.FragmentMyOrder4Notpass4Buyer4Leader;
import cn.net.caas.procurement.fragment.FragmentMyOrder4Unclaimed4Buyer4Leader;

/**
 * 我的订单
 * Created by wjj on 2017/4/9.
 */
public class MyOrderActivity4Buyer4Leader extends AppCompatActivity {
    private Toolbar toolbar_myorder4leader;
    private TextView tv_myorder4leader_all,tv_myorder4leader_unclaimed,tv_myorder4leader_delivered,tv_myorder4leader_notPass;
    private View view_tab_myorder4leader;
    private ViewPager viewpager_myorder4leader;

    private FragmentMyOrder4All4Buyer4Leader fragmentMyOrder4All;
    private FragmentMyOrder4Unclaimed4Buyer4Leader fragmentMyOrder4Unclaimed;
    private FragmentMyOrder4Delivered4Buyer4Leader fragmentMyOrder4Delivered;
    private FragmentMyOrder4Notpass4Buyer4Leader fragmentMyOrder4Notpass;

    private MyFragmentPagerAdapter adapter=null;
    private List<Fragment> list_fragment=null;

    private int width;//tab下划线的宽度
    private int currentIndex = 0;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder4leader);

        initView();
        setListener();
        initTabLine();

        fragmentMyOrder4All = FragmentMyOrder4All4Buyer4Leader.newInstance();
        fragmentMyOrder4Unclaimed = FragmentMyOrder4Unclaimed4Buyer4Leader.newInstance();
        fragmentMyOrder4Delivered = FragmentMyOrder4Delivered4Buyer4Leader.newInstance();
        fragmentMyOrder4Notpass = FragmentMyOrder4Notpass4Buyer4Leader.newInstance();
        list_fragment=new ArrayList<>();
        list_fragment.add(fragmentMyOrder4All);
        list_fragment.add(fragmentMyOrder4Unclaimed);
        list_fragment.add(fragmentMyOrder4Delivered);
        list_fragment.add(fragmentMyOrder4Notpass);

        adapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewpager_myorder4leader.setAdapter(adapter);

        viewpager_myorder4leader.addOnPageChangeListener(listener);
    }

    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            Log.i("12345","onPageSelected");
            currentIndex = position;//记录出当前显示的page

            // page切换后，更改标题文字的色彩 1：还原所有，2：根据当前设置
            tv_myorder4leader_all.setTextColor(Color.BLACK);
            tv_myorder4leader_unclaimed.setTextColor(Color.BLACK);
            tv_myorder4leader_delivered.setTextColor(Color.BLACK);
            tv_myorder4leader_notPass.setTextColor(Color.BLACK);

            switch (position){
                case 0:
                    tv_myorder4leader_all.setTextColor(0xffFF2D2D);
                    break;
                case 1:
                    tv_myorder4leader_unclaimed.setTextColor(0xffFF2D2D);
                    break;
                case 2:
                    tv_myorder4leader_delivered.setTextColor(0xffFF2D2D);
                    break;
                case 3:
                    tv_myorder4leader_notPass.setTextColor(0xffFF2D2D);
                    break;
                default:
                    break;
            }
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setSelected(position,positionOffset,currentIndex);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setSelected(int position, float positionOffset,int currentIndex){
        if (position == currentIndex) {
            params.leftMargin = (int) (width * positionOffset + width * currentIndex);
        } else {
            params.leftMargin = (int) (-width * (1 - positionOffset) + width * currentIndex);
        }
        view_tab_myorder4leader.setLayoutParams(params);
    }

    private void initView() {
        toolbar_myorder4leader = (Toolbar) findViewById(R.id.toolbar_myorder4leader);
        setSupportActionBar(toolbar_myorder4leader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_myorder4leader.setNavigationIcon(R.drawable.toolbar_back2);

        tv_myorder4leader_all= (TextView) findViewById(R.id.tv_myorder4leader_all);
        tv_myorder4leader_unclaimed= (TextView) findViewById(R.id.tv_myorder4leader_unclaimed);
        tv_myorder4leader_delivered= (TextView) findViewById(R.id.tv_myorder4leader_delivered);
        tv_myorder4leader_notPass= (TextView) findViewById(R.id.tv_myorder4leader_notPass);
        view_tab_myorder4leader=findViewById(R.id.view_tab_myorder4leader);
        viewpager_myorder4leader= (ViewPager) findViewById(R.id.viewpager_myorder4leader);
    }

    // 给标题文字设置点击事件，点击时更改page
    private void setListener() {
        tv_myorder4leader_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_myorder4leader.setCurrentItem(0);
            }
        });
        tv_myorder4leader_unclaimed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_myorder4leader.setCurrentItem(1);
            }
        });
        tv_myorder4leader_delivered.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_myorder4leader.setCurrentItem(2);
            }
        });
        tv_myorder4leader_notPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_myorder4leader.setCurrentItem(3);
            }
        });
    }

    /**
     * 初始化tab下划线
     */
    private void initTabLine() {
        params = (LinearLayout.LayoutParams) view_tab_myorder4leader.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / 4;
        params.width = width;
        view_tab_myorder4leader.setLayoutParams(params);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }
        @Override
        public int getCount() {
            return list_fragment.size();
        }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("123","MyOrderActivity4Buyer4Leader得到返回值");
        Log.i("123","requestCode: "+requestCode+",resultCode: "+resultCode);
        if (requestCode==RequestCode.APPLYCLAIMS_MYORDER && resultCode== ResultCode.APPLYCLAIMS_MYORDER){
            fragmentMyOrder4Unclaimed.isRefresh=true;
            viewpager_myorder4leader.setCurrentItem(1);
        }
    }

    private void toast(String str){
        Toast.makeText(MyOrderActivity4Buyer4Leader.this, str, Toast.LENGTH_SHORT).show();
    }
}
