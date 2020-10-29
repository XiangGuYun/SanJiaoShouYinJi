package android_serialport_api.cardwriteread;

import java.util.List;

import android_serialport_api.cardwriteread.meal.AllCaiModel;

/**
 * @author 86139
 */
public class Constant {
    /**
     * 总店id
     */
    public static Integer shopId = null; 
    /**
     * 分店id
     */
    public static Integer branchId = null; 
    /**
     * 设备id
     */
    public static Integer cashierDeskId = null;
    /**
     * 用户账号
     */
    public static String curUsername = null; 
    /**
     * 商铺名称
     */
    public static String shopName = null;

    public static String employeeId = null;
    /**
     * 百度授权码
     */
    public static String key = ""; 

    public static final int HTTP_OK = 200;
    
    public static final String PWD_ENTER_FACE_MANAGER = "6666.60";

    public static final String FACE_SETTING = "-FaceSetting";

    public static final String FIXED_SETTING = "-SolidSetting";

    public static final int MULTI_SCREENS = 2;

    public static final String RESPECT = "尊敬的";

    public static final String FACE_SIMILAR = "FACE_SIMILAR";

    public static final String CODE = "code";

    public static final String DATA = "data";

    public static final String MESSAGE = "massage";

    public static final String NAME = "姓名:";

    public static final String WAITING_PAY = "等待支付";

    public static List<AllCaiModel.DataBean.ResultsBean> listDishOrder = null;


    public static boolean staticPay;
    public static boolean startPay;
}
