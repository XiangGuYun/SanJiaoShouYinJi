package android_serialport_api.cardwriteread.contract;

import android.app.Activity;

import org.json.JSONObject;

import java.util.List;

import android_serialport_api.cardwriteread.base.BaseView;
import android_serialport_api.cardwriteread.model.DataJsonCallBack;


public class SubbranchContract {
    public interface Model {
        void query(Activity activity, String shopid, DataJsonCallBack callBack);
    }

    public interface View extends BaseView {
        void ReshView(List<JSONObject> list);
    }

    public interface Presenter {
        void query(Activity activity);
    }
}
