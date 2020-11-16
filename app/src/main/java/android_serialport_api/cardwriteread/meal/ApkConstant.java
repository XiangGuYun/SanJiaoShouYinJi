package android_serialport_api.cardwriteread.meal;

import android.text.TextUtils;

import android_serialport_api.cardwriteread.MyApplication;
import android_serialport_api.cardwriteread.net.Apis;

import com.yp.baselib.utils.SPUtils;


/**
 * Created by 20191024 on 2020/1/6.
 */

public class ApkConstant {
    public static final String OFFLINE_IP = "http://123.206.224.209:9010";
//    public static final String ONLINE_IP = "https://binguoai.com";
    public static  String ONLINE_IP = Apis.OffLineIp;//getIpAddress();
    public static  String GETSHANGMIORDER = ONLINE_IP+"/api/order/getShangMiOrder/";
    public static String CUSTOMER = ONLINE_IP+"/api/customer";
    public static String CREATESHANGMI = ONLINE_IP+"/api/order/createShangMi";
    public static String GETBANLANCE = ONLINE_IP+"/zjypg/getBanlance";
    public static String SHANGMIINITV2 = ONLINE_IP+"/zjypg/shangmiInitV2";
    public static String SYNCDATA = ONLINE_IP+"/zjypg/syncData";
    public static String ISQLQUERY = ONLINE_IP+"/api/wxapp/openapi/iSqlquery";
    public static String CREATE = ONLINE_IP+"/api/order/create";
    /** 获取门店配置 */
    public static String GETSHOPCONFIG = ONLINE_IP+"/api/shop/getShopConfig";


    public static String getIpAddress(){
        String ip = SPUtils.INSTANCE.get(MyApplication.getAppContext(), "IpAddress","", "default").toString();
        if (TextUtils.isEmpty(ip)){
            return "https://binguoai.com";
        }else{
            return ip;
        }
    }
}
