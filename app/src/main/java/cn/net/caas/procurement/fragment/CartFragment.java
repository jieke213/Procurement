package cn.net.caas.procurement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.net.caas.procurement.MainActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.constant.Constants;

/**
 * 购物车
 * Created by wjj on 2017/3/6.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private MainActivity mainActivity;

    private Button btn_jump;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        mainActivity= (MainActivity) getActivity();

        initView();

        return view;
    }

    private void initView() {
        btn_jump= (Button) view.findViewById(R.id.btn_jump);
        btn_jump.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_jump:
                mainActivity.setTabSelection(0);//点击“去逛逛”，跳转到首页
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentTag= Constants.FRAGMENT_FLAG_CART;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            Log.i("123","CartFragment隐藏");
        }else{
            Log.i("123","CartFragment显示");
        }
    }

}
