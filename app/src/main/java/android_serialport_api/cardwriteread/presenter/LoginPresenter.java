package android_serialport_api.cardwriteread.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.callback.Callback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.xutils.http.RequestParams;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.baiduface.baidulistener.SdkInitListener;
import android_serialport_api.cardwriteread.baiduface.baidumanage.FaceSDKManager;
import android_serialport_api.cardwriteread.baiduface.baiduutils.ConfigUtils;
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils;
import android_serialport_api.cardwriteread.base.BasePresenter;
import android_serialport_api.cardwriteread.contract.LoginContract;
import android_serialport_api.cardwriteread.jiedan.net.Req;
import android_serialport_api.cardwriteread.model.DataJsonCallBack;
import android_serialport_api.cardwriteread.model.DataJsonCallBackV2;
import android_serialport_api.cardwriteread.model.LoginModel;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.net.bean.LoginRequest;
import android_serialport_api.cardwriteread.net.bean.LoginResponseV2;
import android_serialport_api.cardwriteread.util.MethodUtils;
import android_serialport_api.cardwriteread.util.SharedUtil;
import android_serialport_api.cardwriteread.view.LoginActivity;
import android_serialport_api.cardwriteread.view.SubbranchActivity;


public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private LoginModel loginModel;

    /**
     * 百度人脸
     */
    private Boolean isInitConfig;
    private Boolean isConfigExit;
    /**
     * 密码验证状态 -1未验证 0正在验证 1验证成功
     */
    private int mLoginState = -1;
    /**
     * 百度人脸初始化是否完成 -1未完成 0正在验证 1验证成功
     */
    private int mBaiDuFaceInitState = -1;
    /**
     * 加载框
     */
    private MaterialDialog dialog;

    public LoginPresenter() {
        loginModel = new LoginModel();
    }

    /**
     * 百度授权码获取
     */
    @Override
    public void getBaiDuSqm(final Activity activity) {
        System.out.println("设备:" + android.os.Build.SERIAL);
        loginModel.getBaiDuSDKSerial(activity, android.os.Build.SERIAL, (code, jsonObject) -> {
//            {"code":200,"message":"SUCCESS","data":"DangKou1-face-offline-app"}
            if(code != 200 && jsonObject != null){
                ToastUtils.toast(jsonObject.toString());
            }
            System.out.println("SQM:" + jsonObject);

            if (code == Constant.HTTP_OK) {
                // 将授权码记录到常量key中
                Constant.key = jsonObject.getString("data").trim();
                // 初始化SDK
                initLicense(activity);
            }
        });
    }

    @Override
    public void login(final Activity activity, final String username, final String password) {
        if (mLoginState == 0) {
            return;
        }
        mLoginState = 0;
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            mLoginState = -1;
            mView.toast("密码或账号不能为空", 0);
            return;
        }
        System.out.println("请求地址:" + Apis.OffLineIp + Apis.ShangMiInitV2);
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceId(Build.SERIAL);
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        loginRequest.setDeviceType(13);
        RequestParams params = new RequestParams(Apis.OffLineIp + Apis.ShangMiInitV2);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(loginRequest));
        loginModel.login(activity, params, new DataJsonCallBackV2() {
            @Override
            public void callback(int code, String s) {
                System.out.println("code:" + code + "\nres:" + s);
                if (code == 0) {
                    //登陆成功
                    //保存基本信息
                    LoginResponseV2 loginResponse = JSONObject.parseObject(s, LoginResponseV2.class);
                    Constant.shopId = loginResponse.getData().getShopId();
                    Constant.cashierDeskId = loginResponse.getData().getCashierDeskId();
                    Constant.curUsername = loginRequest.getUsername();
                    Constant.shopName = loginResponse.getData().getShopName();
                    Constant.employeeId = loginResponse.getData().getEmployeeId();
                    //保存信息用于下次自动登陆
                    SharedUtil.getInstatnce().getShared(activity).edit().putString("username", username).putString("password", password).apply();
                    mLoginState = 1;
                    startActivity(activity);
                } else {
                    mLoginState = -1;
                    dialog.cancel();
                    mView.toast("登录失败 "+s, 0);
                }
            }
        });
    }

    private void startActivity(Activity activity) {
        if (mLoginState == 1 && mBaiDuFaceInitState != 1) {
            mView.toast("密码验证成功，等待人脸初始化完成", 0);
        }
        if (mLoginState != 1 && mBaiDuFaceInitState == 1) {
            mView.toast("人脸初始化配置完成,等待登陆", 0);
        }
//        if(mBaiDuFaceInitState != 1){
//            ToastUtils.toast("百度人脸SDK初始化失败");
//            dialog.cancel(); //关闭弹窗
//            return;
//        }
        if (mLoginState == 1 && mBaiDuFaceInitState == 1) {
            dialog.cancel(); //关闭弹窗
            Intent intent = new Intent(activity, SubbranchActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    @Override
    public void autoLogin(Activity activity) {
        //开启加载框
        if (dialog == null) {
            dialog = MethodUtils.getInstanceSingle().openHttpDialog(0, activity);
        }
        String username = SharedUtil.getInstatnce().getShared(activity).getString("username", "");
        String password = SharedUtil.getInstatnce().getShared(activity).getString("password", "");
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            login(activity, username, password);
        } else {
            dialog.cancel();
        }
    }

    /**
     * 百度AI人脸识别
     *  app定时对服务器进行人脸数据的拉取，保存至本地，更新本地数据库信息
     *  测试加入人脸注册功能
     */
    /**
     * 在线授权激活
     *
     * 初始化百度SDK
     */
    @Override
    public void initLicense(final Activity activity) {
        if (mBaiDuFaceInitState == 0) {
            return;
        }
        mBaiDuFaceInitState = 0;
        FaceAuth faceAuth = new FaceAuth();
        //String key = "9RFV-2BFJ-4R6D-MSXP";
        String key = Constant.key;
        if (TextUtils.isEmpty(key)) {
            mView.toast("请输入激活序列号!", 0);
            return;
        }
        //initLicenseBatchLine
        //initLicenseOnLine
        if (!key.contains("DangKou")) {
            //序列号授权
            faceAuth.initLicenseOnLine(activity, key, (code, response) -> {
                if (code == 0) {
                    SQMInitModel(activity);
                }
            });
        } else {
            //应用授权
            faceAuth.initLicenseBatchLine(activity, key, (code, response) -> {
                if (code == 0) {
                    SQMInitModel(activity);
                }
            });
        }
    }

    private void SQMInitModel(final Activity activity){
        FaceSDKManager.getInstance().initModel(activity, new SdkInitListener() {
            @Override
            public void initStart() {

            }

            @Override
            public void initLicenseSuccess() {

            }

            @Override
            public void initLicenseFail(int errorCode, String msg) {
                // 初始化证书失败
                ToastUtils.toast(activity, errorCode + msg);
            }

            @Override
            public void initModelSuccess() {
                // 初始化模型成功时，则初始化百度人脸识别
                initBaiduFace(activity);
            }

            @Override
            public void initModelFail(int errorCode, String msg) {
                // 初始化模型失败
                ToastUtils.toast(activity, errorCode + msg);
            }
        });
    }

    private void initBaiduFace(Activity activity) {
        // todo shangrong 增加配置信息初始化操作
        isConfigExit = ConfigUtils.isConfigExit();
        isInitConfig = ConfigUtils.initConfig();
        if (isInitConfig && isConfigExit) {
            // mView.toast("初始配置加载成功", 0);
            mBaiDuFaceInitState = 1;
            startActivity(activity);
        } else {
            //  mView.toast("初始配置失败,将重置文件内容为默认配置,请重启系统", 0);
            ConfigUtils.modityJson();
        }
    }

}
