package android_serialport_api.cardwriteread.net;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.android.tu.loadingdialog.LoadingDailog;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;

import java.util.List;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils;
import android_serialport_api.cardwriteread.meal.AllCaiModel;
import android_serialport_api.cardwriteread.meal.AllCaiTypeModel;
import android_serialport_api.cardwriteread.meal.ApkConstant;
import android_serialport_api.cardwriteread.meal.MealHttpCallBack;
import android_serialport_api.cardwriteread.meal.OrderCountModel;
import android_serialport_api.cardwriteread.net.bean.RequestCaiModel;

/**
 * 管理所有的网络请求
 */
public class Req {

    /**
     * 请求所有菜品类型
     * @param isShowDialog
     * @param callback
     * @param callbackReqDishes
     */
    public static void reqDishesTypes(boolean isShowDialog, CallbackReqDishesTypes callback, CallbackReqDishes callbackReqDishes){
        RequestCaiModel requestCaiModel = new RequestCaiModel();
        requestCaiModel.setPage_num(1);
        requestCaiModel.setPage_size(100);
        requestCaiModel.setSql_id("D2MINI-COOKPLAN-002");
        RequestCaiModel.ParamsBean paramsBean = new RequestCaiModel.ParamsBean();
        paramsBean.setShopId(Constant.shopId+"");
        paramsBean.setCashierDeskId(Constant.cashierDeskId+"");
        requestCaiModel.setParams(paramsBean);

        RequestParams params = new RequestParams(ApkConstant.ISQLQUERY);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(requestCaiModel));
        NetWorkUtils.postHttpRequest(isShowDialog, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) {
                AllCaiTypeModel allCaiTypeModel = JSONObject.parseObject(result,AllCaiTypeModel.class);
                if (allCaiTypeModel.getCode() == 200){
                    List<AllCaiTypeModel.DataBean.ResultsBean> resultsBeanList = allCaiTypeModel.getData().getResults();
                    callback.callback(resultsBeanList);
                }else{
                    ToastUtils.toast(allCaiTypeModel.getMessage());
                }
                Req.reqDishes(callbackReqDishes);
            }

            @Override
            public void onCancelled(String msg) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 请求所有的菜品数据
     */
    public static void reqDishes(CallbackReqDishes callback) {
        RequestCaiModel requestCaiModel = new RequestCaiModel();
        requestCaiModel.setPage_num(1);
        requestCaiModel.setPage_size(100);
        requestCaiModel.setSql_id("D2MINI-COOKPLAN-001");
        RequestCaiModel.ParamsBean paramsBean = new RequestCaiModel.ParamsBean();
        paramsBean.setShopId(Constant.shopId+"");
        paramsBean.setCashierDeskId(Constant.cashierDeskId+"");
        requestCaiModel.setParams(paramsBean);

        RequestParams params = new RequestParams(ApkConstant.ISQLQUERY);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(requestCaiModel));
        System.out.println("req:"+new Gson().toJson(requestCaiModel));
        NetWorkUtils.postHttpRequest(true, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) {
                System.out.println("res:"+result);
                AllCaiModel allCaiModel = JSONObject.parseObject(result,AllCaiModel.class);
                if (allCaiModel.getCode() == 200){
                    List<AllCaiModel.DataBean.ResultsBean> resultsBeanList = allCaiModel.getData().getResults();
                    System.out.println("resultsBeanList:"+resultsBeanList);
                    if (resultsBeanList == null){
                        return;
                    }
                    callback.callback(resultsBeanList);
                }else{
                    ToastUtils.toast(allCaiModel.getMessage());
                }
            }

            @Override
            public void onCancelled(String msg) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 请求订单数量
     */
    public static void reqOrderCount(Activity activity, CallbackReqOrderCount callbackReqOrderCount) {
        RequestParams params = new RequestParams(ApkConstant.GETSHANGMIORDER+Constant.cashierDeskId);
        params.setAsJsonContent(true);
        params.setBodyContent("");
        NetWorkUtils.getHttpRequest(activity, false,params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) {
                OrderCountModel orderCountModel = JSONObject.parseObject(result,OrderCountModel.class);
                if (orderCountModel.getCode() == 200){
                    callbackReqOrderCount.callback(orderCountModel.getData());
                }else{
                    ToastUtils.toast(orderCountModel.getMessage());
                }
            }

            @Override
            public void onCancelled(String msg) {

            }

            @Override
            public void onError(String error) {

            }
            @Override
            public void onFinish() {

            }
        });
    }

    public interface CallbackReqOrderCount{
        void callback(List<OrderCountModel.DataBean> list);
    }

    public interface CallbackReqDishes{
        void callback(List<AllCaiModel.DataBean.ResultsBean> list);
    }

    public interface CallbackReqDishesTypes{
        void callback(List<AllCaiTypeModel.DataBean.ResultsBean> list);
    }

}
