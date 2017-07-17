package cn.net.caas.procurement.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by wjj on 2017/3/10.
 */
public abstract class BaseFragment4Order extends Fragment{
    public boolean isVisible;//是否课件
    public boolean isPrepared;//标志位，View是否已经初始化完成
    public boolean isFirstLoad=true;//是否是第一次加载

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible=true;
            lazyLoad();
        } else{
            isVisible=false;
        }
    }

    public abstract void lazyLoad();
}
