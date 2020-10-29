package android_serialport_api.cardwriteread.util;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

public class MethodUtils {

    public static class Single{
        public static final MethodUtils cu = new MethodUtils();
    }

    public MethodUtils(){

    }

    public static MethodUtils getInstanceSingle(){
        return Single.cu;
    }

    public MaterialDialog openHttpDialog(int type, Activity activity) {
        MaterialDialog materialDialog = null;
        switch (type) {
            case 0:
                materialDialog = showLoading(activity);
                break;
        }
        return materialDialog;
    }

    //http加载对话框
    private MaterialDialog showLoading(Activity activity) {
        MaterialDialog materDialog = showDialog(activity,"请稍等...");
        if(!activity.isFinishing())
            materDialog.show();
        return materDialog;
    }

    public MaterialDialog showDialog(Activity activity,String s){
        MaterialDialog materDialog = new MaterialDialog.Builder(activity)
                .content(s)
                .progress(true, -1)//等待图标 true=圆形icon false=进度条
                .cancelable(false)//不会被取消 （包括返回键和外部点击都无法取消）
                .build();
        return materDialog;
    }

    public float getDimens(Context context, int id){
        return context.getResources().getDimension(id);
    }
}
