package android_serialport_api.cardwriteread.pay;

import android.widget.Toast;

import java.math.BigDecimal;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.MyApplication;
import android_serialport_api.cardwriteread.net.bean.ShangMiOrderRequest;
import android_serialport_api.cardwriteread.util.ScanGunKeyEventHolder;

/**
 * 二维码支付帮助类
 */
public class PayQrCodeHelper {

    private static PayQrCodeHelper helper;
    private OnGetQrCodeModelListener getQrCodeModelListener;
    private GetScanCondition getScanCondition;

    /**
     * 二维码扫描工具类
     */
    private ScanGunKeyEventHolder scanGunKeyEventHolder;

    private PayQrCodeHelper(){

    }

    public static PayQrCodeHelper getInstance(){
        if(helper == null){
            helper = new PayQrCodeHelper();
        }
        return helper;
    }

    /**
     * 初始化二维码扫描
     *
     * @param moneyForPay 待支付的金额数量
     * @param getScanCondition 可以执行二维码扫描的条件，例如当处于支付状态时不应该进行扫描
     */
    public void initSearchQrCode(float moneyForPay, OnGetQrCodeModelListener listener, GetScanCondition getScanCondition) {
        try {
            getQrCodeModelListener = listener;
            scanGunKeyEventHolder = new ScanGunKeyEventHolder();
            scanGunKeyEventHolder.setReadSuccessListener(barcode -> {
                if (getScanCondition.getCondition()) {
                    ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
                    shangMiOrderRequest.setAuthCode(barcode);
                    shangMiOrderRequest.setPayType(4);
                    shangMiOrderRequest.setShopID(Constant.shopId);
                    shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);
                    BigDecimal priceIncent = new BigDecimal(String.valueOf(moneyForPay)).multiply(new BigDecimal(100));
                    shangMiOrderRequest.setAccountBalance(priceIncent.longValue());
                    getQrCodeModelListener.onGetModel(shangMiOrderRequest);
//                    isPayFinished = false;
//                    PayModel = 4;
//                    doPay(shangMiOrderRequest);
//                    mView.uploadPayDialogText("支付中...");
                }
            });
        } catch (Exception e) {
            Toast.makeText(MyApplication.getAppContext(), "扫描错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 已经获取到了扫描到的二维码并封装成了模型类
     */
    public interface OnGetQrCodeModelListener{
        void onGetModel(ShangMiOrderRequest request);
    }

    public interface GetScanCondition{
        boolean getCondition();
    }


}
