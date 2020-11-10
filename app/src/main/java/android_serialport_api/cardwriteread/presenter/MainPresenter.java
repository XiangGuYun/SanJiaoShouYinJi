package android_serialport_api.cardwriteread.presenter;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.xutils.http.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPort;
import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.MyApplication;
import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.baiduface.baiducallBack.FaceDetectCallBack;
import android_serialport_api.cardwriteread.baiduface.baiducamera.AutoTexturePreviewView;
import android_serialport_api.cardwriteread.baiduface.baiducamera.CameraPreviewManager;
import android_serialport_api.cardwriteread.baiduface.baidumanage.FaceSDKManager;
import android_serialport_api.cardwriteread.baiduface.baidumodel.LivenessModel;
import android_serialport_api.cardwriteread.baiduface.baidumodel.SingleBaseConfig;
import android_serialport_api.cardwriteread.baiduface.baidumodel.User;
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils;
import android_serialport_api.cardwriteread.base.BasePresenter;
import android_serialport_api.cardwriteread.contract.MainContract;

import com.yp.baselib.utils.SPUtils;

import android_serialport_api.cardwriteread.meal.BusUtils;
import android_serialport_api.cardwriteread.model.DataJsonCallBackV2;
import android_serialport_api.cardwriteread.model.MainModel;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.net.bean.GetBalanceRequest;
import android_serialport_api.cardwriteread.net.bean.GetBalanceResponse;
import android_serialport_api.cardwriteread.net.bean.OrderDetail;
import android_serialport_api.cardwriteread.net.bean.ShangMiOrderRequest;
import android_serialport_api.cardwriteread.util.CardReadHelp;
import android_serialport_api.cardwriteread.util.ScanGunKeyEventHolder;
import android_serialport_api.cardwriteread.util.SharedUtil;
import android_serialport_api.cardwriteread.util.SoundUtils;
import android_serialport_api.cardwriteread.util.SyncUserFaceServiceV3;
import android_serialport_api.cardwriteread.view.MainActivity;
import android_serialport_api.cardwriteread.view.MainViceScreen;


/**
 * 这里有一个问题
 * 用总店的id支付可以成功
 * 但是用分店的id支付会显示不支持卡片类型错误
 * 这里测试暂时先用总店id
 *
 * @author 86139
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    private MainModel mainModel;
    //二维码扫描工具类
    public ScanGunKeyEventHolder scanGunKeyEventHolder;

    //收款金额
    private float money = 0;
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
    private int PayModel = -1;
    private ExecutorService pool;

    public boolean isPayState() {
        return isPayState;
    }

    /**
     * 是否开启支付状态
     * 默认不开启
     * 用途区别用户正在输入金额没有开启支付状态，防止用户刷卡程序进行了过度处理
     * 弹窗弹出则是支付状态
     * 弹出关闭则是非支付状态
     */
    private boolean isPayState = false;

    /**
     * 付款操作是否完成
     * 这是一种付款中的状态只有在啊支付状态开启下才能开启这种状态
     * 用途 用于三种支付方式的资源竞争
     * 用于防止用户过快刷卡
     * 默认是完成状态
     * 付款完成时候才能发起下一笔支付操作
     * 否则卡住
     */
    private boolean ispayfinsh = true;

    /**
     * 是否设置开启了固定收银
     * 默认不开启
     */
    private boolean fixedpay = false;

    /**
     * 是否开启人脸识别支付
     * 默认开启
     */
    private boolean isFacePay = true;

    /**
     * 副屏
     */
    private DisplayManager mDisplayManager;//屏幕管理类xrcr
    private Display mDisplay[];//屏幕数组
    private MainViceScreen mPresentation;
    @Deprecated
    private boolean isPresentationPause = false; //是否暂停副屏幕更新

    //刷卡
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;

    //后台更新
    private SyncUserFaceServiceV3 syncUserFaceService = null;

    //倒计时控制查询余额屏幕显示
    private CountDownTimer countDownTimer = null;

    public MainPresenter() {
        mainModel = new MainModel();
    }

    @Override
    public void doPay(final ShangMiOrderRequest shangMiOrderRequest) {
        try {
            if (money <= 0) {
                return;
            }
            SoundUtils.play(3);//music:支付中
            RequestParams params = new RequestParams(Apis.OffLineIp + Apis.CreateShangMi);
            params.setAsJsonContent(true);
            String json = new Gson().toJson(shangMiOrderRequest);
            params.setBodyContent(json);
            Log.d("PayResult", json);
            mainModel.pay(mView.getActivity(), params, (code, s) -> {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                try {
                    final JSONObject rep = JSONObject.parseObject(s);
                    if (rep.getInteger("code") == 200) {
                        //支付成功也延迟一秒
                        String username = JSONObject.parseObject(s).getJSONObject("data").getString("customerName");
                        SoundUtils.play(5); //music:支付成功
                        username = NameToStar(username);
                        if (PayModel == 1) username = "";
                        if (mPresentation != null) {
                            if (PayModel != 1) mPresentation.uploadDetails("姓名:" + username);
                            mPresentation.updateTextForPay("支付成功", R.color.black1);
                        }
                        mView.uploadPayDialogText(username + "\n支付成功");
                        //根据卡号查询余额显示
                        queryBanlce(rep.getJSONObject("data").getString("orderNo"), mPresentation.getDetailsText());
                        //保存订单记录到本地数据库
                        addOrder(rep.getJSONObject("data").getString("payType"), String.valueOf(money), rep.getJSONObject("data").getString("orderNo"));
                        //更新订单界面
                        mView.initOrderData();
                        paySuccess();
                    } else {
                        //支付失败接着等待支付
                        String fail = "";
                        if (s.contains("message")) {
                            fail = "支付失败\n" + rep.getString("message");
                        }
                        SoundUtils.play(4); //music:支付失败
                        if (mPresentation != null)
                            mPresentation.updateTextForPay(fail, R.color.black1);
                        mView.uploadPayDialogText(fail);
                        payFail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MainViceScreen.showAdPicture();
                }
            });
        } catch (Exception e) {
            mView.toast("do pay error", 0);
            e.printStackTrace();
        }
    }

    //姓名打*号
    private String NameToStar(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        if (s.length() == 2) s = s.substring(0, 1) + "*";
        if (s.length() > 2) s = s.substring(0, 1) + "*" + s.substring(2, 3);
        return s;
    }

    /**
     * 下面封了一个支付成功方法一个支付失败方法
     * 为了我们更方便的阅读业务逻辑
     * 也同时收到了延迟的影响
     */
    private void paySuccess() {
        int dy = 1500;
        if (fixedpay == false) {
            dy = 1500;
        } else {
            dy = 3000;
        }
        new Handler().postDelayed(() -> {
            if (mPresentation != null) mPresentation.updateTextForPay("等待支付", R.color.black1);
            mView.uploadPayDialogText("等待支付");
            if (!fixedpay) {
                //更改支付状态
                //关闭支付弹窗
                mView.closePayDialog(MainActivity.isMeal);
            }
            if(MainActivity.isMeal){
                mView.closePayDialog(MainActivity.isMeal);
                BusUtils.sendMsg(0x1111);
            }
            ispayfinsh = true;
            //isPresentationPause = false;
            //状态复位
            PayModel = -1;
        }, dy);
    }

    private void payFail() {
        new Handler().postDelayed(() -> {
            ispayfinsh = true;
            // isPresentationPause = false;
            //状态复位
            PayModel = -1;
        }, 1500);
    }

    //保存订单数据到本地数据库
    private void addOrder(String ordertype, String money, String orderno) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNo(orderno);
        orderDetail.setOrderTypeStr("");
        orderDetail.setOrderType(Integer.valueOf(ordertype));
        orderDetail.setPrice(money);
        orderDetail.setRealPrice(money);
        orderDetail.setDiscountPrice("");
        orderDetail.setShopId(Constant.branchId);
        orderDetail.setDateTime(simpleDateFormat.format(date));
        MyApplication.getAppDBManager().getOrderDao().insertData(orderDetail);
    }

    /**
     * 获取机器扫码内容
     */
    @Override
    public void initSearchQrCode() {
        try {
            scanGunKeyEventHolder = new ScanGunKeyEventHolder();
            scanGunKeyEventHolder.setReadSuccessListener(barcode -> {
                if (isPayState && ispayfinsh) {
                    MainViceScreen.hideAdPicture();
                    ispayfinsh = false;
                    PayModel = 4;
                    ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
                    shangMiOrderRequest.setAuthCode(barcode);
                    shangMiOrderRequest.setPayType(4);
                    shangMiOrderRequest.setShopID(Constant.shopId);
                    shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);
                    BigDecimal priceIncent = new BigDecimal(String.valueOf(money)).multiply(new BigDecimal(100));
                    shangMiOrderRequest.setAccountBalance(priceIncent.longValue());
                    doPay(shangMiOrderRequest);
                    mView.uploadPayDialogText("支付中...");
                }
            });
        } catch (Exception e) {
            mView.toast("扫描错误", 0);
            e.printStackTrace();
        }
    }

    /**
     * 更新支付状态
     */
    @Override
    public void uploadPayState(boolean b) {
        isPayState = b;
    }

    /**
     * 更新是否设置为固定收银状态
     */
    @Override
    public void uploadFixedPayState() {
        fixedpay = !fixedpay;
        //记录当前分店设置状态以便于下次重启系统开启
        SharedUtil.getInstatnce().getShared(mView.getActivity()).edit().putBoolean(Constant.branchId + "-SolidSetting", fixedpay).commit();
    }

    /**
     * 更新开启/关闭人脸识别支付
     */
    public void setisFacePay(boolean b) {
        isFacePay = b;
        //记录当前分店设置状态以便于下次重启系统开启
        SharedUtil.getInstatnce().getShared(mView.getActivity()).edit().putBoolean(Constant.branchId + "-FaceSetting", b).commit();
    }

    /**
     * 设置付款金额
     * 在ecitview中监听写入
     *
     * @param money
     */
    public void setMoney(float money) {
        this.money = money;
    }

    /*--------------------------------分割线 start------------------------------------------------*/

    /**
     * 刷卡支付
     * 初始化卡片
     * 开启线程
     */
    @Override
    public void doCardNumber(String cardNumber) {
        try {
            mSerialPort = MyApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        pool = Executors.newSingleThreadExecutor();
        pool.submit(() -> {
            while (!pool.isShutdown()) {
                setHex();
                byte[] buffer = new byte[64];
                if (mInputStream == null) {
                    return;
                }
                try {
                    mInputStream.read(buffer);
                    parase_pkg(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        syncUserFaceService.stopService();
        mPresentation.dismiss();
    }

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
        String nfc = CardReadHelp.getInstance().BytetoHex(buf).replace(" ", "").toUpperCase();
        //只有当处于支付状态才有刷卡操作
        if (isPayState) {
            processIntent(nfc);
        } else {
            queryBanlce(nfc, "");
        }
    }

    //设置HEX读取
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

    private void processIntent(String cardno) {
        //拿出我们想要的HEX
        if (cardno.length() < SPUtils.INSTANCE.getInt(mPresentation.getContext(), "cardLength", 9, "default")) {
            return;
        }
        if (SPUtils.INSTANCE.getInt(mPresentation.getContext(), "debug", 0, "default") == 1) {
            ToastUtils.toast("卡号长度是：" + cardno.length());
        }
        if (money <= 0) {
            return;
        }
        if (isPayState && ispayfinsh) {
            ispayfinsh = false;
            PayModel = 2;
            if (TextUtils.isEmpty(String.valueOf(money))) {
                return;
            }
            CardPay(cardno);
        }
    }

    //刷卡支付操作
    private void CardPay(final String nfc) {
        try {
            if (TextUtils.isEmpty(nfc)) {
                return;
            }
            MainViceScreen.hideAdPicture();
            mView.uploadPayDialogText("支付中...");
            ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
            shangMiOrderRequest.setCardNo(nfc);
            shangMiOrderRequest.setPayType(2);
            shangMiOrderRequest.setShopID(Constant.shopId);
            shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);
            BigDecimal priceInCent = new BigDecimal(String.valueOf(money)).multiply(new BigDecimal(100));
            shangMiOrderRequest.setAccountBalance(priceInCent.longValue());
            doPay(shangMiOrderRequest);
        } catch (Exception e) {
            mView.toast("CardPay 刷卡支付操作错误", 0);
            e.printStackTrace();
        }
    }

    //查询余额
    //s 为附属字段 如果传了则直接append再后面 如果为空则不处理
    private void queryBanlce(String nfc, final String str) {
        try {
            if (TextUtils.isEmpty(nfc)) {
                return;
            }
            GetBalanceRequest getBalanceRequest = new GetBalanceRequest();
            getBalanceRequest.setCardNo(nfc.toUpperCase());
            getBalanceRequest.setShopId(Constant.shopId);
            getBalanceRequest.setDeviceId(Build.SERIAL);
            RequestParams params = new RequestParams(Apis.OffLineIp + Apis.GetBanlance);
            params.setAsJsonContent(true);
            params.setBodyContent(new Gson().toJson(getBalanceRequest));
            mainModel.pay(mView.getActivity(), params, new DataJsonCallBackV2() {
                @Override
                public void callback(int code, String s) {
                    //{"code":200,"pageCount":0,"pageSize":100,"message":"SUCCESS","data":{"username":"邹焕鑫","balance":"199999878300"}}
                    int sta = JSONObject.parseObject(s).getInteger("code");
                    String details = "";
                    try {
                        if (code == 0 && sta == 200) {
                            //读取卡片余额成功
                            GetBalanceResponse getBalanceResponse = JSONObject.parseObject(s, GetBalanceResponse.class);
                            if (getBalanceResponse.getData().getUsername() != null) {
                                String str = String.valueOf((Integer.parseInt(getBalanceResponse.getData().getBalance()) * 0.01));
                                details = "姓名:" + NameToStar(getBalanceResponse.getData().getUsername()) + "\n余额:" + str;
                            }
                        }
                        if (!details.equals("")) {
                            mPresentation.uploadDetails(TextUtils.isEmpty(str) ? details : str + "\n" + details);
                        }
                        if(sta != 200){
                            String message = JSONObject.parseObject(s).getString("message");
                            mPresentation.uploadDetails(message);
                        }
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        countDownTimer = new CountDownTimer(3000, 3000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                mPresentation.uploadDetails("");
                            }
                        }.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*--------------------------------分割线 end------------------------------------------------*/


    /**
     * 初始化副屏 初始化摄像头
     */
    @Override
    public void initViceScreen() {
        try {
            mDisplayManager = (DisplayManager) mView.getActivity().getSystemService(Context.DISPLAY_SERVICE);
            mDisplay = mDisplayManager.getDisplays();
            mDisplay = mDisplayManager.getDisplays();
            if (mDisplay.length < 2) {
                return;
            }
            if (mPresentation == null) {
                mPresentation = new MainViceScreen(mView.getActivity(), mDisplay[1]);
                mPresentation.show();
                mPresentation.updateTextForPay("待输入", R.color.black1);
            }
        } catch (Exception e) {
            mView.toast("初始化副屏发生异常：" + e.getLocalizedMessage(), 0);
            Log.d("Test", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public MainViceScreen getmPresentation() {
        return mPresentation;
    }

    @Override
    public void updateViceScreenDisplay() {
        //mPresentation.uploadImage(saveBitmap);
        mPresentation.updateTextForPay(money == 0 ? "待输入" : "应付金额:" + money, R.color.black1);
    }


    @Override
    public void initCamera(Activity activity, AutoTexturePreviewView mAutoCameraPreviewView) {
        CameraPreviewManager.getInstance().setCameraFacing(Integer.parseInt(SPUtils.INSTANCE.get(activity, "CameraFace", CameraPreviewManager.CAMERA_FACING_FRONT, "default").toString()));
//        CameraPreviewManager.getInstance().setDisplayOrientation(Integer.parseInt(SPUtils.Companion.get(activity, "displayOrient", 180).toString()));
        CameraPreviewManager.getInstance().startPreview(activity, mPresentation.getTexturePreview(),
                SingleBaseConfig.getBaseConfig().getRgbAndNirWidth(), SingleBaseConfig.getBaseConfig().getRgbAndNirHeight(),
                (data, camera, width, height) -> {
                    // 摄像头预览数据进行人脸检测
                    //调节资源冲突
                    //只有当为支付状态并且开启人脸识别支付后才能开始识别支付
                    //isFacePay为是否开启人脸识别，由用户手动按钮开启/关
                    System.out.println("isPayState" + isPayState + " isFacePay" + isFacePay);
                    MainViceScreen.showTV0("是否是人脸支付："+isFacePay, "是否是支付状态："+isPayState);
                    if (isPayState && isFacePay) {
                        FaceSDKManager.getInstance().onDetectCheck(data, null, null,
                                height, width, SingleBaseConfig.getBaseConfig().getType(), new FaceDetectCallBack() {
                                    @Override
                                    public void onFaceDetectCallback(LivenessModel livenessModel) {
                                        try {
                                            if (livenessModel != null){
                                                MainViceScreen.showTV1("当前是否检测到人脸：是");
                                                MainViceScreen.showTV2("检测到的人脸显示度是："+livenessModel.getFeatureScore());
                                                checkCloseResult(livenessModel);
                                            } else {
                                                MainViceScreen.showTV1("当前是否检测到人脸：否");
                                            }
                                        } catch (Exception e) {
                                            System.out.println("*** 发生了异常：" + e.getLocalizedMessage());
                                        }
                                    }

                                    @Override
                                    public void onTip(int code, String msg) {

                                    }

                                    @Override
                                    public void onFaceDetectDarwCallback(LivenessModel livenessModel) {

                                    }
                                });
                    }

                    //判断卡帧
                    //一旦人脸识别进入了支付状态则 isPresentationPause 为会改为true，意味这卡帧，也就是停止对副屏幕的更新
                    //直到人脸识别付款完毕 isPresentationPause 状态重新回到false
                    //只有人脸识别支付方法中才能改变 isPresentationPause 的状态为true，另外俩种支付不会改变
//                        if (isPresentationPause == false){
//                            Bitmap tempBmp = new NV21ToBitmap(mView.getActivity()).nv21ToBitmap(data,width, height);
//                            saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, tempBmp);
//                            if (saveBitmap != null) {
//                                //传到副屏进行显示
//                                mPresentation.uploadImage(saveBitmap);
//                            }
//                        }
                });

    }

    /**
     * 后台更新
     */
    @Override
    public void enableBackgroundUpdate(boolean is) {
        //检查前台状态
        if (syncUserFaceService == null) {
            syncUserFaceService = new SyncUserFaceServiceV3();
            syncUserFaceService.startService(mView.getActivity());
        }
        syncUserFaceService.setIs(is);
    }

    //检测人脸
    private void checkCloseResult(LivenessModel livenessModel) {
        System.out.println("当前的相似度是" + livenessModel.getFeatureScore());
        User user = livenessModel.getUser();
        if (user == null && !mPresentation.getDetailsText().contains("尊敬的")) {
            MainViceScreen.hideAdPicture();
            mView.uploadPayDialogText("识别失败,等待支付...");
            mPresentation.uploadDetails("识别失败,请调整姿态");
        } else {
            if (isPayState && ispayfinsh && livenessModel.getFeatureScore() >=
                    Integer.parseInt(SPUtils.INSTANCE.get(mView.getActivity(), Constant.FACE_SIMILAR, 94, "default").toString())) {
                //识别成功开始付款
                MainViceScreen.hideAdPicture();
                isPresentationPause = true;
                ispayfinsh = false;
                PayModel = 1;
                System.out.println("相似度:" + livenessModel.getFeatureScore() + "  user:" + user.getUserInfo());
                String name = user.getUserInfo().split("-")[0];
                ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
                shangMiOrderRequest.setCustomerID(Integer.valueOf(livenessModel.getUser().getUserInfo().split("-")[2]));
                shangMiOrderRequest.setPayType(1);
                shangMiOrderRequest.setShopID(Constant.shopId);
                shangMiOrderRequest.setCustomerName(name);
                shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);
                BigDecimal priceIncent = new BigDecimal(String.valueOf(money)).multiply(new BigDecimal(100));
                shangMiOrderRequest.setAccountBalance(priceIncent.longValue());
                doPay(shangMiOrderRequest);
                if (name.length() == 3) {
                    name = name.charAt(0) + "*" + name.charAt(2);
                } else if (name.length() == 2) {
                    name = name.charAt(0) + "*";
                }
                mView.uploadPayDialogText("支付中，尊敬的:" + name);
                mPresentation.uploadDetails("尊敬的:" + name);
            }
        }
    }

    /**
     * 统计订单数据
     */
    @Override
    public void statisticalOrderData() {
        RequestParams params = new RequestParams(Apis.OffLineIp + Apis.GetShangMirOrder + Constant.cashierDeskId);
        params.setAsJsonContent(true);
        params.setBodyContent("");
        System.out.println("订单接口:" + Apis.OffLineIp + Apis.GetShangMirOrder + Constant.cashierDeskId);
        mainModel.httpGetRequest(mView.getActivity(), params, new DataJsonCallBackV2() {
            @Override
            public void callback(int code, String s) throws JSONException {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                try {
                    JSONObject rep = JSONObject.parseObject(s);
                    if (code == 0 && rep.getInteger("code") == 200) {
                        JSONArray array = rep.getJSONArray("data");
                        List<JSONObject> tlist = new ArrayList<>();
                        for (int i = 0; i < array.size(); i++) {
                            tlist.add(array.getJSONObject(i));
                        }
                        mView.showStatisticalOrderDataDialog(tlist);
                    }
                } catch (Exception e) {
                    mView.toast("统计数据异常", 0);
                    e.printStackTrace();
                }
            }
        });
    }
}
