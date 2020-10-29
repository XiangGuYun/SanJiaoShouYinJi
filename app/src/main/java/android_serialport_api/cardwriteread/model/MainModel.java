package android_serialport_api.cardwriteread.model;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.xutils.http.RequestParams;

import android_serialport_api.cardwriteread.contract.MainContract;
import android_serialport_api.cardwriteread.net.HttpCallBackV2;
import android_serialport_api.cardwriteread.net.NetWorkUtils;

public class MainModel implements MainContract.Model {

    @Override
    public void pay(Activity activity, RequestParams params, final DataJsonCallBackV2 callBackV2) {
        NetWorkUtils.postHttpRequest(activity, false, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) throws JSONException {
                Log.d("ReadCard", result);
                callBackV2.callback(0,result);
            }

            @Override
            public void onCancelled(String msg) throws JSONException {
                Log.d("ReadCard", msg);
                callBackV2.callback(1,msg);
            }

            @Override
            public void onError(String error) throws JSONException {
                Log.d("ReadCard", error);
                callBackV2.callback(2,error);
            }

            @Override
            public void onFinish() {
                //不需要完成回调
                //callBackV2.callback(3,"");
            }
        });
    }

    @Override
    public void httpGetRequest(Activity activity, RequestParams params, final DataJsonCallBackV2 callBackV2){
        NetWorkUtils.getHttpRequest(activity, true, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) throws JSONException {
                callBackV2.callback(0,result);
            }

            @Override
            public void onCancelled(String msg) throws JSONException {
                callBackV2.callback(1,msg);
            }

            @Override
            public void onError(String error) throws JSONException {
                callBackV2.callback(2,error);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
