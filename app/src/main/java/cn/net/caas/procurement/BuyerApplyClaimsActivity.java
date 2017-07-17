package cn.net.caas.procurement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.MyAction;
import cn.net.caas.procurement.constant.ResultCode;
import cn.net.caas.procurement.util.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 采购员申请报销
 * Created by wjj on 2017/4/13.
 */
public class BuyerApplyClaimsActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar_buyerapplyclaims;
    private Button btn_buyerapplyclaims;
    private Button btn_upload;
    private ImageView image_upload;
    private TextView tv_upload;
    private TextView tv_progress;

    private SharedPreferences sp;
    private String access_token;

    private static final int RESULT=1;

    private long caasOrderId;
    private long supplierOrderId;
    private int status;

    private String picturePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyerapplyclaims);

        initView();

        //从本地获取access_token
        sp=getSharedPreferences(Constants.LOGIN_INFO,MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caasOrderId = bundle.getLong("caasOrderId");
        supplierOrderId = bundle.getLong("supplierOrderId");
        status = bundle.getInt("status");
        Log.i("123","caasOrderId: "+caasOrderId+",supplierOrderId: "+supplierOrderId+",status: "+status);

    }

    private void initView() {
        toolbar_buyerapplyclaims= (Toolbar) findViewById(R.id.toolbar_buyerapplyclaims);
        setSupportActionBar(toolbar_buyerapplyclaims);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_buyerapplyclaims.setNavigationIcon(R.drawable.toolbar_back2);

        btn_buyerapplyclaims= (Button) findViewById(R.id.btn_buyerapplyclaims);
        btn_buyerapplyclaims.setOnClickListener(this);

        tv_upload= (TextView) findViewById(R.id.tv_upload);
        tv_upload.setVisibility(View.VISIBLE);
        image_upload= (ImageView) findViewById(R.id.image_upload);
        image_upload.setVisibility(View.INVISIBLE);
        btn_upload= (Button) findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(this);

        tv_progress= (TextView) findViewById(R.id.tv_progress);
        tv_progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_buyerapplyclaims:
                if (picturePath.endsWith(".jpg")
                        || picturePath.endsWith(".png")
                        || picturePath.endsWith(".jpeg")
                        || picturePath.endsWith(".JPG")
                        || picturePath.endsWith(".JPEG")
                        ){
                    tv_progress.setVisibility(View.VISIBLE);
                    applyClaims(access_token,supplierOrderId,picturePath);
                }else{
                    toast("只支持.jpg和.png格式的图片");
                }
                break;
            case R.id.btn_upload:
                //从相册中选择图片
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT);
                break;
            default:
                break;
        }
    }

    private void applyClaims(String token,long supplierOrderId,String picturePath){
        File file=new File(picturePath);
        OkhttpUtil.uploadFile(Constants.ORDER_OP_BUYERAPPLYCLAIMS,token,String.valueOf(supplierOrderId), file, new OkhttpUtil.Listener() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(1);
            }
            @Override
            public void onReponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Log.i("123","json: "+json);
                    Message msg = Message.obtain();
                    msg.obj=json;
                    msg.what=0;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //选中图片后，返回当前界面 ，并将选择的图片显示到指定的框中
            tv_upload.setVisibility(View.INVISIBLE);
            image_upload.setVisibility(View.VISIBLE);

            //得到原始图片
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            saveBitmapTofile(bitmap, Environment.getExternalStorageDirectory().getPath()+"/1.jpg");

            //压缩图片大小
//            Bitmap bitmap_size = zoomImg(bitmap, tv_upload.getWidth(), tv_upload.getHeight());
            Bitmap bitmap_size;
            int scale=bitmap.getHeight()/bitmap.getWidth();
            if (bitmap.getHeight()>bitmap.getWidth()){
                bitmap_size = getSmallBitmap(picturePath,tv_upload.getHeight()/scale,tv_upload.getHeight());
                saveBitmapTofile(bitmap_size, Environment.getExternalStorageDirectory().getPath()+"/2.jpg");
            }else{
                bitmap_size = getSmallBitmap(picturePath,tv_upload.getWidth(),tv_upload.getWidth()*scale);
                saveBitmapTofile(bitmap_size, Environment.getExternalStorageDirectory().getPath()+"/2.jpg");
            }

            //压缩图片质量
            Bitmap bitmap_quality = compressQuality(bitmap_size);
            saveBitmapTofile(bitmap_quality, Environment.getExternalStorageDirectory().getPath()+"/3.jpg");

            image_upload.setImageBitmap(bitmap_quality);

        }
    }

    private boolean saveBitmapTofile(Bitmap bmp, String filename) {
        if (bmp == null || filename == null)
            return false;
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }

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

    //压缩图片大小
    public static Bitmap getSmallBitmap(String filePath,int width,int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //inJustDecodeBounds设置为true，可以不把图片读到内存中,但依然可以计算出图片的大小
        BitmapFactory.decodeFile(filePath, options);


        // Calculate inSampleSize
        options.inSampleSize = compressSize(options, width, height);


        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //此时返回的就是已经被压缩的bitmap。。可以用于显示在界面上
        return BitmapFactory.decodeFile(filePath, options);
    }

    //图片大小的压缩
    public static int compressSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;    //图片的原始高 宽
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //图片质量的压缩
    private Bitmap compressQuality(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        Log.i("123","baos.toByteArray().length: "+baos.toByteArray().length/1024);
        while ( baos.toByteArray().length / 1024>200) {    //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            Log.i("123","baos.toByteArray().length: "+baos.toByteArray().length/1024);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String json= (String) msg.obj;
                    parseJson(json);
                    break;
                case 1:
                    tv_progress.setVisibility(View.INVISIBLE);
                    toast("发票上传失败");
                    break;
                default:
                    break;
            }
        }
    };

    private void parseJson(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            String prompt = jsonObject.optString("prompt");
            boolean success = jsonObject.optBoolean("success");
            final AlertDialog.Builder builder=new AlertDialog.Builder(BuyerApplyClaimsActivity.this);

            tv_progress.setVisibility(View.INVISIBLE);
            if (success==false){
                Log.i("123","申请报销失败，prompt: "+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.APPLYCLAIMS_MYORDER,intent);
                                BuyerApplyClaimsActivity.this.finish();
                            }
                        })
                        .setMessage(prompt)
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            } else{
                Log.i("123","申请报销成功，prompt"+prompt);
                builder.setTitle("提示")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.setCancelable(true);
                                Intent intent=new Intent();
                                setResult(ResultCode.APPLYCLAIMS_MYORDER,intent);
                                BuyerApplyClaimsActivity.this.finish();
                            }
                        })
                        .setMessage("申请报销成功")
                        .create();
                        builder.setCancelable(false);
                        builder.show();
            }

            //申请报销之后，发送广播
            Intent intent=new Intent(MyAction.APPLYCLAIMS);
            sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
