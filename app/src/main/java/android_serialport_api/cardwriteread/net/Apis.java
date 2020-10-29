package android_serialport_api.cardwriteread.net;

/**
 * 接口
 */
public class Apis {
//    public static final String OffLineIp = "https://binguoai.com";
    //"http://192.168.10.27"
    public static String OffLineIp = "";
    public static String GetShangMirOrder = "/api/order/getShangMiOrder/";
    public static String Customer = "/api/customer";
    public static String CreateShangMi = "/api/order/createShangMi";
    public static String GetBanlance = "/zjypg/getBanlance";
    public static String ShangMiInitV2 = "/zjypg/shangmiInitV2";
    public static String ShopList = "/api/branch/list";
    /**
     * getCustomerList
     * getCustomerListNew
     * 这里有俩个接口 俩个接口有区别 第一个接口是返回单个人脸数据 第二个接口是返回所有人脸数据
     */
    public static String GetCustomerList = "/zjypg/getCustomerList"; //获取店下所有用户信息
    public static String GetCustomerListNew = "/zjypg/getCustomerListNew"; //获取店下所有用户信息
    public static String SyncData = "/zjypg/syncData"; //有更新变动的数据
    public static String GetShopConfig = "/zjypg/getShopConfig"; //得到广告图

    public static String getBaiDuSDKSerial = "/api/wxapp/aiyouwei/getBaiduSDKSerial"; //获取百度密钥 授权码
}
