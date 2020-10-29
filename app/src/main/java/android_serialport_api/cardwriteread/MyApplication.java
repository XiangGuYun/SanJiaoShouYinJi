package android_serialport_api.cardwriteread;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.yp.baselib.helper.ExceptionHelper;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;
import android_serialport_api.cardwriteread.baiduface.baidumanage.FaceSDKManager;
import android_serialport_api.cardwriteread.model.db.AppDBManager;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.util.HttpsHelp;
import android_serialport_api.cardwriteread.util.SharedUtil;
import android_serialport_api.cardwriteread.util.SoundUtils;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    private static MyApplication mApp = null;
    private static SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private static SerialPort mSerialPort = null;
    private static AppDBManager appDBManager;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initOKHttp();
        CrashReport.initCrashReport(getApplicationContext(), "5a8ae9060a", false);
    }

    public static Context getAppContext(){
        return context;
    }

    private void init(){
        mApp = this;
        context = this.getApplicationContext();
        x.Ext.init(this);
        //初始化本地百度人脸数据库
        FaceSDKManager.getInstance().initDataBases(mApp);
        //初始化本地订单数据库
        if (appDBManager == null){
            appDBManager = new AppDBManager(mApp,"appdbmanage");
        }
        if (SharedUtil.getInstatnce().getShared(mApp).getString("ip","").equals("")){
            SharedUtil.getInstatnce().getShared(mApp).edit().putString("ip","https://binguoai.com").commit();
            Apis.OffLineIp = "https://binguoai.com";
        }else{
            Apis.OffLineIp = SharedUtil.getInstatnce().getShared(mApp).getString("ip","");
        }
        //初始化语音
        SoundUtils.init(mApp);
    }

    public static AppDBManager getAppDBManager() {
        return appDBManager;
    }

    public static SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            String path = "/dev/ttyS1"; // /dev/ttyS1
            int baudrate = 115200; //115200

            /* Check parameters */
            if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public static void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    public static MyApplication getApplication(){
        return mApp;
    }

    private void initOKHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(HttpsHelp.createSSLSocketFactory())
                .hostnameVerifier((hostname, session) -> true)
                .retryOnConnectionFailure(true)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


}
