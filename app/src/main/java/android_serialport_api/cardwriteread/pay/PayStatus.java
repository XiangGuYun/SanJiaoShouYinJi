package android_serialport_api.cardwriteread.pay;

/**
 * 支付状态
 * 0 二维码支付
 * 1 人脸支付
 * 2 实体卡支付
 * 3 其他支付
 * 4 商户扫码支付
 * -1 无状态
 * 默认无状态 -1
 */
public class PayStatus {
    public static final int PAY_QR_CODE = 0;

    public static final int PAY_FACE = 1;

    public static final int PAY_CARD = 2;

    public static final int PAY_OTHER = 3;

    public static final int PAY_BUSINESS_QR_CODE = 4;

    public static final int NO_STATUS = -1;
}
