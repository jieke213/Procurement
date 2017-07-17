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
import cn.net.caas.procurement.fragment.FragmentChecked4All;
import cn.net.caas.procurement.fragment.FragmentChecked4Detail;
import cn.net.caas.procurement.fragment.FragmentChecked4PartArrival;
import cn.net.caas.procurement.fragment.FragmentChecked4Tocheck;

/**
 * 验货员验货
 * Created by wjj on 2017/4/10.
 */
public class CheckedActivity extends AppCompatActivity {
    private Toolbar toolbar_checked;
    private TextView tv_checked_detail,tv_checked_tocheck,tv_checked_partArrival,tv_checked_all;
    private View view_tab_checked;
    private ViewPager viewpager_checked;

    private FragmentChecked4Detail fragmentChecked4Detail;
    private FragmentChecked4Tocheck fragmentChecked4Tocheck;
    private FragmentChecked4PartArrival fragmentChecked4PartArrival;
    private FragmentChecked4All fragmentChecked4All;

    private MyFragmentPagerAdapter adapter=null;
    private List<Fragment> list_fragment=null;

    private int width;//tab下划线的宽度
    private int currentIndex = 0;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked);

        initView();
        setListener();
        initTabLine();

        fragmentChecked4Detail = FragmentChecked4Detail.newInstance();
        fragmentChecked4Tocheck = FragmentChecked4Tocheck.newInstance();
        fragmentChecked4PartArrival = FragmentChecked4PartArrival.newInstance();
        fragmentChecked4All = FragmentChecked4All.newInstance();
        list_fragment=new ArrayList<>();
        list_fragment.add(fragmentChecked4Detail);
        list_fragment.add(fragmentChecked4Tocheck);
        list_fragment.add(fragmentChecked4PartArrival);
        list_fragment.add(fragmentChecked4All);

        adapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewpager_checked.setAdapter(adapter);

        viewpager_checked.addOnPageChangeListener(listener);
    }

    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            Log.i("12345","onPageSelected");
            currentIndex = position;//记录出当前显示的page

            // page切换后，更改标题文字的色彩 1：还原所有，2：根据当前设置
            tv_checked_detail.setTextColor(Color.BLACK);
            tv_checked_tocheck.setTextColor(Color.BLACK);
            tv_checked_partArrival.setTextColor(Color.BLACK);
            tv_checked_all.setTextColor(Color.BLACK);

            switch (position){
                case 0:
                    tv_checked_detail.setTextColor(0xffFF2D2D);
                    break;
                case 1:
                    tv_checked_tocheck.setTextColor(0xffFF2D2D);
                    break;
                case 2:
                    tv_checked_partArrival.setTextColor(0xffFF2D2D);
                    break;
                case 3:
                    tv_checked_all.setTextColor(0xffFF2D2D);
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
        view_tab_checked.setLayoutParams(params);
    }

    private void initView() {
        toolbar_checked = (Toolbar) findViewById(R.id.toolbar_checked);
        setSupportActionBar(toolbar_checked);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_checked.setNavigationIcon(R.drawable.toolbar_back2);

        tv_checked_detail= (TextView) findViewById(R.id.tv_checked_detail);
        tv_checked_tocheck= (TextView) findViewById(R.id.tv_checked_tocheck);
        tv_checked_partArrival= (TextView) findViewById(R.id.tv_checked_partArrival);
        tv_checked_all= (TextView) findViewById(R.id.tv_checked_all);
        view_tab_checked=findViewById(R.id.view_tab_checked);
        viewpager_checked= (ViewPager) findViewById(R.id.viewpager_checked);
    }

    // 给标题文字设置点击事件，点击时更改page
    private void setListener() {
        tv_checked_detail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_checked.setCurrentItem(0);
            }
        });
        tv_checked_tocheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_checked.setCurrentItem(1);
            }
        });
        tv_checked_partArrival.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_checked.setCurrentItem(2);
            }
        });
        tv_checked_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_checked.setCurrentItem(3);
            }
        });
    }

    /**
     * 初始化tab下划线
     */
    private void initTabLine() {
        params = (LinearLayout.LayoutParams) view_tab_checked.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / 4;
        params.width = width;
        view_tab_checked.setLayoutParams(params);
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
        //待验货
        if (requestCode== RequestCode.CHECKPASS_TOCHECK && resultCode== ResultCode.CHECKPASS){
            fragmentChecked4Tocheck.isRefresh=true;
            viewpager_checked.setCurrentItem(1);
        }
        if (requestCode== RequestCode.PARTARRIVAL_TOCHECK && resultCode== ResultCode.PARTARRIVAL){
            fragmentChecked4Tocheck.isRefresh=true;
            viewpager_checked.setCurrentItem(1);
        }
        if (requestCode== RequestCode.COMPLETEREFUND_TOCHECK && resultCode== ResultCode.COMPLETEREFUND){
            fragmentChecked4Tocheck.isRefresh=true;
            viewpager_checked.setCurrentItem(1);
        }

        //部分到货
        if (requestCode==RequestCode.CHECKPASS_PARTARRIVAL && resultCode==ResultCode.CHECKPASS){
            fragmentChecked4PartArrival.isRefresh=true;
            viewpager_checked.setCurrentItem(2);
        }
        if (requestCode==RequestCode.PARTARRIVAL_PARTARRIVAL && resultCode==ResultCode.PARTARRIVAL){
            fragmentChecked4PartArrival.isRefresh=true;
            viewpager_checked.setCurrentItem(2);
        }
        if (requestCode==RequestCode.COMPLETEREFUND_PARTARRIVAL && resultCode==ResultCode.COMPLETEREFUND){
            fragmentChecked4PartArrival.isRefresh=true;
            viewpager_checked.setCurrentItem(2);
        }
    }

    private void toast(String str){
        Toast.makeText(CheckedActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
