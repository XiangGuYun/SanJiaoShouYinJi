package android_serialport_api.cardwriteread.meal;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import android_serialport_api.cardwriteread.R;

/**
 * Created by 20191024 on 2020/1/17.
 */

public class SoundPlayUtils {
    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(15,
            AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }

        // 初始化声音
        mContext = context;

        mSoundPlayer.load(mContext, R.raw.input, 1);// 1
        mSoundPlayer.load(mContext, R.raw.pleasepay, 1);// 2
        mSoundPlayer.load(mContext, R.raw.paying, 1);// 3
        mSoundPlayer.load(mContext, R.raw.payfail, 1);// 4
        mSoundPlayer.load(mContext, R.raw.paysuccess, 1);// 5
        mSoundPlayer.load(mContext, R.raw.networkerror, 1);// 6
        mSoundPlayer.load(mContext, R.raw.beep, 1);// 7
        mSoundPlayer.load(mContext, R.raw.nospay, 1);// 8
        mSoundPlayer.load(mContext, R.raw.spay, 1);// 9
        mSoundPlayer.load(mContext, R.raw.face_close, 1);// 10
        mSoundPlayer.load(mContext, R.raw.face_open, 1);// 11

        return soundPlayUtils;
    }

    public static final int input =  1;
    public static final int please_pay =  2;
    public static final int paying =  3;
    public static final int pay_fail =  4;
    public static final int pay_success =  5;
    public static final int network_error =  6;
    public static final int beep =  7;
    public static final int no_pay =  8;
    public static final int spay =  9;
    public static final int face_close =  10;
    public static final int face_open =  11;

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.autoPause();
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

}
