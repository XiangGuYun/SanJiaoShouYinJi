package android_serialport_api.cardwriteread.net;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;

//http回调

/**
 * author:zhx
 * date:2019-12-24
 */
public interface HttpCallBack {
    void Error(Call call, IOException e) throws JSONException;
    //回调返回内容
    void Success(Call call, String res) throws IOException, JSONException;
}
