package cn.net.caas.procurement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.net.caas.procurement.constant.Constants;

/**
 * Created by wjj on 2017/3/6.
 */
public class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static BaseFragment newInstance(String tag) {
        BaseFragment baseFragment = null;
        if (tag.equals(Constants.FRAGMENT_FLAG_HOME)) {
            baseFragment = new HomeFragment();
        } else if (tag.equals(Constants.FRAGMENT_FLAG_OTHER)) {
            baseFragment = new CategoryFragment();
        } else if (tag.equals(Constants.FRAGMENT_FLAG_CART)) {
            baseFragment = new CartFragment();
        } else if (tag.equals(Constants.FRAGMENT_FLAG_MINE)){
            baseFragment = new MineFragment();
        }

        return baseFragment;
    }
}
