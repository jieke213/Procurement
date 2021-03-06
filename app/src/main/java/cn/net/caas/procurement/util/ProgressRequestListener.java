package cn.net.caas.procurement.util;

/**
 * Created by wjj on 2017/4/19.
 */
/**
 * 请求体进度回调接口，比如用于文件上传中
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-09-02
 * Time: 17:16
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
