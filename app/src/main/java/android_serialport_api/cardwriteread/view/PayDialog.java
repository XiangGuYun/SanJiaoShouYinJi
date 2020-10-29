package android_serialport_api.cardwriteread.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.util.MethodUtils;


/**
 * 弃用
 */
@Deprecated
public class PayDialog extends Dialog {
    private Activity activity;
    private TextView paydialogMoney;
    private Button paydialogCancel;
    private String str = "加载中...";
    private CancelClick cancelClick;
    public interface CancelClick{
        void cancel();
    }

    public PayDialog(@NonNull Activity activity, String str, CancelClick cancelClick) {
        super(activity, R.style.MyDialog);
        this.activity = activity;
        this.str = str;
        this.cancelClick = cancelClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paydialog);
        initView();

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.height = (int) MethodUtils.getInstanceSingle().getDimens(activity, R.dimen.dp_160);
        params.width = (int) MethodUtils.getInstanceSingle().getDimens(activity, R.dimen.dp_200);
        this.getWindow().setAttributes(params);
    }

    private void initView() {
        paydialogMoney = (TextView) findViewById(R.id.paydialog_money);
        paydialogCancel = (Button) findViewById(R.id.paydialog_cancel);
        paydialogMoney.setText("应付:"+str);
        paydialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClick.cancel();
            }
        });
    }

}
