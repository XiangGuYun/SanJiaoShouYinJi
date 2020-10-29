package android_serialport_api.cardwriteread.contract;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

import android_serialport_api.cardwriteread.baiduface.baiducamera.AutoTexturePreviewView;
import android_serialport_api.cardwriteread.base.BaseView;
import android_serialport_api.cardwriteread.model.DataJsonCallBackV2;
import android_serialport_api.cardwriteread.net.bean.ShangMiOrderRequest;
import android_serialport_api.cardwriteread.view.MainActivity;


/**
 * @author 86139
 */
public class MainContract {
    public interface Model {
        /**
         * 支付网络接口
         * @param activity
         * @param params
         * @param callBackV2
         */
        void pay(Activity activity, RequestParams params, DataJsonCallBackV2 callBackV2);

        /**
         * get请求 统计订单专用
         * @param activity
         * @param params
         * @param callBackV2
         */
        void httpGetRequest(Activity activity, RequestParams params, DataJsonCallBackV2 callBackV2);
    }

    public interface View extends BaseView {
        /**
         * 发起支付
         */
        void pay(boolean isMeal);

        /**
         * 初始化订单
         */
        void initOrderData();

        /**
         * 返回activity对象
         * @return
         */
        Activity getActivity();

        /**
         * 打卡支付弹窗
         */
        void openPayDialog(boolean isMeal);

        /**
         * 关闭支付弹窗
         */
        void closePayDialog(boolean isMeal);

        /**
         * 更新弹窗内容
         * @param s
         */
        void uploadPayDialogText(String s);

        /**
         * 展示统计订单数据弹窗
         * @param list
         */
        void showStatisticalOrderDataDialog(List<com.alibaba.fastjson.JSONObject> list);
    }

    public interface Presenter {
        /**
         * 支付接口 人脸支付 刷卡支付 扫码支付都走这里
         * 弹窗的处理这里
         * @param request
         */
        void doPay(ShangMiOrderRequest request);

        /**
         * 初始化二维码扫描
         */
        void initSearchQrCode();

        /**
         * 更新支付状态
         * @param b
         */
        void uploadPayState(boolean b);

        /**
         * 更改设置是否固定收银
         */
        void uploadFixedPayState();

        /**
         * 初始化副屏
         */
        void initViceScreen();

        /**
         * 初始化相机
         * @param activity
         * @param m
         */
        void initCamera(Activity activity, AutoTexturePreviewView m);

        /**
         * 更新副屏显示内容
         */
        void updateViceScreenDisplay();

        /**
         * 改变后台更新状态
         * @param is
         */
        void enableBackgroundUpdate(boolean is);

        /**
         * 统计订单数据
         */
        void statisticalOrderData();

        /**
         * 处理读取到的卡号
         * @param cardNumber
         */
        void doCardNumber(String cardNumber);
    }
}
