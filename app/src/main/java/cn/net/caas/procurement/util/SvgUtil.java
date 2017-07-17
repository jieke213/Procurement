package cn.net.caas.procurement.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.IOException;

/**
 * Created by wjj on 2017/5/3.
 */
public class SvgUtil {
    public static Bitmap getBitmapFromSvg(Context context){
        try {
            SVG svg = SVGParser.getSVGFromAsset(context.getAssets(), "ins300.svg");
            PictureDrawable pictureDrawable = svg.createPictureDrawable();
            Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
