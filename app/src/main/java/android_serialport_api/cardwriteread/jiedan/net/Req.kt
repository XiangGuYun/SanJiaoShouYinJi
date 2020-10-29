package android_serialport_api.cardwriteread.jiedan.net

import android.util.Log
import android_serialport_api.cardwriteread.Constant
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils
import android_serialport_api.cardwriteread.jiedan.api.MyCallback
import android_serialport_api.cardwriteread.jiedan.api.MyRetrofit
import android_serialport_api.cardwriteread.jiedan.api.request.InitRequest
import android_serialport_api.cardwriteread.jiedan.api.request.OrderListRequest
import android_serialport_api.cardwriteread.jiedan.api.response.InitResponse
import android_serialport_api.cardwriteread.jiedan.api.response.OrderListResponse
import android_serialport_api.cardwriteread.jiedan.api.response.OrderVO
import android_serialport_api.cardwriteread.meal.Jc_Utils
import com.google.gson.Gson
import com.kotlinlib.common.Ctx
import com.yp.baselib.utils.LogUtils
import retrofit2.Call

object Req {

    /**
     * 登录接单系统
     */
    fun loginJieDan(ctx: Ctx, username:String, password:String, callback1:(InitResponse)->Unit,
        callback2: (OrderListResponse) -> Unit){

        val loginRequest = InitRequest()
        loginRequest.deviceId = Jc_Utils.getMacFromHardware(ctx)
        loginRequest.username = username
        loginRequest.password = password
        MyRetrofit.getApiService().shangmishouchiInit(loginRequest).enqueue(object : MyCallback<InitResponse?>() {

            override fun onSuccess(loginResponse: InitResponse?) {
                Log.d("OK_ME", Gson().toJson(loginResponse))
                loginResponse?.let {
                    if (loginResponse.code == 200) {
                        JieDanConstant.shopId = loginResponse.data.shopId
                        JieDanConstant.shopName = loginResponse.data.shopName
                        JieDanConstant.employeeId = loginResponse.data.employeeId
                        callback1.invoke(loginResponse)
                        getOrderList(ctx, callback2)
                    }
                }
            }

            override fun onFailure(call: Call<InitResponse?>, t: Throwable) {
                super.onFailure(call, t)
                Log.d("OK_ME", t.localizedMessage)
            }
        })
    }

    /**
     * 获取订单列表
     */
    private fun getOrderList(ctx: Ctx, callback2: (OrderListResponse) -> Unit){
        val request = OrderListRequest()
        request.branchId = Constant.branchId
        request.deviceId = Jc_Utils.getMacFromHardware(ctx)
        request.shopId = JieDanConstant.shopId
        request.page = 1
        request.employeeId = JieDanConstant.employeeId

        MyRetrofit.getApiService().shangmishouchiOrderList(request).enqueue(object : MyCallback<OrderListResponse?>() {
            override fun onSuccess(response: OrderListResponse?) {
                response?.let {
                    if (response.code == 200) {
                        callback2.invoke(response)
                    } else {
                        ToastUtils.toast("暂无订单")
                    }
                }

            }
        })
    }

    /**
     * 获取新订单
     */
    fun getNewOrder(ctx: Ctx, onSuccess:(List<OrderVO>)->Unit){
        val request = OrderListRequest()
        request!!.branchId = Constant.branchId
        request.deviceId = Jc_Utils.getMacFromHardware(ctx)
        request.shopId = JieDanConstant.shopId
        request.page = 1
        request.employeeId = JieDanConstant.employeeId
        MyRetrofit.getApiService().shangmishouchiGetNewOrder(request).enqueue(object : MyCallback<OrderListResponse?>() {

            override fun onSuccess(response: OrderListResponse?) {
                Log.d("OK_ME", Gson().toJson(response))
                if (response!!.code == 200) {
                    onSuccess.invoke(response.data)
                } else {
//                    ToastUtils.toast(response.message)
                }
            }

            override fun onFailure(call: Call<OrderListResponse?>, t: Throwable) {
                super.onFailure(call, t)
                Log.d("OK_ME", t.localizedMessage)
            }

        })
    }


}