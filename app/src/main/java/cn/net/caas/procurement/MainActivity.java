package cn.net.caas.procurement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.fragment.BaseFragment;
import cn.net.caas.procurement.fragment.CartFragment;
import cn.net.caas.procurement.fragment.CategoryFragment;
import cn.net.caas.procurement.fragment.HomeFragment;
import cn.net.caas.procurement.fragment.MineFragment;

/**
 * 主界面
 * Created by wjj on 2017/3/6.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout item_home;
    private LinearLayout item_category;
    private LinearLayout item_cart;
    private LinearLayout item_myorder;
    private ImageView image_home;
    private ImageView image_category;
    private ImageView image_cart;
    private ImageView image_myorder;
    private TextView tv_home;
    private TextView tv_category;
    private TextView tv_cart;
    private TextView tv_myorder;

    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private CartFragment cartFragment;
    private MineFragment mineFragment;

    private FragmentManager fragmentManager=null;
    private FragmentTransaction transaction =null;

    public static String currentFragmentTag="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("123","MainActivity---onCreate");

        initView();

        fragmentManager=getSupportFragmentManager();

        setTabSelection(0);//第一次开启程序时默认打开的fragment

        //在此处添加一行
    }

    private void initView() {
        item_home= (LinearLayout) findViewById(R.id.item_home);
        item_category = (LinearLayout) findViewById(R.id.item_other);
        item_cart= (LinearLayout) findViewById(R.id.item_cart);
        item_myorder= (LinearLayout) findViewById(R.id.item_myorder);
        item_home.setOnClickListener(this);
        item_category.setOnClickListener(this);
        item_cart.setOnClickListener(this);
        item_myorder.setOnClickListener(this);
        image_home= (ImageView) findViewById(R.id.image_home);
        image_category = (ImageView) findViewById(R.id.image_other);
        image_cart= (ImageView) findViewById(R.id.image_cart);
        image_myorder= (ImageView) findViewById(R.id.image_myorder);
        tv_home= (TextView) findViewById(R.id.tv_home);
        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_cart= (TextView) findViewById(R.id.tv_cart);
        tv_myorder= (TextView) findViewById(R.id.tv_myorder);
    }

    //清除掉所有选中项的颜色
    private void clearSelection(){
        image_home.setImageResource(R.drawable.guide_home_up);
        image_category.setImageResource(R.drawable.guide_discover_up);
        image_cart.setImageResource(R.drawable.guide_cart_up);
        image_myorder.setImageResource(R.drawable.guide_account_up);
//        image_home.setColorFilter(Color.BLACK);
//        image_category.setColorFilter(Color.BLACK);
//        image_cart.setColorFilter(Color.BLACK);
//        image_myorder.setColorFilter(Color.BLACK);
        tv_home.setTextColor(Color.BLACK);
        tv_category.setTextColor(Color.BLACK);
        tv_cart.setTextColor(Color.BLACK);
        tv_myorder.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View view){
        int index=-1;
        switch (view.getId()){
            case R.id.item_home:
                index=0;
                break;
            case R.id.item_other:
                index=1;
                break;
            case R.id.item_cart:
                index=2;
                break;
            case R.id.item_myorder:
                index=3;
                break;
            default:
                break;
        }
        setTabSelection(index);
    }


    //设置选中的Fragment
    public void setTabSelection(int index){
        transaction=fragmentManager.beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置Fragment的颜色渐变
        clearSelection();//每次选中之前，先清除掉上次的选中状态
        hideFragments(transaction);
        switch (index){
            case 0:
                image_home.setImageResource(R.drawable.guide_home_down);
                tv_home.setTextColor(0xffD94600);
                if (homeFragment==null){
                    homeFragment = (HomeFragment) BaseFragment.newInstance(Constants.FRAGMENT_FLAG_HOME);
                }

                if (!homeFragment.isAdded()){
                    transaction.add(R.id.fragment_content,homeFragment);
                }else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                image_category.setImageResource(R.drawable.guide_discover_down);
                tv_category.setTextColor(0xffD94600);
                if (categoryFragment ==null){
                    categoryFragment = (CategoryFragment) BaseFragment.newInstance(Constants.FRAGMENT_FLAG_OTHER);
                }
                if (!categoryFragment.isAdded()){
                    transaction.add(R.id.fragment_content, categoryFragment);
                }else {
                    transaction.show(categoryFragment);
                }
                break;
            case 2:
                image_cart.setImageResource(R.drawable.guide_cart_down);
                tv_cart.setTextColor(0xffD94600);
                if (cartFragment==null){
                    cartFragment= (CartFragment) BaseFragment.newInstance(Constants.FRAGMENT_FLAG_CART);
                }
                if (!cartFragment.isAdded()){
                    transaction.add(R.id.fragment_content,cartFragment);
                }else {
                    transaction.show(cartFragment);
                }
                break;
            case 3:
                image_myorder.setImageResource(R.drawable.guide_account_down);
                tv_myorder.setTextColor(0xffD94600);
                if (mineFragment ==null){
                    mineFragment = (MineFragment) BaseFragment.newInstance(Constants.FRAGMENT_FLAG_MINE);
                }
                if (!mineFragment.isAdded()){
                    transaction.add(R.id.fragment_content, mineFragment);
                }else {
                    transaction.show(mineFragment);
                }
                break;
            default:
                break;
        }
        if (transaction !=null && !transaction.isEmpty()){
            transaction.commit();
        }
    }

    //隐藏掉所有的Fragment
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (categoryFragment != null) {
            transaction.hide(categoryFragment);
        }
        if (cartFragment != null) {
            transaction.hide(cartFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_account:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.login, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("123","MainActivity---onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("123","MainActivity---onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("123","MainActivity---onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("123","MainActivity---onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("123","MainActivity---onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("123","MainActivity---onDestroy");
    }

    private void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
