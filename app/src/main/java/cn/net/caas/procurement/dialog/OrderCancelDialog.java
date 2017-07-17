package cn.net.caas.procurement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.net.caas.procurement.R;

/**
 * Created by wjj on 2017/2/23.
 */
public class OrderCancelDialog extends Dialog {
    private EditText et_ordercancel_cause=null;
    private Button btn_ordercancel_cancel=null;
    private Button btn_ordercancel_ok=null;

    private Context context=null;

    private String cancelCause=null;

    public OrderCancelDialog(Context context) {
        super(context);
        this.context=context;
    }

    //自定义回调接口
    public interface DialogCallBackListener{//通过该接口回调Dialog需要传递的值
        void callBack(String msg);//具体方法
    }
    DialogCallBackListener mDialogCallBackListener;
    public void setCallBackListener(DialogCallBackListener mDialogCallBackListener){//设置回调
        this.mDialogCallBackListener = mDialogCallBackListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_cancel);

        et_ordercancel_cause= (EditText) findViewById(R.id.et_ordercancel_cause);
        btn_ordercancel_ok= (Button) findViewById(R.id.btn_ordercancel_ok);
        btn_ordercancel_cancel= (Button) findViewById(R.id.btn_ordercancel_cancel);

        btn_ordercancel_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderCancelDialog.this.dismiss();
            }
        });

        btn_ordercancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cause=et_ordercancel_cause.getText().toString().trim();
                if (cause.equals("")){
                    Toast.makeText(context, "请填写取消订单的原因", Toast.LENGTH_SHORT).show();
                } else{
                    cancelCause=cause;
                    OrderCancelDialog.this.dismiss();
                    if(mDialogCallBackListener != null ){
                        mDialogCallBackListener.callBack(cancelCause);  //触发回调
                    }
                }
            }
        });
    }


}
