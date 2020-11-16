package android_serialport_api.cardwriteread.net;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android_serialport_api.cardwriteread.MyApplication;
import android_serialport_api.cardwriteread.util.MethodUtils;

/**
 * Created by 20191024 on 2019/10/28.
 */

public class NetWorkUtils {
    private static SSLContext mSSLContext = null;
    private static final String TAG = "NetWorkUtils";
    public static int DEFAULT_CACHE_EXPIRY_TIME = 30 * 1000;

    /**
     * http get请求
     *
     * @param callback 回调对象
     */
    public static Callback.Cancelable getHttpRequest(Activity activity, boolean isShowDialog, RequestParams params, final HttpCallBackV2 callback) {
        MaterialDialog dialog = null;
        if (isShowDialog) {
            dialog = MethodUtils.getInstanceSingle().openHttpDialog(0, activity);
        }
        return sendHttpRequest(dialog, HttpMethod.GET, params, callback);
    }


    /**
     * http post请求
     *
     * @param params   请求参数 post请求使用 addBodyParameter方法添加参数
     * @param callback 回调对象
     */
    public static Callback.Cancelable postHttpRequest(Activity activity, boolean isShowDialog, RequestParams params, final HttpCallBackV2 callback) {
        MaterialDialog dialog = null;
        if (isShowDialog) {
            dialog = MethodUtils.getInstanceSingle().openHttpDialog(0, activity);
        }
        return sendHttpRequest(dialog, HttpMethod.POST, params, callback);
    }

    //静默请求
    public static Callback.Cancelable postHttpRequest(boolean isShowDialog, RequestParams params, final HttpCallBackV2 callback) {
        return sendHttpRequest(null, HttpMethod.POST, params, callback);
    }


    public static Callback.Cancelable sendHttpRequest(final MaterialDialog dialog, HttpMethod method, RequestParams params, final HttpCallBackV2 callback) {
        if (params == null) {
            params = new RequestParams();
        }

        params.setCacheMaxAge(1000 * 0); //为请求添加缓存时间
        params.setConnectTimeout(DEFAULT_CACHE_EXPIRY_TIME);
        params.setAsJsonContent(true);
        final String url = params.getUri();
        SSLContext sslContext = getSSLContext(MyApplication.getApplication());
        if (null == sslContext) {
            if (BuildConfig.DEBUG) Log.i(TAG, "Error:Can't Get SSLContext!");
            return null;
        }

        params.setSslSocketFactory(sslContext.getSocketFactory());

        RequestParams finalParams = params;
        return x.http().request(method, params, new Callback.CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException msg) {
                if (dialog != null) dialog.cancel();
                try {
                    callback.onCancelled(msg.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.d("TestNet", finalParams.getUri()+"\n"+finalParams.getBodyContent()+"\n"+arg0.getLocalizedMessage());
                if (dialog != null) dialog.cancel();
                try {
                    callback.onError(arg0.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String result) {
                if(!finalParams.getUri().contains("syncData"))
                    Log.e("TestNet", finalParams.getUri()+"\n"+finalParams.getBodyContent()+"\n"+result);
                if (dialog != null) dialog.cancel();
                ;
                if (result == null) {
                    return;
                }
                try {
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinished() {
                if (dialog != null) dialog.cancel();
                callback.onFinish();
            }
        });
    }

    /**
     * 获取Https的证书
     *
     * @param context 上下文
     * @return SSL的上下文对象
     */
    private static SSLContext getSSLContext(Context context) {
        try {

            mSSLContext = SSLContext.getInstance("TLS");

            //信任所有证书 （官方不推荐使用）
            mSSLContext.init(null, new TrustManager[]{new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {

                }
            }}, new SecureRandom());

            return mSSLContext;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

