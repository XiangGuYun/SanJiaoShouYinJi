package android_serialport_api.cardwriteread.pay;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.xutils.http.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import android_serialport_api.SerialPort;
import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.MyApplication;
import android_serialport_api.cardwriteread.model.DataJsonCallBackV2;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.net.HttpCallBackV2;
import android_serialport_api.cardwriteread.net.NetWorkUtils;
import android_serialport_api.cardwriteread.net.bean.GetBalanceRequest;
import android_serialport_api.cardwriteread.net.bean.GetBalanceResponse;
import android_serialport_api.cardwriteread.net.bean.ShangMiOrderRequest;
import android_serialport_api.cardwriteread.util.CardReadHelp;

import static android_serialport_api.cardwriteread.util.CommonUtils.namePartReplaceStar;

/**
 * 刷卡支付帮助类
 */
public class PayCardHelper {

    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private OnReadCardNumberListener listener;
    private OnReadCardBalanceListener readCardBalanceListener;
    private OnGetCardPayModelListener getCardPayModelListener;

    private PayCardHelper(){

    }

    private static PayCardHelper helper;

    /**************************************************************************
     公有方法
     **************************************************************************/
    public static PayCardHelper getInstance(){
        if(helper == null){
            helper = new PayCardHelper();
        }
        return helper;
    }

    /**
     * 刷卡支付
     * 初始化卡片
     * 开启线程
     */
    public void initCardReader(OnReadCardNumberListener listener){
        try {
            mSerialPort = MyApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            this.listener = listener;

        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                while (true) {
                    setHex();
                    byte[] buffer_tmp = new byte[64];
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    mInputStream.read(buffer);
                    parase_pkg(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 查询余额
     * @param activity
     * @param cardNumber
     * @param readCardBalanceListener
     */
    public void queryBalance(Activity activity, String cardNumber, OnReadCardBalanceListener listener){
        try {
            if (TextUtils.isEmpty(cardNumber)) {
                return;
            }
            this.readCardBalanceListener = listener;
            GetBalanceRequest getBalanceRequest = new GetBalanceRequest();
            getBalanceRequest.setCardNo(cardNumber.toUpperCase());
            getBalanceRequest.setShopId(Constant.shopId);
            getBalanceRequest.setDeviceId(Build.SERIAL);
            RequestParams params = new RequestParams(Apis.OffLineIp + Apis.GetBanlance);
            params.setAsJsonContent(true);
            params.setBodyContent(new Gson().toJson(getBalanceRequest));
            pay(activity, params, (code, s) -> {
                //{"code":200,"pageCount":0,"pageSize":100,"message":"SUCCESS","data":{"username":"邹焕鑫","balance":"199999878300"}}
                int sta = JSONObject.parseObject(s).getInteger("code");
                String details = "";
                try {
                    if (code == 0 && sta == 200) {
                        //读取卡片余额成功
                        GetBalanceResponse getBalanceResponse = JSONObject.parseObject(s, GetBalanceResponse.class);
                        String str = String.valueOf((Integer.parseInt(getBalanceResponse.getData().getBalance()) * 0.01));
                        details = "姓名:" + namePartReplaceStar(getBalanceResponse.getData().getUsername()) + "\n余额:" + str;
                    }
                    readCardBalanceListener.onReadCardBalance(details);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取刷卡支付所需上传的模型类
     * @param cardNumber
     * @param moneyForPay
     * @param listener
     */
    public void getCardPayModel(String cardNumber, float moneyForPay, OnGetCardPayModelListener listener){
        try {
            if (TextUtils.isEmpty(cardNumber)) {
                return;
            }
            this.getCardPayModelListener = listener;
            listener.beforeGet();
            ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
            shangMiOrderRequest.setCardNo(cardNumber);
            shangMiOrderRequest.setPayType(PayStatus.PAY_CARD);
            shangMiOrderRequest.setShopID(Constant.shopId);
            shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);
            BigDecimal priceInCent = new BigDecimal(String.valueOf(moneyForPay)).multiply(new BigDecimal(100));
            shangMiOrderRequest.setAccountBalance(priceInCent.longValue());
            listener.afterGet(shangMiOrderRequest);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    /**************************************************************************
     私有方法
     **************************************************************************/

    /**
     * 设置读取卡片的HEX
     */
    private void setHex() {
        //设置密钥A
        //0块区默认为0
        byte[] buf = new byte[16];
        byte len = 8;
        byte[] key_buf = CardReadHelp.getInstance().parase_text("FF FF FF FF FF FF");
        buf[0] = (byte) ("0".charAt(0) - 0x30);//block 4
        buf[1] = 1;//is_a = true
        System.arraycopy(key_buf, 0, buf, 2, key_buf.length);
        byte COM_PKT_CMD_TYPEA_MF1_READ = 0x22;
        make_packet(COM_PKT_CMD_TYPEA_MF1_READ, buf, len);
    }

    /**
     * 设置HEX读取
     * @param cmd
     * @param buf
     * @param len
     */
    private void make_packet(byte cmd, byte[] buf, byte len) {
        byte sum = 0;
        byte[] send_buff = new byte[len + 4];
        sum += 0x68;
        //sendc(HEAD);
        send_buff[0] = 0x68;

        try {
            mOutputStream.write(0x68);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sum += len + 4;
        //sendc(len + 4);
        send_buff[1] = (byte) (len + 4);
        try {
            mOutputStream.write(len + 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sum += cmd;
        try {
            mOutputStream.write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send_buff[2] = cmd;
        for (int i = 0; i < len; i++) {
            sum += buf[i];
            send_buff[3 + i] = buf[i];
            try {
                mOutputStream.write(buf[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        send_buff[len + 3] = sum;
        try {
            mOutputStream.write(sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parase_pkg(byte[] buffer) {
        if ((buffer[0] == 0x68) && (buffer[1] > 0) && (buffer[1] < 64)) {
            byte len = buffer[1];
            byte sum = CardReadHelp.getInstance().check_sum(buffer, len - 1);
            if (sum == buffer[len - 1])
                parase_cmd(buffer);
        }
    }

    private void parase_cmd(byte[] buffer) {
        byte cmd = buffer[2];
        byte len = buffer[1];
        byte[] buf = new byte[len - 5];
        for (int i = 0; i < len - 5; i++) {
            buf[i] = buffer[i + 4];
        }
        String cardNumber = CardReadHelp.getInstance().BytetoHex(buf).replace(" ", "").toUpperCase();
        listener.onReadCardNumber(cardNumber);
    }

    private void pay(Activity activity, RequestParams params, final DataJsonCallBackV2 callBackV2) {
        NetWorkUtils.postHttpRequest(activity, false, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) throws JSONException {
                callBackV2.callback(0,result);
            }

            @Override
            public void onCancelled(String msg) throws JSONException {
                callBackV2.callback(1,msg);
            }

            @Override
            public void onError(String error) throws JSONException {
                callBackV2.callback(2,error);
            }

            @Override
            public void onFinish() {
                //不需要完成回调
                //callBackV2.callback(3,"");
            }
        });
    }

    /**************************************************************************
     *
     * 回调接口
     *
     **************************************************************************/

    /**
     * 当读取到卡号
     */
    public interface OnReadCardNumberListener{
        void onReadCardNumber(String cardNumber);
    }

    /**
     * 当读取到卡号余额
     */
    public interface OnReadCardBalanceListener{
        void onReadCardBalance(String cardBalance);
    }

    public interface OnGetCardPayModelListener{
        void beforeGet();
        void afterGet(ShangMiOrderRequest request);
    }

}
