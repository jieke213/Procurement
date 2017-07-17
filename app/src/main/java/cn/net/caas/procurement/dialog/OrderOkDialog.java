package cn.net.caas.procurement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import cn.net.caas.procurement.R;
import cn.net.caas.procurement.constant.Constants;

/**
 * Created by wjj on 2017/3/24.
 */
public class OrderOkDialog extends Dialog {
    private Context context;

    private Button btn_review_ok_dialog;
    private Button btn_review_cancel_dialog;
    private Spinner sp_teamName_dialog;
    private EditText et_projFeeFrom_dialog;

    private SharedPreferences sp;
    private String access_token;


    public OrderOkDialog(Context context) {
        super(context);
        this.context=context;


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int windowWidth = outMetrics.widthPixels;
        int windowHeight = outMetrics.heightPixels;

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (windowWidth * 0.8); // 宽度设置为屏幕的一定比例大小
        params.height = (int) (windowHeight * 0.5); // 高度设置为屏幕的一定比例大小

        getWindow().setAttributes(params);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_ok);

        btn_review_ok_dialog= (Button) findViewById(R.id.btn_review_ok_dialog);
        btn_review_cancel_dialog= (Button) findViewById(R.id.btn_review_cancel_dialog);
        sp_teamName_dialog= (Spinner) findViewById(R.id.sp_teamName_dialog);
        et_projFeeFrom_dialog= (EditText) findViewById(R.id.et_projFeeFrom_dialog);

        //从本地获取access_token
        sp=context.getSharedPreferences(Constants.LOGIN_INFO,context.MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");


        btn_review_ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderOkDialog.this.dismiss();
                if(mDialogCallBackListener != null ){
                    mDialogCallBackListener.callBack();  //触发回调
                }
            }
        });

        btn_review_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderOkDialog.this.dismiss();
            }
        });
    }


    //自定义回调接口
    public interface DialogCallBackListener{//通过该接口回调Dialog需要传递的值
        void callBack();//具体方法
    }
    DialogCallBackListener mDialogCallBackListener;
    public void setCallBackListener(DialogCallBackListener mDialogCallBackListener){//设置回调
        this.mDialogCallBackListener = mDialogCallBackListener;
    }
}
