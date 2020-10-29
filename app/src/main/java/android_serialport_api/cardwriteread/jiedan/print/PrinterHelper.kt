package android_serialport_api.cardwriteread.jiedan.print

import android.content.Context
import android.hardware.usb.UsbManager
import android.util.Log
import android_serialport_api.cardwriteread.Constant
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils
import android_serialport_api.cardwriteread.jiedan.api.response.OrderVO
import android_serialport_api.cardwriteread.jiedan.net.JieDanConstant
import com.sunmi.externalprinterlibrary.api.ConnectCallback
import com.sunmi.externalprinterlibrary.api.SunmiPrinter
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi
import java.text.SimpleDateFormat

/**
 * 打印机帮助类
 */
class PrinterHelper private constructor() {

    companion object {
        private var helper: PrinterHelper? = null

        fun getInstance(): PrinterHelper {
            if (helper == null) {
                helper = PrinterHelper()
            }
            return helper!!
        }
    }

    fun test(context: Context): PrinterHelper {
        val mUsbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = mUsbManager.deviceList
        deviceList.values.forEach {
            Log.d("PrinterTag", it.deviceName + " " + it.vendorId + " " + it.productId)
        }
        return this
    }

    /**
     * 是否已连接上打印机
     */
    var isConnected = false

    /**
     * 连接打印机
     */
    fun connect(context: Context) {
        SunmiPrinterApi.getInstance().connectPrinter(context, SunmiPrinter.SunmiNTPrinter,
                object : ConnectCallback {
                    override fun onFound() {
                        //发现打印机会回调此⽅法
                        Log.d("PrinterTag", "onFound")
                    }

                    override fun onUnfound() {
                        //如果没找到打印机会回调此⽅法
                        Log.d("PrinterTag", "onUnfound")
                    }

                    override fun onConnect() {
                        //连接成功后会回调此⽅法，则可以打印
                        Log.d("PrinterTag", "onConnect")
                        isConnected = true
                    }

                    override fun onDisconnect() {
                        //连接中打印机断开会回调此⽅法，此时将中断打印
                        Log.d("PrinterTag", "onDisconnect")
                    }
                });
    }

    /**
     * 取消连接
     */
    fun disconnect(context: Context) {
        try {
            if (isConnected)
                SunmiPrinterApi.getInstance().disconnectPrinter(context)
        } catch (e: Exception) {

        }
    }

    /**
     * 打印小票
     */
    fun printTicket(orderVO: OrderVO) {
        if(!isConnected) {
            ToastUtils.toast("未连接打印机")
            return
        }
        SunmiPrinterApi.getInstance().apply {
            setAlignMode(1)
            setFontZoom(1, 1)
            printText("${JieDanConstant.shopName}\n\n")
            enableBold(true)
            setFontZoom(2, 2)
            printText("${Constant.shopName}\n\n")
            printText("取餐号：${orderVO.serial}\n\n")
            enableBold(false)
            setFontZoom(1, 1)
            printText("订单编号：${orderVO.orderNo}\n\n\n")
            orderVO.orderItemList.forEachIndexed { index, s ->
                if (index == orderVO.orderItemList.size - 1) {
                    printText("${s.productName} x${s.quantity}\n\n\n");
                } else {
                    printText("${s.productName} x${s.quantity}\n");
                }
            }
            printText("下单时间：${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderVO.paytime)}\n");
            printText("请关注\"云澎智能\"小程序\n");
            flush()
            flush()
            flush()
        }
    }


}