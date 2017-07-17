package cn.net.caas.procurement.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wjj on 2017/1/19.
 */
public class MyImageCache implements ImageLoader.ImageCache{
//    private Context context=null;

    private LruCache<String,Bitmap> lruCache=null;
    public DiskLruCache diskLruCache=null;

    private static final int MAX_MEMORY=10*1024*1024;

    public MyImageCache(Context context){
//        this.context=context;

        //创建LruCache实例
        lruCache=new LruCache<String,Bitmap>(MAX_MEMORY){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

        //创建DiskLruCache实例
        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            diskLruCache=DiskLruCache.open(cacheDir,getAppVersion(context),1,10*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从缓存中获取Bitmap（内存缓存和磁盘缓存）
    @Override
    public Bitmap getBitmap(String url){
        if (lruCache.get(url)!=null){
            //从LruCache中获取
            return lruCache.get(url);
        } else{
            String key = md5(url);
            try {
                if (diskLruCache.get(key) != null){
                    //从DiskLruCache中获取
                    DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
                    Bitmap bitmap=null;
                    if(snapshot!=null){
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        //存入LruCache缓存
                        lruCache.put(url,bitmap);
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //存入缓存（内存缓存和磁盘缓存）
    @Override
    public void putBitmap(String url,Bitmap bitmap){
        //存入LruCache缓存
        lruCache.put(url,bitmap);

        //判断是否存在DiskLruCache缓存，没有就存入
        String key = md5(url);
        try {
            if (diskLruCache.get(key) == null){
                DiskLruCache.Editor editor = diskLruCache.edit(key);
                if (editor!=null){
                    OutputStream os = editor.newOutputStream(0);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG,100,os)){
                        editor.commit();
                    } else{
                        editor.abort();
                    }
                }
                diskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取SD卡的缓存路径
    private File getDiskCacheDir(Context context, String fileName){
        String cachePath;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()){
            cachePath=context.getExternalCacheDir().getPath();
        } else {
            cachePath=context.getCacheDir().getPath();
        }
        return new File(cachePath+File.separator+fileName);
    }

    //获得应用程序的版本号
    private int getAppVersion(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    //将URL地址通过MD5算法加密，作为缓存文件的文件名
    public String md5(String url){
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /*    //将一张图片存储到缓存中
    public void addBitmapToCache(String url,Bitmap bitmap){
        if (lruCache.get(url)==null){
            lruCache.put(url,bitmap);
        }
    }

    //从缓存中获取一张图片
    public Bitmap getBitmapFromCache(String url, InputStream is){
        Bitmap bitmap=lruCache.get(url);
        if (bitmap!=null){
            Log.i("123","从LruCahce获取的bitmap");
            return bitmap;
        } else{
            String cacheFileName = md5(url);
            try {
                DiskLruCache.Snapshot snapshot = diskLruCache.get(cacheFileName);
                if (snapshot==null){
                    DiskLruCache.Editor editor = diskLruCache.edit(cacheFileName);
                    if (editor!=null){
                        OutputStream os = editor.newOutputStream(0);
                        BufferedInputStream bis=new BufferedInputStream(is);
                        int len;
                        while((len=bis.read())!=-1){
                            os.write(len);
                        }
                        os.close();
                        bis.close();
                        editor.commit();
                    }
                    snapshot=diskLruCache.get(cacheFileName);
                    if (snapshot!=null){
                        Log.i("123","snapshot!=null");
                        FileInputStream fis = (FileInputStream) snapshot.getInputStream(0);
                        FileDescriptor fileDescriptor = fis.getFD();
                        Log.i("123","fileDescriptor!=null");
                        Bitmap bitmap2=null;
                        if (fileDescriptor!=null){
                            bitmap2 = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                            if (bitmap2!=null){
                                lruCache.put(url,bitmap2);
                            }
                        }
                        fis.close();
                        Log.i("123","从DiskLruCahce获取的bitmap");
                        return bitmap2;
                    } else{
                        Log.i("123","snapshot==null");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
*/
}
