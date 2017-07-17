package cn.net.caas.procurement.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cn.net.caas.procurement.R;

/**
 * Created by wjj on 2017/6/27.
 */
public class MyTextView extends TextView {
    private int duration;
    private int text_color;
    private int text_size;
    private int i=0;
    private String text;
    private String substring;
    private Timer timer;

    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index){
                case R.styleable.MyTextView_duration:
                    duration = typedArray.getInteger(index, 0);
                    break;
                case R.styleable.MyTextView_text_color:
                    text_color=typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.MyTextView_text_size:
                    text_size=typedArray.getDimensionPixelSize
                            (index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        text = getText().toString();
        timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                substring = text.substring(i, text.length());
                i++;
                if (i>=text.length()){
                    i=0;
                }
                postInvalidate();
            }
        };
        timer.schedule(timerTask,500,duration);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (substring!=null){
            Paint paint=new Paint();
            paint.setTextSize(text_size);
            paint.setColor(text_color);
            canvas.drawText(substring,getBaseline(),getBaseline(),paint);
        }
    }

    @Override
    public boolean isInEditMode() {
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }


}
