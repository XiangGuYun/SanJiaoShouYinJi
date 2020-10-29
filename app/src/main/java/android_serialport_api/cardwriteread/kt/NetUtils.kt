package android_serialport_api.cardwriteread.kt

import android.app.Activity
import android_serialport_api.cardwriteread.MyApplication
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils
import android_serialport_api.cardwriteread.net.HttpCallBackV2
import android_serialport_api.cardwriteread.net.NetWorkUtils
import com.google.gson.Gson
import org.json.JSONException
import org.xutils.http.RequestParams

object NetUtils {

    inline fun <reified T> post(activity: Activity?, params: RequestParams?, crossinline onSuccess:(T)->Unit) {
        NetWorkUtils.postHttpRequest(activity, false, params, object : HttpCallBackV2 {
            @Throws(JSONException::class)
            override fun onSuccess(result: String) {
                onSuccess(Gson().fromJson(result, T::class.java))
            }

            @Throws(JSONException::class)
            override fun onCancelled(msg: String) {
            }

            @Throws(JSONException::class)
            override fun onError(error: String) {
                ToastUtils.toast(MyApplication.getAppContext(), error)
            }

            override fun onFinish() {
            }
        })
    }

}