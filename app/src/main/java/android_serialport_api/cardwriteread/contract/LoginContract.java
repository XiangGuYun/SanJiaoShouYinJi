package android_serialport_api.cardwriteread.contract;

import android.app.Activity;

import org.xutils.http.RequestParams;

import android_serialport_api.cardwriteread.base.BaseView;
import android_serialport_api.cardwriteread.model.DataJsonCallBack;
import android_serialport_api.cardwriteread.model.DataJsonCallBackV2;

/**
 * @author 86139
 */
public class LoginContract {
    public interface Model {
        /**
         * 登录
         * @param activity
         * @param params
         * @param callBack
         */
        void login(Activity activity, RequestParams params, DataJsonCallBackV2 callBack);

        /**
         * 获取百度SDK序列号
         * @param activity
         * @param deviceId
         * @param callBackV2
         */
        void getBaiDuSDKSerial(Activity activity, String deviceId, DataJsonCallBack callBackV2);
    }

    public interface View extends BaseView {

    }

    public interface Presenter {
        /**
         * 登录
         * @param activity
         * @param username
         * @param password
         */
        void login(Activity activity, String username, String password);

        /**
         * 自动登录
         * @param activity
         */
        void autoLogin(Activity activity);

        /**
         * 初始化SDK
         * @param activity
         */
        void initLicense(Activity activity);

        /**
         * 获取百度授权码
         * @param activity
         */
        void getBaiDuSqm(Activity activity);
    }
}
