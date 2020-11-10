package android_serialport_api.cardwriteread.jiedan.net

import android.util.Log
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType


object OK {

    /**
     * 表示所传参数被忽略
     */
    const val OPTIONAL = "optional"

    val TAG = "OK_Result"

    const val MEDIA_TYPE = "application/json; charset=utf-8"

    /**
     * post请求
     */
    inline fun <reified T> post(
        url: String, //URL
        crossinline onSuccess: (data: T) -> Unit, //成功回调
        vararg pairs: Pair<String, String>//参数
    ) {
        Log.d(TAG, "");
        val mapJson = Gson().toJson(HashMap(pairs.toMap()).filterValues { it != OPTIONAL })
        val builder = OkHttpUtils
            .postString()
            .url(url)
            .content(mapJson)
            .mediaType(MediaType.parse(MEDIA_TYPE))

        Log.d(TAG, "");

        builder.build()
            .connTimeOut(6000)
            .readTimeOut(6000)
            .writeTimeOut(6000)
            .execute(object : StringCallback() {

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    Log.d(TAG, "error is ${e?.localizedMessage}");
                    call?.cancel()
                }

                override fun onResponse(response: String?, id: Int) {
                    Log.d(TAG, "url is $url"+"\nmapJson is $mapJson+${response!!}")

                    onSuccess.invoke(Gson().fromJson(response, T::class.java))


                }
            })

    }

    inline fun <reified T> get(url: String, crossinline onSuccess: (data: T) -> Unit, vararg params: Pair<String, String>) {
        val req = OkHttpUtils
            .get()
            .url(url)

        params.forEach {
            req.addParams(it.first, it.second)
        }

        req.build()
            .execute(object : StringCallback() {
                override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                    Log.d(TAG, "error is ${e?.localizedMessage}");
                }

                override fun onResponse(response: String?, id: Int) {
                    onSuccess.invoke(Gson().fromJson(response, T::class.java))
                }
            })
    }

}