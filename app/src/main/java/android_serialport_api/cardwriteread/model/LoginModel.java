package android_serialport_api.cardwriteread.model;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.IOException;

import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils;
import android_serialport_api.cardwriteread.contract.LoginContract;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.net.HttpCallBack;
import android_serialport_api.cardwriteread.net.HttpCallBackV2;
import android_serialport_api.cardwriteread.net.NetWorkUtils;
import android_serialport_api.cardwriteread.net.newApi;
import okhttp3.Call;


/**
 * @author 86139
 */
public class LoginModel implements LoginContract.Model {
    @Override
    public void login(Activity activity, RequestParams params, final DataJsonCallBackV2 callBack) {
        NetWorkUtils.postHttpRequest(activity, false, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) throws JSONException {
                callBack.callback(0,result);
            }

            @Override
            public void onCancelled(String msg) throws JSONException {
                callBack.callback(1,msg);
            }

            @Override
            public void onError(String error) throws JSONException {
                Log.d("Test", error);
                ToastUtils.toast(error);
                callBack.callback(2,error);
            }

            @Override
            public void onFinish() {
                //callBack.callback(3,"");
            }
        });
    }

    @Override
    public void getBaiDuSDKSerial(Activity activity, String deviceId, final DataJsonCallBack callBack) {
        String url = Apis.OffLineIp + Apis.getBaiDuSDKSerial+"?deviceId="+deviceId;
        newApi.getInstance().GET(activity, url, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) throws JSONException {
                callBack.callback(-1,null);
            }

            @Override
            public void Success(Call call, String res) throws IOException, JSONException {
                callBack.callback(new JSONObject(res).getInt("code"),new JSONObject(res));
            }
        },true);
    }
}
