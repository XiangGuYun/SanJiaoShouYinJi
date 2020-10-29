package android_serialport_api.cardwriteread.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import android_serialport_api.cardwriteread.view.LoginActivity;

public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }

    /**
     * 执行命令
     */
    public static String execCmd(String cmd) {
        System.out.println(cmd);
        Process exeEcho;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            exeEcho = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    exeEcho.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            System.out.println(stringBuffer.toString());
            exeEcho.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }


    public static boolean installApp(String apkPath,Context context) {
        if (apkPath == null || apkPath.length() < 4) {
            return false;
        }
        String type = apkPath.substring(apkPath.length() - 4, apkPath.length());
        if (!type.equals(".apk")) {
            return false;
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            return false;
        }
        System.out.println("begin install apkPath============== " + apkPath);
        //三角形的直接先走intent意图安卓
        installApk(context,apkPath);
        //boolean isSuccess = install(apkPath);
        //System.out.println("install result============== " + isSuccess);
        return true;
    }

    /**
     * intent意图安卓app
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            System.out.println("7.0以上，正在安装apk...");
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "android_serialport_api.cardwriteread.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            System.out.println("7.0以下，正在安装apk...");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 执行静默安装逻辑
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public static boolean install(String apkPath) {
        boolean result = false;
//        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
//            Process process = new ProcessBuilder()
//                    .command("/system/xbin/su")
//                    .redirectErrorStream(true).start();

//            OutputStream out = mProcess.getOutputStream();
            // 申请su权限
//            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            //String command = "pm install -i com.yp.payment –-user 0" + apkPath + "\n";
            String command = "pm install -i android_serialport_api.cardwriteread –user 0 "+apkPath;
//            String command = "pm install -r " + apkPath + "\n";
            Process process = Runtime.getRuntime().exec(command);
//            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
//            dataOutputStream.flush();
//            dataOutputStream.writeBytes("exit\n");
//            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "===========install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", "=========" + e.getMessage(), e);
        } finally {
            try {
//                if (dataOutputStream != null) {
//                    dataOutputStream.close();
//                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }
}
