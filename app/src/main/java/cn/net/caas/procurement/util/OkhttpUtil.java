package cn.net.caas.procurement.util;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.net.caas.procurement.entity.MyPartArrivalGoods;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wjj on 2017/1/18.
 */
public class OkhttpUtil {
    private static OkHttpClient okHttpClient=null;
    private static Request.Builder requestBuilder=null;

    private static final long CONN_TIME=30;  //连接超时时间
    private static final long WIRTE_TIME=10; //写超时时间
    private static final long READ_TIME=30;  //读超时时间

    private static MyCallback callback=null;
    private static Call mCall=null;

    //自定义接口
    public interface Listener{
        void onFailure(Call call, IOException e);
        void onReponse(Call call, Response response);
    }

    //初始化，并且设置请求超时（连接超时时间，写超时时间，读超时时间）
    private static void init(long connTime,long writeTime,long readTime){
        requestBuilder=new Request.Builder();
        okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(connTime, TimeUnit.SECONDS)
                .writeTimeout(writeTime,TimeUnit.SECONDS)
                .readTimeout(readTime,TimeUnit.SECONDS)
                .build();
    }


    //异步get请求
    public static void get(String url,final Listener listener){
        init(CONN_TIME,WIRTE_TIME,READ_TIME);

        Request request = requestBuilder.url(url).get().build();
        mCall = okHttpClient.newCall(request);

        Log.i("123","--------------callback为空："+(callback==null));
        Log.i("123","--------------call已经取消："+(mCall.isCanceled()));
        //第一种方法
        if (callback==null && !mCall.isCanceled()){
            callback=new MyCallback(listener);
            mCall.enqueue(callback);
            Log.i("123","--------------enqueue");
            Log.i("123","--------------cancel");
            callback=null;
        }

        //第二种方法
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                listener.onFailure(call, e);
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("123","--------------Callback");
//                listener.onReponse(call,response);
//            }
//        });

    }

    static class MyCallback implements Callback{

        private Listener mListener;

        public MyCallback(Listener listener){
            mListener=listener;
            Log.i("123","--------------MyCallback");
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mListener.onFailure(call, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i("123","--------------MyCallback--onResponse");
            mListener.onReponse(call,response);
        }
    }

    //异步post请求
    public static void post(String url, Map<String,String> map,final Listener listener){
        init(CONN_TIME,WIRTE_TIME,READ_TIME);

        FormBody.Builder formbodyBuilder=new FormBody.Builder();
        Set<String> set = map.keySet();
        for(String key:set){
            String value=map.get(key);
            formbodyBuilder.add(key,value);
        }
        FormBody formbody = formbodyBuilder.build();

        Request request = requestBuilder.post(formbody).url(url).build();
        Call call = okHttpClient.newCall(request);

        //第一种方法
        if (callback==null){
            callback=new MyCallback(listener);
            call.enqueue(callback);
            callback=null;
        }

        //第二种方法
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                listener.onFailure(call,e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                listener.onReponse(call,response);
//            }
//        });
    }

    //上传json
    public static void postJson(String url, String json, final Listener listener){
        init(CONN_TIME,WIRTE_TIME,READ_TIME);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = requestBuilder.post(requestBody).url(url).build();
        Call call = okHttpClient.newCall(request);

        //第一种方法
        if (callback==null){
            callback=new MyCallback(listener);
            call.enqueue(callback);
            callback=null;
        }

    }

    //上传文件
    public static void uploadFile(String url,String access_token,String supplierOrderId,File file,final Listener listener){
        init(CONN_TIME,WIRTE_TIME,READ_TIME);

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("invoicePicUrl", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"),file))
                .addFormDataPart("access_token",access_token)
                .addFormDataPart("subOrderId",supplierOrderId)
                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        Call call = okHttpClient.newCall(request);

        //第一种方法
        if (callback==null){
            callback=new MyCallback(listener);
            call.enqueue(callback);
            callback=null;
        }
    }

    //取消Okhttp的请求
    public static void callAllQuest(){
        okHttpClient.dispatcher().cancelAll();
    }
}
