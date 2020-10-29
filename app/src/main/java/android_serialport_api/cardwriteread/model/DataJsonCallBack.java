package android_serialport_api.cardwriteread.model;

import org.json.JSONException;
import org.json.JSONObject;

public interface DataJsonCallBack {

    /**
     *
     * @param code 200成功 400失败
     * @param jsonObject
     * @throws JSONException
     */
    void callback(int code, JSONObject jsonObject) throws JSONException;

}
