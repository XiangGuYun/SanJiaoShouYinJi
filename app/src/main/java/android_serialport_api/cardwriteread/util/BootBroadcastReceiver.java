package android_serialport_api.cardwriteread.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android_serialport_api.cardwriteread.view.LoginActivity;
import android_serialport_api.cardwriteread.view.MainActivity;

/**
 * 类名：BootBroadcastReceiver
 * 功能描述：启动时系统发出的广播的接收器
 * #<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 * @author android_ls
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent(context, LoginActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
    }
}