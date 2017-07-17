package cn.net.caas.procurement.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by crystysal on 2017/1/16.
 */
public class ProcurementApplication extends Application{
    private static RequestQueue queue;

//    private ApplicationLike tinkerApplicationLike;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        queue= Volley.newRequestQueue(getApplicationContext());

//        if (BuildConfig.TINKER_ENABLE){
//            //我们可以从这里获得Tinker加载过程的信息
//            tinkerApplicationLike= TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
//
//            //初始化TinkerPatch SDK，更多配置可参照API章节中的初始化SDK
//            TinkerPatch.init(tinkerApplicationLike)
//                    .reflectPatchLibrary()
//                    .setPatchRollbackOnScreenOff(true)
//                    .setPatchRestartOnSrceenOff(true);
//
//            //每隔3小时去访问后台时候有更新，通过handler实现轮训的效果
//            new FetchPatchHandler().fetchPatchWithInterval(3);
//        }
    }

    public static RequestQueue getRequestQueue(){
        return queue;
    }
}
