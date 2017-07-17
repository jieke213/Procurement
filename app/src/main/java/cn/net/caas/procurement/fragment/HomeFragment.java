package cn.net.caas.procurement.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.net.caas.procurement.MainActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.ScanResultActivity;
import cn.net.caas.procurement.SearchResultActivity;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.scan.MipcaActivityCapture;

/**
 * 首页
 * Created by wjj on 2017/3/6.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{
    private List<View> list_image=null;
    private ViewPager viewPager_circle;
    private CirclePageIndicator circlePageIndicator;
    private View view1,view2,view3,view4;
    private ImageView image1,image2,image3,image4;
    private ImageView image_static1,image_static2,image_static3,image_static4;
    private ImageView image_left,image_center,image_right;
    private Toolbar toolbar;
    private EditText et_search;
    private LinearLayout layout_scan;

    private MainActivity mainActivity;

    private View view;

    private FrameLayout.LayoutParams params;

    private MyPagerAdapter adapter;

    private MyTimerTask timerTask=null;

    private final static int SCANNIN_GREQUEST_CODE = 1;

    private final static int SEARCH_REQUEST_CODE = 001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("123","HomeFragment---onCreateView");
        view = inflater.inflate(R.layout.fragment_home,container, false);

        mainActivity= (MainActivity) getActivity();

        initView();

        createPoster();

        cyclePoster();

        initStaticImage2();

        return view;
    }

    //初始化控件
    private void initView() {
        viewPager_circle= (ViewPager) view.findViewById(R.id.viewpager_circle_home);
        circlePageIndicator= (CirclePageIndicator) view.findViewById(R.id.circle_indicator_home);

        view1 = LayoutInflater.from(getActivity()).inflate(R.layout.image_poster1, null);
        view2 = LayoutInflater.from(getActivity()).inflate(R.layout.image_poster2, null);
        view3 = LayoutInflater.from(getActivity()).inflate(R.layout.image_poster3, null);
        view4 = LayoutInflater.from(getActivity()).inflate(R.layout.image_poster4, null);
        image1= (ImageView) view1.findViewById(R.id.image1);
        image2= (ImageView) view2.findViewById(R.id.image2);
        image3= (ImageView) view3.findViewById(R.id.image3);
        image4= (ImageView) view4.findViewById(R.id.image4);

        toolbar= (Toolbar) view.findViewById(R.id.toolbar_home);
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        et_search= (EditText) view.findViewById(R.id.et_search_home);
        et_search.setOnClickListener(this);
        et_search.setFocusable(false);
        layout_scan= (LinearLayout) view.findViewById(R.id.layout_scan);
        layout_scan.setOnClickListener(this);

        image_static1= (ImageView) view.findViewById(R.id.image_static1);
        image_static2= (ImageView) view.findViewById(R.id.image_static2);
        image_static3= (ImageView) view.findViewById(R.id.image_static3);
        image_static4= (ImageView) view.findViewById(R.id.image_static4);

        image_left= (ImageView) view.findViewById(R.id.image_left);
        image_center= (ImageView) view.findViewById(R.id.image_center);
        image_right= (ImageView) view.findViewById(R.id.image_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_search_home:
                Intent intent_searchresult2=new Intent(mainActivity, SearchResultActivity.class);
                startActivityForResult(intent_searchresult2,SEARCH_REQUEST_CODE);
                break;
            case R.id.layout_scan:
                Intent intent = new Intent();
                intent.setClass(mainActivity, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            default:
                break;
        }
    }

    //创建广告
    private void createPoster() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Log.i("123","width: "+width);
        Log.i("123","height: "+height);

        //设置ViewPager的高和宽
        params= (FrameLayout.LayoutParams) viewPager_circle.getLayoutParams();
        params.height=width/2;
        viewPager_circle.setLayoutParams(params);

        list_image=new ArrayList<>();
        list_image.add(view1);
        list_image.add(view2);
        list_image.add(view3);
        list_image.add(view4);

        //压缩图片
        Bitmap bitmap_old1=scaleImage(R.mipmap.slide1);
        Bitmap bitmap_old2=scaleImage(R.mipmap.slide2);
        Bitmap bitmap_old3=scaleImage(R.mipmap.slide3);
        Bitmap bitmap_old4=scaleImage(R.mipmap.slide4);

        Bitmap bitmap_new1 = zoomImg(bitmap_old1, width, width / 2);
        Bitmap bitmap_new2 = zoomImg(bitmap_old2, width, width / 2);
        Bitmap bitmap_new3 = zoomImg(bitmap_old3, width, width / 2);
        Bitmap bitmap_new4 = zoomImg(bitmap_old4, width, width / 2);
        image1.setImageBitmap(bitmap_new1);
        image2.setImageBitmap(bitmap_new2);
        image3.setImageBitmap(bitmap_new3);
        image4.setImageBitmap(bitmap_new4);

        //设置viewpager的指示器
        float density = getResources().getDisplayMetrics().density;
        circlePageIndicator.setFillColor(0xffffffff);
        circlePageIndicator.setRadius(5*density);

        adapter=new MyPagerAdapter();
        viewPager_circle.setAdapter(adapter);
        viewPager_circle.addOnPageChangeListener(listener);

        circlePageIndicator.setViewPager(viewPager_circle);
    }

    private Bitmap scaleImage(int resId){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int windowWidth = dm.widthPixels;
        int windowHeight = dm.heightPixels;
        Log.i("123","windowWidth: "+windowWidth+",windowHeight: "+windowHeight);

        int imageWidth2 = options.outWidth;
        int imageHeight2 = options.outHeight;
        Log.i("123","前：imageWidth: "+imageWidth2+",imageHeight: "+imageHeight2);

        BitmapFactory.decodeResource(getResources(),resId,options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        Log.i("123","后：imageWidth: "+imageWidth+",imageHeight: "+imageHeight);

        int scaleX = imageWidth / windowWidth;
        int scaleY = imageHeight / windowHeight;
        int scale=1;
        if (scaleX>scaleY && scaleY>=1){
            scale=scaleX;
        }
        if (scaleY>scaleX && scaleX>=1){
            scale=scaleY;
        }

        options.inJustDecodeBounds=false;
        options.inSampleSize=scale;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId, options);

        return bitmap;
    }

    private void initStaticImage2(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        Bitmap pic_static1 = scaleImage(R.mipmap.pic_static1);
        Bitmap pic_static2 = scaleImage(R.mipmap.pic_static2);
        Bitmap pic_static3 = scaleImage(R.mipmap.pic_static3);
        Bitmap pic_static4 = scaleImage(R.mipmap.pic_static4);
        pic_static1 = zoomImg(pic_static1, width, width * 3/4);
        pic_static2 = zoomImg(pic_static2, width, width * 3/4);
        pic_static3 = zoomImg(pic_static3, width, width * 3/4);
        pic_static4 = zoomImg(pic_static4, width, width * 3/4);
        image_static1.setImageBitmap(pic_static1);
        image_static2.setImageBitmap(pic_static2);
        image_static3.setImageBitmap(pic_static3);
        image_static4.setImageBitmap(pic_static4);

        Bitmap pic_left = scaleImage(R.mipmap.pic_left);
        Bitmap pic_center = scaleImage(R.mipmap.pic_center);
        Bitmap pic_right = scaleImage(R.mipmap.pic_right);
        pic_left = zoomImg(pic_left, width, width * 2/3);
        pic_center = zoomImg(pic_center, width, width * 2/3);
        pic_right = zoomImg(pic_right, width, width * 2/3);
        image_left.setImageBitmap(pic_left);
        image_center.setImageBitmap(pic_center);
        image_right.setImageBitmap(pic_right);
    }

    //广告位下面的静态图片排版
    private void initStaticImage(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;

        //压缩图片
        Bitmap bitmap_old1 = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_static1);
        Bitmap bitmap_old2 = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_static2);
        Bitmap bitmap_old3 = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_static3);
        Bitmap bitmap_old4 = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_static4);
        Bitmap bitmap_new1 = zoomImg(bitmap_old1, width, width * 3/4);
        Bitmap bitmap_new2 = zoomImg(bitmap_old2, width, width * 3/4);
        Bitmap bitmap_new3 = zoomImg(bitmap_old3, width, width * 3/4);
        Bitmap bitmap_new4 = zoomImg(bitmap_old4, width, width * 3/4);
        image_static1.setImageBitmap(bitmap_new1);
        image_static2.setImageBitmap(bitmap_new2);
        image_static3.setImageBitmap(bitmap_new3);
        image_static4.setImageBitmap(bitmap_new4);

        Bitmap bitmap_old_left = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_left);
        Bitmap bitmap_old_center = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_center);
        Bitmap bitmap_old_right = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_right);
        Bitmap bitmap_new_left = zoomImg(bitmap_old_left, width, width * 2/3);
        Bitmap bitmap_new_center = zoomImg(bitmap_old_center, width, width * 2/3);
        Bitmap bitmap_new_right = zoomImg(bitmap_old_right, width, width * 2/3);
        image_left.setImageBitmap(bitmap_new_left);
        image_center.setImageBitmap(bitmap_new_center);
        image_right.setImageBitmap(bitmap_new_right);
    }

    //定时轮播
    private void cyclePoster(){
        Timer timer=new Timer();
        if (timerTask == null){
            timerTask=new MyTimerTask();
            timer.schedule(timerTask,3000,3000);
        }

    }

    class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            int currentItem = viewPager_circle.getCurrentItem();

            if (currentItem == /*viewPager_circle.getAdapter()*/adapter.getCount()-1) {
                handler.sendEmptyMessage(1);
            } else{
                Message msg = Message.obtain();
                msg.obj=currentItem;
                msg.what=2;
                handler.sendMessage(msg);
            }
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    viewPager_circle.setCurrentItem(0);
                    break;
                case 2:
                    int position= (int) msg.obj;
                    viewPager_circle.setCurrentItem(position+1);
                    break;
                default:
                    break;
            }
        }
    };

    //压缩图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list_image.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list_image.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list_image.get(position));
            return list_image.get(position);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            Log.i("123","HomeFragment隐藏");
        }else{
            Log.i("123","HomeFragment显示");
        }
    }

    //扫描二维码之后，返回扫描结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == mainActivity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    String result = bundle.getString("result");

                    Intent intent=new Intent(mainActivity, ScanResultActivity.class);
                    bundle.putString("scanResult",result);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    //viewpager的滑动接口实现
    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position == 3){

            }
        }
        @Override
        public void onPageSelected(int position) {

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("123","HomeFragment---onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("123","HomeFragment---onResume");
        MainActivity.currentFragmentTag= Constants.FRAGMENT_FLAG_HOME;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("123","HomeFragment---onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("123","HomeFragment---onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("123","HomeFragment---onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("123","HomeFragment---onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("123","HomeFragment---onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("123","HomeFragment---onDetach");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("123","HomeFragment---onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("123","HomeFragment---onActivityCreated");
    }

    private void toast(String str){
        Toast.makeText(mainActivity, str, Toast.LENGTH_SHORT).show();
    }
}
