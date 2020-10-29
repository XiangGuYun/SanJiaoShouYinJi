package android_serialport_api.cardwriteread.meal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils;
import android_serialport_api.cardwriteread.net.HttpCallBackV2;

/**
 * 支付助手类
 * @author YeXuDong
 */
public class PayHelper {
//    /**
//     * 是否正处于支付状态
//     */
//    public static boolean IS_PAYING = false;
//
//    /**
//     * 是否处于点餐页面
//     */
//    public static boolean PAY_ORDER = true;
//
//    /**
//     * 是否处于支付页面
//     */
//    public static boolean PAY_IN_FRONT = false;
//
//    /**
//     * 是否打开过支付对话框
//     * 由于支付对话框是一个Activity，因此只需onCreate一次即可
//     */
//    public static boolean haveOpenPayDialog = false;
//
//    private SimpleDateFormat simpleDateFormat;
//
//    private DecimalFormat df;
//
//    private OrderDao orderDao;
//
//    private PayHelper() {
//        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//        df = new DecimalFormat("#0.00");
//    }
//
//    private static PayHelper helper;
//
//    public static PayHelper getInstance() {
//        if (helper == null) {
//            helper = new PayHelper();
//        }
//        return helper;
//    }
//
//    /**
//     * 收银支付
//     * @param shangMiOrderRequest
//     */
//    public void doPayCash(final Activity activity, final ShangMiOrderRequest shangMiOrderRequest, final double price) {
//        if(orderDao == null){
//            orderDao = new OrderDao(activity);
//        }
//        final OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setPrice(PriceUtil.changeF2Y(shangMiOrderRequest.getAccountBalance()));
//
//        orderDetail.setOrderType(shangMiOrderRequest.getPayType());
//        switch (orderDetail.getOrderType()) {
//            //支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
//            case 1:
//                orderDetail.setOrderTypeStr("刷脸");
//                break;
//            case 2:
//                orderDetail.setOrderTypeStr("刷卡");
//                break;
//            case 3:
//                orderDetail.setOrderTypeStr("现金");
//                break;
//            case 4:
//                orderDetail.setOrderTypeStr("刷码");
//                break;
//            default:
//        }
//        orderDetail.setDateTime(simpleDateFormat.format(new Date()));
//        orderDetail.setShopId(shangMiOrderRequest.getShopID());
//        orderDetail.setCashierDeskId(shangMiOrderRequest.getCashierDeskID());
//
//        RequestParams params = new RequestParams(ApkConstant.CREATESHANGMI);
//        params.setAsJsonContent(true);
//        params.setBodyContent(new Gson().toJson(shangMiOrderRequest));
//        NetWorkUtils.postHttpRequest(false, params, new HttpCallBackV2() {
//            @Override
//            public void onSuccess(String result) {
//                JsonsRootBean orderResponse = JSONObject.parseObject(result, JsonsRootBean.class);
//                if (orderResponse.getCode() == 200) {
//                    PayActivity.Companion.setText("支付成功,请稍候...");
///*
//                    AdPresentation.setString("支付成功");
//*/
//                    SoundPlayUtils    .play(SoundPlayUtils.pay_success);
//                    orderDetail.setRealPrice(PriceUtil.changeF2Y((long) orderResponse.getData().getRealFee()));
//                    orderDetail.setDiscountPrice(PriceUtil.changeF2Y((long) (orderResponse.getData().getTotalFee() - orderResponse.getData().getRealFee())));
//                    orderDetail.setOrderNo(orderResponse.getData().getOrderNo());
//                    orderDao.insertData(orderDetail);
///*
//                    EventBus.getDefault().post(new MessageEvent());
//*/
//                    if (orderDetail.getOrderType() == 2) {//支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
//                        if (orderResponse.getData().getPayCard() != null) {
//                      /*      Constant.payUser = orderResponse.getData().getCustomerName();
//                            Constant.payBanlance =
//                                    PriceUtil.changeF2Y(Long.valueOf(orderResponse.getData().getPayCard().getAccountbalance()));
//                            //刷卡显示余额
//                            AdPresentation.setString(Constant.payUser + "余额：" + Constant.payBanlance + "元");*/
//                        }
//
//                        if (!Constant.staticPay) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    BusUtils.sendMsg(9);
//                                    hideDialogActivity(activity);
//                                }
//                            }, 1000);
//                        } else {
//                            Constant.startPay = true;
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    PayActivity.Companion.setText("等待支付……");
//                                 /*   AdPresentation.setBitmap(null);
//                                    AdPresentation.setString("等待支付……");*/
//                                    SoundPlayUtils.play(2);
//                                    IS_PAYING = false;
//                                }
//                            }, 1000);
//                        }
//                    } else {
//                        if (!Constant.staticPay) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    BusUtils.sendMsg(9);
//                                    hideDialogActivity(activity);
//                                }
//                            }, 1000);
//                        } else {
//                            Constant.startPay = true;
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    PayActivity.Companion.setText("等待支付……");
//                                 /*   AdPresentation.setBitmap(null);
//                                    AdPresentation.setString("等待支付……");*/
//                                    SoundPlayUtils.play(2);
//                                    IS_PAYING = false;
//                                }
//                            }, 1000);
//                        }
//                    }
//                    Constant.curOrderLis/**/t = orderDao.queryOrderList();
//                } else {
//                    SoundPlayUtils.play(SoundPlayUtils.pay_fail);
//                    PayActivity.Companion.setText("支付失败: " + orderResponse.getMessage());
///*
//                    AdPresentation.setString(orderResponse.getMessage());
//*/
//                    PayActivity.Companion.setText("等待支付...");
///*
//                    AdPresentation.setString("应付：" + df.format(price));
//*/
//                    SoundPlayUtils.play(2);
//                    IS_PAYING = false;
//                    ToastUtils.toast(orderResponse.getMessage());
//                }
//            }
//
//            @Override
//            public void onCancelled(String msg) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                IS_PAYING = false;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
//
//    }
//
//    /**
//     * 点餐支付
//     * @param shangMiOrderRequest
//     */
//    public void doPayOrder(final Activity activity, final ShangMiOrderRequest shangMiOrderRequest, final double price) {
//        int payMoney = 0;
//        int totalCount = 0;
//        List<CreatModel.ItemsBean> items = new ArrayList<>();
//        if (Constant.listDishOrder.size() == 0) {
//            payMoney = 0;
//        } else {
//            for (int i = 0; i < Constant.listDishOrder.size(); i++) {
//                payMoney += Constant.listDishOrder.get(i).getCount() * Constant.listDishOrder.get(i).getPrice();
//                totalCount += Constant.listDishOrder.get(i).getCount();
//                CreatModel.ItemsBean itemsBean = new CreatModel.ItemsBean();
//                itemsBean.setPrice(Constant.listDishOrder.get(i).getPrice());
//                itemsBean.setProductId(Constant.listDishOrder.get(i).getId() + "");
//                itemsBean.setProductName(Constant.listDishOrder.get(i).getName());
//                itemsBean.setQuantity(Constant.listDishOrder.get(i).getCount());
//                items.add(itemsBean);
//            }
//        }
//        CreatModel creatModel = new CreatModel();
//        creatModel.setCashierDeskId(Constant.cashierDeskId + "");
//        creatModel.setTotalFee(payMoney);
//        creatModel.setRealFee(payMoney);
//        creatModel.setTotalQuantity(totalCount);
//        creatModel.setSerialNumber(Jc_Utils.GetGUID());
//        creatModel.setPayType(shangMiOrderRequest.getPayType());
//        creatModel.setShopId(Constant.shopId + "");
//        creatModel.setItems(items);
//
//        if (shangMiOrderRequest.getPayType() == 1) {
////            人脸
////            if (shuaCardModel != null) {
////                creatModel.setCardNo(shuaCardModel.getData().getCardNo());
////                creatModel.setCustomerId(shuaCardModel.getData().getCustomerId() + "");
////                creatModel.setCustomerName(shuaCardModel.getData().getName());
////                creatModel.setCustomerPhone(shuaCardModel.getData().getPhone());
////            }
//        } else if (shangMiOrderRequest.getPayType() == 2) {
////            刷卡
//            creatModel.setCardNo(shangMiOrderRequest.getCardNo());
//            creatModel.setAccountBalance(shangMiOrderRequest.getAccountBalance() + "");
//        } else if (shangMiOrderRequest.getPayType() == 4) {
//            //扫码
//            creatModel.setAuthCode(shangMiOrderRequest.getAuthCode());
//        }
//        RequestParams params = new RequestParams(ApkConstant.CREATE);
//        params.setAsJsonContent(true);
//        params.setBodyContent(new Gson().toJson(creatModel));
//
//
//        NetWorkUtils.postHttpRequest(false, params, new HttpCallBackV2() {
//            @Override
//            public void onSuccess(String result) {
//                if (shangMiOrderRequest.getPayType() == 1) {
//                    BaseActModel baseActModel = JSONObject.parseObject(result, BaseActModel.class);
//                    int code = baseActModel.getCode();
//                    if (code == 200) {
//                        PayActivity.Companion.setText(baseActModel.getData().getCustomerName() + "支付成功,请稍候...");
///*
//                        AdPresentation.setString(baseActModel.getData().getCustomerName() + "支付成功");
//*/
//                        SoundPlayUtils.play(5);
//                        BusUtils.sendMsg(9);
//                        hideDialogActivity(activity);
//                    } else {
//                        IS_PAYING = false;
//                        BusUtils.sendMsg(6);
//                        ToastUtils.toast( baseActModel.getMessage());
//                    }
//                    return;
//                }
//                JsonsRootBean orderResponse = JSONObject.parseObject(result, JsonsRootBean.class);
//                if (orderResponse.getCode() == 200) {
//                    PayActivity.Companion.setText("支付成功,请稍候...");
///*
//                    AdPresentation.setString("支付成功");
//*/
//                    // BusUtils.sendMsg(0x1213);
//                    SoundPlayUtils.play(5);
//                    BusUtils.sendMsg(9);
//                    hideDialogActivity(activity);
//                } else {
//                    SoundPlayUtils.play(4);
//                    PayActivity.Companion.setText("支付失败: " + orderResponse.getMessage());
///*
//                    AdPresentation.setString(orderResponse.getMessage());
//*/
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            PayActivity.Companion.setText("等待支付...");
///*
//                            AdPresentation.setString("应付：" + new DecimalFormat("#0.00").format(price));
//*/
//                            SoundPlayUtils.play(2);
//                            IS_PAYING = false;
//                        }
//                    }, 1000);
//                    ToastUtils.toast(orderResponse.getMessage());
//                }
//            }
//
//            @Override
//            public void onCancelled(String msg) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                IS_PAYING = false;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
//
//    }
//
//    /**
//     * 隐藏当前dialog activity
//     * @param activity
//     */
//    public static void hideDialogActivity(Activity activity) {
//        activity.moveTaskToBack(true);
//        BusUtils.sendMsg(0x1212);
///*
//        AdPresentation.setString("云澎科技");
//*/
//        IS_PAYING = false;
//        PAY_IN_FRONT = false;
//    }
//
//    /**
//     * 使用人脸识别进行支付
//     * @param shuaCardModel 请求后端人脸识别成功后的响应数据
//     */
//    public static void doPayByFace(final Activity activity, ShuaCardModel shuaCardModel, double price) {
//        PayActivity.Companion.setText("支付中");
//        // 如果是从点餐界面进来
//        if (SharedPreferenceUtil.getInstance(MyApplication.getApplication()).getBoolean("isDiancan", false)) {
//            // 点餐提交接口
//            ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
//            shangMiOrderRequest.setPayType(1);
//            requestCreatePay(activity, price, shuaCardModel, shangMiOrderRequest);
//            return;
//        }
//        // 如果是从收银台界面进来
//        // 创建人脸支付模型类
//        PayByFaceRequestModel payByFaceRequestModel = new PayByFaceRequestModel();
//        // 将支付价格乘以100
//        BigDecimal priceInCent = new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(100));
//        // 设置账户余额
//        payByFaceRequestModel.setAccountBalance(priceInCent.longValue() + "");
//        // 设置收银台ID
//        payByFaceRequestModel.setCashierDeskID(Constant.cashierDeskId);
//        // 设置顾客ID
//        payByFaceRequestModel.setCustomerID(shuaCardModel.getData().getCustomerId());
//        // 设置顾客手机号码
//        payByFaceRequestModel.setCustomerPhone(shuaCardModel.getData().getPhone());
//        // 设置订单类型
//        payByFaceRequestModel.setOrderType(0);
//        // 设置支付类型
//        payByFaceRequestModel.setPayType(1);
//        // 设置门店ID
//        payByFaceRequestModel.setShopID(Constant.shopId);
//        // 设置顾客姓名
//        payByFaceRequestModel.setCustomerName(shuaCardModel.getData().getName());
//        // 设置卡号
//        payByFaceRequestModel.setCardNo(shuaCardModel.getData().getCardNo());
//
//        // 设置请求接口，并创建请求参数
//        RequestParams requestParams = new RequestParams(ApkConstant.CREATESHANGMI);
//        requestParams.setAsJsonContent(true);
//        // 设置请求体
//        requestParams.setBodyContent(new Gson().toJson(payByFaceRequestModel));
//
//        // 创建订单详情类
//        final OrderDetail orderDetail = new OrderDetail();
//        // 设置支付价格
//        orderDetail.setPrice(PriceUtil.changeF2Y(priceInCent.longValue()));
//        // 设置订单类型
//        orderDetail.setOrderType(1);
//        // 根据订单类型来设置订单类型字符串
//        switch (orderDetail.getOrderType()) {
//            case 1:
//                orderDetail.setOrderTypeStr("刷脸");
//                break;
//            case 2: //支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
//                orderDetail.setOrderTypeStr("刷卡");
//                break;
//            case 3:
//                orderDetail.setOrderTypeStr("现金");
//                break;
//            case 4:
//                orderDetail.setOrderTypeStr("刷码");
//                break;
//            default:
//        }
//        // 设置时间戳为当前时间
//        orderDetail.setDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        // 设置门店ID
//        orderDetail.setShopId(payByFaceRequestModel.getShopID());
//        // 设置收银台ID
//        orderDetail.setCashierDeskId(payByFaceRequestModel.getCashierDeskID());
//
//        final OrderDao orderDao = new OrderDao(activity);
//
//        // 请求人脸支付接口
//        NetWorkUtils.postHttpRequest(true, requestParams, new HttpCallBack.HttpCallback() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onSuccess(String result) {
//                BaseActModel baseActModel = JSONObject.parseObject(result, BaseActModel.class);
//                int code = baseActModel.getCode();
//                if (code == 200) {
//                    // 支付成功
//                    PayActivity.Companion.setText(String.format("%s支付成功,请稍候...", baseActModel.getData().getCustomerName()));
//                    // 更新副屏幕显示
//                    AdPresentation.setString(baseActModel.getData().getCustomerName() + "支付成功");
//                    BusUtils.sendMsg(0x1213);
//
//                    SoundPlayUtils.play(5);
//                    // 设置实际价格
//                    orderDetail.setRealPrice(PriceUtil.changeF2Y((long) baseActModel.getData().getRealFee()));
//                    // 设置优惠价格
//                    orderDetail.setDiscountPrice(PriceUtil.changeF2Y((long) (baseActModel.getData().getTotalFee() - baseActModel.getData().getRealFee())));
//                    // 设置订单序号
//                    orderDetail.setOrderNo(baseActModel.getData().getOrderNo());
//                    // 将订单详情插入到数据库中
//                    orderDao.insertData(orderDetail);
//                    EventBus.getDefault().post(new MessageEvent());
//                    // 支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
//                    if (orderDetail.getOrderType() == 2) {
//                        // 如果当前的支付类型为实体卡支付
//                        if (baseActModel.getData().getPayCard() != null) {
//                            // 记录顾客姓名
//                            Constant.payUser = baseActModel.getData().getCustomerName();
//                            // 记录账户余额
//                            Constant.payBanlance = PriceUtil.changeF2Y(baseActModel.getData().getPayCard().getAccountbalance());
//                        }
//                        if (!Constant.staticPay) {
//                            hideDialogActivity(activity);
//                        } else {
//                            Constant.startPay = true;
//                            PayActivity.Companion.setText("等待支付……");
//                            AdPresentation.setBitmap(null);
//                            AdPresentation.setString("等待支付……");
//                            SoundPlayUtils.play(2);
//                            IS_PAYING = false;
//                        }
//                    } else {
//                        if (!Constant.staticPay) {
////                            handler.sendEmptyMessage(9);
//                            BusUtils.sendMsg(9);
//                            hideDialogActivity(activity);
//                        } else {
//                            Constant.startPay = true;
//                            PayActivity.Companion.setText("等待支付……");
//                            AdPresentation.setBitmap(null);
//                            AdPresentation.setString("等待支付……");
//                            SoundPlayUtils.play(2);
//                            IS_PAYING = false;
//                        }
//                    }
//                    Constant.curOrderList = orderDao.queryOrderList();
//                } else {
//                    // 支付失败
//                    IS_PAYING = false;
////                    Message message = handler.obtainMessage();
////                    message.what = 6;
////                    handler.sendMessage(message);
//                    BusUtils.sendMsg(6);
//                    TastyToast.makeText(activity, baseActModel.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//                }
//            }
//
//            @Override
//            public void onCancelled(String msg) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                IS_PAYING = false;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
//    }
//
//    private static void requestCreatePay(final Activity activity, final double price, ShuaCardModel shuaCardModel, final ShangMiOrderRequest shangMiOrderRequest) {
//        int payMoney = 0;
//        int totalCount = 0;
//        List<CreatModel.ItemsBean> items = new ArrayList<>();
//        if (Constant.listDishOrder.size() == 0) {
//            payMoney = 0;
//        } else {
//            for (int i = 0; i < Constant.listDishOrder.size(); i++) {
//                payMoney += Constant.listDishOrder.get(i).getCount() * Constant.listDishOrder.get(i).getPrice();
//                totalCount += Constant.listDishOrder.get(i).getCount();
//                CreatModel.ItemsBean itemsBean = new CreatModel.ItemsBean();
//                itemsBean.setPrice(Constant.listDishOrder.get(i).getPrice());
//                itemsBean.setProductId(Constant.listDishOrder.get(i).getId() + "");
//                itemsBean.setProductName(Constant.listDishOrder.get(i).getName());
//                itemsBean.setQuantity(Constant.listDishOrder.get(i).getCount());
//                items.add(itemsBean);
//            }
//        }
//        CreatModel creatModel = new CreatModel();
//        creatModel.setCashierDeskId(Constant.cashierDeskId + "");
//        creatModel.setTotalFee(payMoney);
//        creatModel.setRealFee(payMoney);
//        creatModel.setTotalQuantity(totalCount);
//        creatModel.setSerialNumber(Jc_Utils.GetGUID());
//        creatModel.setPayType(shangMiOrderRequest.getPayType());
//        creatModel.setShopId(Constant.shopId + "");
//        creatModel.setItems(items);
//
//        if (shangMiOrderRequest.getPayType() == 1) {
////            人脸
//            if (shuaCardModel != null) {
//                creatModel.setCardNo(shuaCardModel.getData().getCardNo());
//                creatModel.setCustomerId(shuaCardModel.getData().getCustomerId() + "");
//                creatModel.setCustomerName(shuaCardModel.getData().getName());
//                creatModel.setCustomerPhone(shuaCardModel.getData().getPhone());
//            }
//        } else if (shangMiOrderRequest.getPayType() == 2) {
////            刷卡
//            creatModel.setCardNo(shangMiOrderRequest.getCardNo());
//            creatModel.setAccountBalance(shangMiOrderRequest.getAccountBalance() + "");
//        } else if (shangMiOrderRequest.getPayType() == 4) {
//            //扫码
//            creatModel.setAuthCode(shangMiOrderRequest.getAuthCode());
//        }
//        RequestParams params = new RequestParams(ApkConstant.CREATE);
//        params.setAsJsonContent(true);
//        params.setBodyContent(new Gson().toJson(creatModel));
//
//
//        NetWorkUtils.postHttpRequest(false, params, new HttpCallBack.HttpCallback() {
//            @Override
//            public void onSuccess(String result) {
//                if (shangMiOrderRequest.getPayType() == 1) {
//                    BaseActModel baseActModel = JSONObject.parseObject(result, BaseActModel.class);
//                    int code = baseActModel.getCode();
//                    if (code == 200) {
//                        PayActivity.Companion.setText(baseActModel.getData().getCustomerName() + "支付成功,请稍候...");
//                        AdPresentation.setString(baseActModel.getData().getCustomerName() + "支付成功");
//                        BusUtils.sendMsg(0x1213);
//
//                        IS_PAYING = false;
//                        SoundPlayUtils.play(5);
//                        BusUtils.sendMsg(9);
//                        hideDialogActivity(activity);
//                    } else {
//                        IS_PAYING = false;
//                        BusUtils.sendMsg(6);
//                        TastyToast.makeText(activity, baseActModel.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//                    }
//                    return;
//                }
//                JsonsRootBean orderResponse = JSONObject.parseObject(result, JsonsRootBean.class);
//                if (orderResponse.getCode() == 200) {
//                    PayActivity.Companion.setText("支付成功,请稍候...");
//                    AdPresentation.setString("支付成功");
//                    BusUtils.sendMsg(0x1213);
//
//                    IS_PAYING = false;
//                    SoundPlayUtils.play(5);
//                    BusUtils.sendMsg(9);
//                    hideDialogActivity(activity);
//                } else {
//                    SoundPlayUtils.play(4);
//                    PayActivity.Companion.setText("支付失败: " + orderResponse.getMessage());
//                    AdPresentation.setString(orderResponse.getMessage());
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            PayActivity.Companion.setText("等待支付...");
//                            AdPresentation.setString("应付：" + new DecimalFormat("#0.00").format(price));
//                            SoundPlayUtils.play(2);
//                            IS_PAYING = false;
//                        }
//                    }, 1000);
//                    TastyToast.makeText(activity, orderResponse.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//                }
//            }
//
//            @Override
//            public void onCancelled(String msg) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                IS_PAYING = false;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
//    }

}
