package android_serialport_api.cardwriteread.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 字段
 * username
 * password
 * ip
 * branchId+"-FaceSetting"  branchId+"-SolidSetting"=>face:branchId-true(是否开启人脸识别) soild:branchId-true(是否开启固定收银)
 * loginbranchId 上次选择的分店ID
 * loginbranchName 上次选择的分店名称
 */
public class SharedUtil {

    private static class Instantce{
        public static final SharedUtil su = new SharedUtil();
    }

    public SharedUtil(){

    }

    public static SharedUtil getInstatnce(){
        return Instantce.su;
    }

    public SharedPreferences getShared(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedContract.SharedName, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
