package cn.net.caas.procurement.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.icu.text.DisplayContext;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import cn.net.caas.procurement.R;

/**
 * Created by wjj on 2017/6/26.
 */
public class MyEditText extends EditText{
    private Drawable drawableRight;
    boolean isHasTouch=false;

    public MyEditText(Context context) {
        this(context,null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context,attrs,android.R.attr.editTextStyle);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDrawableInvisible();
    }

    private void setDrawableInvisible() {
        drawableRight = getCompoundDrawables()[2];
        if (drawableRight==null){
            drawableRight = getResources().getDrawable(R.drawable.password_invisible);
        }
        drawableRight.setBounds(0,0,drawableRight.getIntrinsicWidth(),drawableRight.getIntrinsicHeight());
    }

    private void setDrawableVisible() {
        drawableRight = getCompoundDrawables()[2];
        if (drawableRight==null){
            drawableRight = getResources().getDrawable(R.drawable.password_visible);
        }
        drawableRight.setBounds(0,0,drawableRight.getIntrinsicWidth(),drawableRight.getIntrinsicHeight());
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()== MotionEvent.ACTION_UP){
            Log.i("123","抬起");
            if (getCompoundDrawables()[2]!=null){
                isHasTouch = (event.getX() > getWidth()-getTotalPaddingRight()) && (event.getX() < getWidth()-getPaddingRight());
                if (isHasTouch){
                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Log.i("123","改变图片");
                    setDrawableVisible();
                    isHasTouch=false;
                }else{
                    setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    setDrawableInvisible();
                    isHasTouch=true;
                }
            }
        }

        return super.onTouchEvent(event);
    }
}
