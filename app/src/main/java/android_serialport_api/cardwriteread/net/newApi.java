package android_serialport_api.cardwriteread.net;


import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android_serialport_api.cardwriteread.util.HttpsHelp;
import android_serialport_api.cardwriteread.util.MethodUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class newApi {
    private int overtime = 10;


    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    private static class SingletonClassInstance {
        private static final newApi np = new newApi();
    }

    public newApi() {

    }

    public static synchronized newApi getInstance() {
        return SingletonClassInstance.np;
    }


    /**
     * GET
     * @param activity
     * @param url
     * @param h
     */

    public void GET(Activity activity, final String url, HttpCallBack h, boolean dialogType){
        MaterialDialog materialDialog = null;
        if (dialogType){
            materialDialog = MethodUtils.getInstanceSingle().openHttpDialog(0,activity);
        }
        sendGET(url,h,materialDialog);
    }


    private void sendGET(final String url, final HttpCallBack h, final MaterialDialog materialDialog){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(overtime, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(overtime, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(overtime, TimeUnit.SECONDS)//设置写入超时时间
                .sslSocketFactory(HttpsHelp.createSSLSocketFactory())
                .build();
        Request request = new Request.Builder()
                .url(url)
                //.header("token",token)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (materialDialog != null) materialDialog.cancel();
                try {
                    h.Error(call,e);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (materialDialog != null) materialDialog.cancel();
                String res = response.body().string();
                response.body().close();
                //System.out.println("状态码:"+response.code()+" \n  请求地址:"+url+"\n  返回数据:"+res);
                try {
                    h.Success(call,res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * post
     * @param activity
     * @param urls
     * @param h
     * @param dialogType
     */
    public void POST(Activity activity, final String urls, RequestBody formBody, final HttpCallBack h, boolean dialogType){
        MaterialDialog materialDialog = null;
        if (dialogType){
            materialDialog = MethodUtils.getInstanceSingle().openHttpDialog(0,activity);
        }
        sendPOST(urls,formBody,h,materialDialog);
    }
    private void sendPOST(final String url, RequestBody formBody, final HttpCallBack h, final MaterialDialog materialDialog){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(overtime, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(overtime, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(overtime, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        Request request = new Request.Builder()
                .url(Apis.OffLineIp+url)
                .post(formBody)
                .build();
        Call call  = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (materialDialog != null) materialDialog.cancel();
                try {
                    h.Error(call,e);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (materialDialog != null) materialDialog.cancel();
                String res = response.body().string();
                response.body().close();
               // System.out.println("状态码:"+response.code()+" \n  请求地址:"+url+"\n  返回数据:"+res);
                try {
                    h.Success(call,res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
