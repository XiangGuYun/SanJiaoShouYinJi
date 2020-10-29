package android_serialport_api.cardwriteread.net;

import org.json.JSONException;

//http回调

/**
 * author:zhx
 * date:2019-12-24
 */
public interface HttpCallBackV2 {
    void onSuccess(String result) throws JSONException;
    void onCancelled(String msg) throws JSONException;
    void onError(String error) throws JSONException;
    void onFinish();
}
