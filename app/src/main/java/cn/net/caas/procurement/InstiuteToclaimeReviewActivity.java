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
import cn.net.caas.procurement.fragment.FragmentInstiuteToclaimeReview;
import cn.net.caas.procurement.fragment.FragmentInstiuteToclaimeReviewAll;
import cn.net.caas.procurement.fragment.FragmentLeaderToclaimeReview;
import cn.net.caas.procurement.fragment.FragmentLeaderToclaimeReviewAll;

/**
 * 研究所报销审核
 * Created by wjj on 2017/4/7.
 */
public class InstiuteToclaimeReviewActivity extends AppCompatActivity {

    private Toolbar toolbar_instiutetoclaimereview;
    private TextView tv_instiutetoclaimereview,tv_instiutetoclaimereview_all;
    private View view_tab_instiutetoclaimereview;
    private ViewPager viewpager_instiutetoclaimereview;

    private FragmentInstiuteToclaimeReview fragmentInstiuteToclaimeReview;
    private FragmentInstiuteToclaimeReviewAll fragmentInstiuteToclaimeReviewAll;

    private MyFragmentPagerAdapter adapter=null;
    private List<Fragment> list_fragment=null;

    private int width;//tab下划线的宽度
    private int currentIndex = 0;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instiutetoclaimereview);

        initView();
        setListener();
        initTabLine();

        fragmentInstiuteToclaimeReview = FragmentInstiuteToclaimeReview.newInstance();
        fragmentInstiuteToclaimeReviewAll = FragmentInstiuteToclaimeReviewAll.newInstance();
        list_fragment=new ArrayList<>();
        list_fragment.add(fragmentInstiuteToclaimeReview);
        list_fragment.add(fragmentInstiuteToclaimeReviewAll);

        adapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewpager_instiutetoclaimereview.setAdapter(adapter);

        viewpager_instiutetoclaimereview.addOnPageChangeListener(listener);
    }

    private void initView() {
        toolbar_instiutetoclaimereview = (Toolbar) findViewById(R.id.toolbar_instiutetoclaimereview);
        setSupportActionBar(toolbar_instiutetoclaimereview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_instiutetoclaimereview.setNavigationIcon(R.drawable.toolbar_back2);

        tv_instiutetoclaimereview= (TextView) findViewById(R.id.tv_instiutetoclaimereview);
        tv_instiutetoclaimereview_all= (TextView) findViewById(R.id.tv_instiutetoclaimereview_all);
        view_tab_instiutetoclaimereview=findViewById(R.id.view_tab_instiutetoclaimereview);
        viewpager_instiutetoclaimereview= (ViewPager) findViewById(R.id.viewpager_instiutetoclaimereview);
    }

    private void setListener() {
        tv_instiutetoclaimereview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_instiutetoclaimereview.setCurrentItem(0);
            }
        });
        tv_instiutetoclaimereview_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_instiutetoclaimereview.setCurrentItem(1);
            }
        });
    }

    private void initTabLine() {
        params = (LinearLayout.LayoutParams) view_tab_instiutetoclaimereview.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / 2;
        params.width = width;
        view_tab_instiutetoclaimereview.setLayoutParams(params);
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

    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            Log.i("12345","onPageSelected");
            currentIndex = position;//记录出当前显示的page

            // page切换后，更改标题文字的色彩 1：还原所有，2：根据当前设置
            tv_instiutetoclaimereview.setTextColor(Color.BLACK);
            tv_instiutetoclaimereview_all.setTextColor(Color.BLACK);

            switch (position){
                case 0:
                    tv_instiutetoclaimereview.setTextColor(0xffFF2D2D);
                    break;
                case 1:
                    tv_instiutetoclaimereview_all.setTextColor(0xffFF2D2D);
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
        view_tab_instiutetoclaimereview.setLayoutParams(params);
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
        if (requestCode== RequestCode.INSTIUTE_TOCLAIME_PASS && resultCode== ResultCode.INSTIUTE_TOCLAIME_PASS){
            fragmentInstiuteToclaimeReview.isRefresh=true;
            viewpager_instiutetoclaimereview.setCurrentItem(0);
        }
        if (requestCode== RequestCode.INSTIUTE_TOCLAIME_REFUSE && resultCode== ResultCode.INSTIUTE_TOCLAIME_REFUSE){
            fragmentInstiuteToclaimeReview.isRefresh=true;
            viewpager_instiutetoclaimereview.setCurrentItem(0);
        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
