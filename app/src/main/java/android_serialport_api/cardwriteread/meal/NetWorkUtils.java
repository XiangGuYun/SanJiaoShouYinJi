package android_serialport_api.cardwriteread.meal;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
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
import android_serialport_api.cardwriteread.net.HttpCallBackV2;

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
     * @param params   请求参数 get请求使用 addQueryStringParameter方法添加参数
     * @param callback 回调对象
     */
    public static Callback.Cancelable getHttpRequest(boolean isShowDialog, RequestParams params, final HttpCallBackV2 callback) {
        return sendHttpRequest(isShowDialog, HttpMethod.GET, params, callback);
    }

    /**
     * http post请求
     *
     * @param params   请求参数 post请求使用 addBodyParameter方法添加参数
     * @param callback 回调对象
     */
    public static Callback.Cancelable postHttpRequest(boolean isShowDialog, RequestParams params, final HttpCallBackV2 callback) {
        return sendHttpRequest(isShowDialog, HttpMethod.POST, params, callback);
    }


    public static Callback.Cancelable sendHttpRequest(final boolean isShowDialog, HttpMethod method, RequestParams params, final HttpCallBackV2 callback) {
        Log.i("请求参数---",params.getBodyContent());
        if (params == null) {
            params = new RequestParams();
        }

        params.setCacheMaxAge(1000 * 0); //为请求添加缓存时间
        params.setConnectTimeout(DEFAULT_CACHE_EXPIRY_TIME);
        params.setAsJsonContent(true);
        final String url = params.getUri();
//        LogUtil.d("sendRequest: url = " + url);
        Log.i("请求url---",url);
        SSLContext sslContext = getSSLContext(MyApplication.getApplication());
        if (null == sslContext) {
            if (BuildConfig.DEBUG) Log.i(TAG, "Error:Can't Get SSLContext!");
            return null;
        }

        params.setSslSocketFactory(sslContext.getSocketFactory());

        if (isShowDialog == true){
//            SDDialogManager.showProgressDialog("请稍候...");
        }
        return x.http().request(method, params, new Callback.CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException msg) {
                if (isShowDialog == true)
//                SDDialogManager.dismissProgressDialog();
                {
                    try {
                        callback.onCancelled(msg.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                if (isShowDialog == true)
//                SDDialogManager.dismissProgressDialog();
                {
                    try {
                        callback.onError(arg0.getMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSuccess(String result) {
                Log.i("请求返回---",result);
                if (isShowDialog == true)
//                SDDialogManager.dismissProgressDialog();
                if (result == null) {
                    return;
                }
//                BaseActModel baseActModel = JSONObject.parseObject(result,BaseActModel.class);
//                if (baseActModel.getCode() != 200){
//                    SDToast.showToast(baseActModel.getMessage());
//                }
                try {
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinished() {
                if (isShowDialog == true)
//                SDDialogManager.dismissProgressDialog();
                LogUtil.d("onFinished");
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
//        CertificateFactory certificateFactory = null;
//        InputStream inputStream = null;
//        Certificate cer = null;
//        KeyStore keystore = null;
//        TrustManagerFactory trustManagerFactory = null;
        try {
//            certificateFactory = CertificateFactory.getInstance("X.509");
//            inputStream = context.getAssets().open("baidu.cer");//这里导入SSL证书文件
//            try {
//                cer = certificateFactory.generateCertificate(inputStream);
//                Log.i(TAG, cer.getPublicKey().toString());
//            } finally {
//                inputStream.close();
//            }

            //创建一个证书库，并将证书导入证书库
//            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keystore.load(null, null); //双向验证时使用
//            keystore.setCertificateEntry("trust", cer);
//
//            // 实例化信任库
//            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(keystore);

            mSSLContext = SSLContext.getInstance("TLS");
//            mSSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());



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

