package android_serialport_api.cardwriteread.model;

import org.json.JSONException;

public interface DataJsonCallBackV2 {

    /**
     *
     * @param code 0 onSuccess 1 onCancelled 2 onError 3 onFinish
     * @param String
     * @param s
     * @throws JSONException
     */
    void callback(int code, String s) throws JSONException;

}
