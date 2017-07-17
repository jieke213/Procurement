package cn.net.caas.procurement.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import cn.net.caas.procurement.OrderManagerActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.util.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wjj on 2017/1/16.
 */
public class MyReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    private String access_token;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        //1.自定义消息
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String msg_extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
//        Log.i("123","消息标题："+title+",消息内容："+message+",附加字段："+msg_extra+",唯一标识消息的ID："+msg_id);

        //2.通知
        String notification_title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        String notification_extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int notification_id = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//通知栏的notification的id
        String msg_id2 = bundle.getString(JPushInterface.EXTRA_MSG_ID);//唯一标识通知的id
//        Log.i("123","通知标题："+notification_title+",通知内容："+alert+",附加字段："+notification_extra+",通知栏中通知的ID："+notification_id+",唯一标识通知的ID："+msg_id2);

        //从本地获取access_token
        sp = context.getSharedPreferences(Constants.LOGIN_INFO, context.MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        //自定义消息
        if (msg_extra!=null){
            try {
                JSONObject jsonObject=new JSONObject(msg_extra);
                String content = jsonObject.optString(Constants.JPUSH_KEY);
                if (content.equals(Constants.JPUSH_ORDER)){
                    getNotification(access_token,context);
                    Toast.makeText(context, "接收到广播通知", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getNotification(String token,final Context context){
        Log.i("123","token: "+token);
        if (token!=null && !token.equals("error")){
            OkhttpUtil.get(Constants.NOTIFICATION + "access_token=" + token, new OkhttpUtil.Listener() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.i("123","getNotification: failure");
                }
                @Override
                public void onReponse(Call call, Response response) {
                    Log.i("123","getNotification: success");
//                try {
//                    String json = response.body().string();
//                    JSONObject jsonObject=new JSONObject(json);
//                    JSONObject jsonObject_data = jsonObject.optJSONObject("data");
//                    if (jsonObject_data == null){
//                        return;
//                    }
//                    JSONArray jsonArray_notifyList = jsonObject_data.optJSONArray("notifyList");
//                    for (int i = 0; i < jsonArray_notifyList.length(); i++) {
//                        JSONObject jsonObject_notify = jsonArray_notifyList.getJSONObject(i);
//                        int notifyId = jsonObject_notify.optInt("id");
//                        String title = jsonObject_notify.optString("title");
//                        String msg = jsonObject_notify.optString("msg");
//                        showNotification(notifyId,title,msg,context);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                    for (int i = 0; i < 2; i++) {
                        showNotification(i,"有未审核","有未审核",context);
                    }
                }
            });
        }

    }

    //Notification
    private void showNotification(int notifyId,String title,String msg,Context context){
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        builder.setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(PendingIntent.getActivity(context,1,new Intent(context,OrderManagerActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_SOUND)
        ;
        Notification notification = builder.build();
        notification.flags=Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notifyId,notification);
    }
}
