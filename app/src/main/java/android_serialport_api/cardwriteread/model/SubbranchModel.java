package android_serialport_api.cardwriteread.model;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import android_serialport_api.cardwriteread.contract.SubbranchContract;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.net.HttpCallBack;
import android_serialport_api.cardwriteread.net.newApi;
import okhttp3.Call;

public class SubbranchModel implements SubbranchContract.Model {
    @Override
    public void query(Activity activity, String shopid, final DataJsonCallBack callBack) {
        newApi.getInstance().GET(activity, Apis.OffLineIp+Apis.ShopList + "/" + shopid, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) throws JSONException {

            }

            @Override
            public void Success(Call call, String res) throws IOException, JSONException {
                callBack.callback(0, new JSONObject(res));
            }
        }, false);
    }

}
