package cn.net.caas.procurement;

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

import cn.net.caas.procurement.fragment.FragmentLeaderBuyReview;
import cn.net.caas.procurement.fragment.FragmentLeaderBuyReviewAll;

/**
 * 课题组长采购审核
 * Created by wjj on 2017/4/7.
 */
public class LeaderBuyReviewActivity extends AppCompatActivity {
    private Toolbar toolbar_leaderbuyreview;
    private TextView tv_leaderbuyreview,tv_leaderbuyreview_all;
    private View view_tab_leaderbuyreview;
    private ViewPager viewpager_leaderbuyreview;

    private FragmentLeaderBuyReview fragmentLeaderBuyReview;
    private FragmentLeaderBuyReviewAll fragmentLeaderBuyReviewAll;

    private MyFragmentPagerAdapter adapter=null;
    private List<Fragment> list_fragment=null;

    private int width;//tab下划线的宽度
    private int currentIndex = 0;
    private LinearLayout.LayoutParams params;

    private int order_flag=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderbuyreview);

        initView();
        setListener();
        initTabLine();

        fragmentLeaderBuyReview = FragmentLeaderBuyReview.newInstance();
        fragmentLeaderBuyReviewAll = FragmentLeaderBuyReviewAll.newInstance();
        list_fragment=new ArrayList<>();
        list_fragment.add(fragmentLeaderBuyReview);
        list_fragment.add(fragmentLeaderBuyReviewAll);

        adapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewpager_leaderbuyreview.setAdapter(adapter);

        viewpager_leaderbuyreview.addOnPageChangeListener(listener);
    }

    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            Log.i("12345","onPageSelected");
            currentIndex = position;//记录出当前显示的page

            // page切换后，更改标题文字的色彩 1：还原所有，2：根据当前设置
            tv_leaderbuyreview.setTextColor(Color.BLACK);
            tv_leaderbuyreview_all.setTextColor(Color.BLACK);

            switch (position){
                case 0:
                    tv_leaderbuyreview.setTextColor(0xffFF2D2D);
                    break;
                case 1:
                    tv_leaderbuyreview_all.setTextColor(0xffFF2D2D);
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
        view_tab_leaderbuyreview.setLayoutParams(params);
    }

    private void initView() {
        toolbar_leaderbuyreview = (Toolbar) findViewById(R.id.toolbar_leaderbuyreview);
        setSupportActionBar(toolbar_leaderbuyreview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_leaderbuyreview.setNavigationIcon(R.drawable.toolbar_back2);

        tv_leaderbuyreview= (TextView) findViewById(R.id.tv_leaderbuyreview);
        tv_leaderbuyreview_all= (TextView) findViewById(R.id.tv_leaderbuyreview_all);
        view_tab_leaderbuyreview=findViewById(R.id.view_tab_leaderbuyreview);
        viewpager_leaderbuyreview= (ViewPager) findViewById(R.id.viewpager_leaderbuyreview);
    }

    // 给标题文字设置点击事件，点击时更改page
    private void setListener() {
        tv_leaderbuyreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_leaderbuyreview.setCurrentItem(0);
            }
        });
        tv_leaderbuyreview_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewpager_leaderbuyreview.setCurrentItem(1);
            }
        });
    }

    /**
     * 初始化tab下划线
     */
    private void initTabLine() {
        params = (LinearLayout.LayoutParams) view_tab_leaderbuyreview.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / 2;
        params.width = width;
        view_tab_leaderbuyreview.setLayoutParams(params);
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

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
