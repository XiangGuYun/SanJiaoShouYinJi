package android_serialport_api.cardwriteread.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.cardwriteread.R;

/**
 * @author 86139
 */
public class SoundUtils {
    public static SoundPool soundPool = new SoundPool(15, AudioManager.STREAM_SYSTEM, 5);
    public static Context context;

    public static final int INPUT = 1;
    public static final int PLEASE_PAY = 2;
    public static final int PAYING = 3;
    public static final int PAY_FAILURE = 4;
    public static final int PAY_SUCCESS = 5;
    public static final int NETWORK_ERROR = 6;
    public static final int BEEP = 7;
    public static final int NO_PAY = 8;

    public static void init(Context ctx) {
        context = ctx;
        //1
        soundPool.load(context, R.raw.input, 1);
        //2
        soundPool.load(context, R.raw.pleasepay, 1);
        //3
        soundPool.load(context, R.raw.paying, 1);
        //4
        soundPool.load(context, R.raw.payfail, 1);
        //5
        soundPool.load(context, R.raw.paysuccess, 1);
        //6
        soundPool.load(context, R.raw.networkerror, 1);
        //7
        soundPool.load(context, R.raw.beep, 1);
        //8
        soundPool.load(context, R.raw.nospay, 1);
        //9
        soundPool.load(context, R.raw.spay, 1);
        //10
        soundPool.load(context, R.raw.face_close, 1);
        //11
        soundPool.load(context, R.raw.face_open, 1);
    }

    public static void play(int id) {
        soundPool.autoPause();
        soundPool.play(id, 1, 1, 0, 0, 1);
    }
}
