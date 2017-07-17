package cn.net.caas.procurement.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.net.caas.procurement.application.ProcurementApplication;

/**
 * Created by wjj on 2017/1/20.
 */
public class VolleyUtil {

    //从网络上获取图片的方式（可缓存，不可指定图片的长和宽）
    public static void loadImage(Context context, String url, final ImageView imageView, final Bitmap defaultImage, final Bitmap errorImage){
        ImageLoader imageLoader=new ImageLoader(ProcurementApplication.getRequestQueue(),new MyImageCache(context));
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                } else if (defaultImage!=null){
                    imageView.setImageBitmap(defaultImage);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null){
                    imageView.setImageBitmap(errorImage);
                }
            }
        });
    }

    //从网络上获取图片的方式（可缓存，可指定图片的长和宽）
    public static void loadImage(Context context, String url, final ImageView imageView, final Bitmap defaultImage, final Bitmap errorImage,
                                 int maxWidth,int maxHeight){
        ImageLoader imageLoader=new ImageLoader(ProcurementApplication.getRequestQueue(),new MyImageCache(context));
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                } else if (defaultImage!=null){
                    imageView.setImageBitmap(defaultImage);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null){
                    imageView.setImageBitmap(errorImage);
                }
            }
        },maxWidth,maxHeight);
    }

    //从网络上获取图片的方式（可缓存，不可指定图片的长和宽，不可以显示加载中的图片和加载失败后的图片）
    public static void loadImage(Context context,String url, final ImageView imageView){
        ImageLoader imageLoader=new ImageLoader(ProcurementApplication.getRequestQueue(),new MyImageCache(context));
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null){
                    volleyError.getMessage();
                    volleyError.printStackTrace();
                }
            }
        });
    }

    //从网络上获取图片的方式（可缓存，可指定图片的长和宽，不可以显示加载中的图片和加载失败后的图片）
    public static void loadImage(Context context,String url, final ImageView imageView,int maxWidth,int maxHeight){
        ImageLoader imageLoader=new ImageLoader(ProcurementApplication.getRequestQueue(),new MyImageCache(context));
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null){
                    volleyError.getMessage();
                    volleyError.printStackTrace();
                }
            }
        },maxWidth,maxHeight);
    }


    //自定义接口
    public interface Listener{
        void onResponse(JSONObject jsonObject);
        void onErrorResponse(VolleyError volleyError);
    }

    public static void get(String url, final Listener listener){
        RequestQueue requestQueue = ProcurementApplication.getRequestQueue();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                listener.onResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onErrorResponse(volleyError);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000*30,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public static void post(String url, final Listener listener){
        RequestQueue requestQueue = ProcurementApplication.getRequestQueue();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                listener.onResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onErrorResponse(volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                return super.getParams();
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000*30,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
}
