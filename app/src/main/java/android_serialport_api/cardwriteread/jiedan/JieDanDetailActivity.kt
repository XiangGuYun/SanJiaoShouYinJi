package android_serialport_api.cardwriteread.jiedan

import android.os.Bundle
import android_serialport_api.cardwriteread.R
import android_serialport_api.cardwriteread.jiedan.api.response.MeshOrderItemVO
import android_serialport_api.cardwriteread.jiedan.api.response.OrderVO
import android_serialport_api.cardwriteread.jiedan.print.PrinterHelper
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.utils.SPUtils
import com.yp.baselib.base.BaseActivity
import com.yp.baselib.utils.BusUtils
import com.yp.baselib.ex.RvEx
import kotlinx.android.synthetic.main.frag_jie_dan_detail.*

/**
 * 订单详情
 */
@LayoutId(R.layout.frag_jie_dan_detail)
class JieDanDetailActivity : BaseActivity(), RvEx {

    override fun init(bundle: Bundle?) {
        val bean = extraSerial("bean") as OrderVO
        // 取餐号
        tvQuCanHao.text = bean.serial
        // 订单号
        tvDingDanHao.text = bean.orderNo
        // 订单时间
        tvDingDanShiJian.text = bean.paytime.fmtDate("yyyy-MM-dd HH:mm:ss")
        // 用户名称
        tvUsername.text = bean.customerName
        // 手机号
        tvPhone.text = bean.customerPhone
        val listDetail = ArrayList(bean.orderItemList)
        listDetail.add(MeshOrderItemVO())
        listDetail.add(MeshOrderItemVO())
        rvDetail.wrap.rvMultiAdapter(listDetail,
                { h, p ->
                    when (p) {
                        listDetail.size - 1 -> {
                            h.tv(R.id.tvPricePay).text =
                                    "￥ ${bean.totalfee.toBigDecimal().divide(100.toBigDecimal())}"
                        }
                        listDetail.size - 2 -> {
                            h.tv(R.id.tvYouHui).text =
                                    "￥ ${bean.totalfee.toBigDecimal().subtract(bean.realfee.toBigDecimal()).toDouble()}"
                        }
                        else -> {
                            h.tv(R.id.tvFoodName).text = listDetail[p].productName + "    x" + listDetail[p].quantity
                            h.tv(R.id.tvFoodPrice).text = "￥ ${listDetail[p].price.toBigDecimal().divide(100.toBigDecimal())}"
                        }
                    }
                }, { p ->
            when (p) {
                listDetail.size - 1 -> 2
                listDetail.size - 2 -> 1
                else -> 0
            }
        }, R.layout.item_dan_detail, R.layout.item_dan_detail1, R.layout.item_dan_detail2)

        if(SPUtils.get(this, bean.orderNo, "").toString() == "1"){
            btnPrint.text = "再次打印"
        } else {
            btnPrint.text = "打印"
        }

        btnPrint.click {
            if(PrinterHelper.getInstance().isConnected){
                PrinterHelper.getInstance().printTicket(bean)
                btnPrint.text = "再次打印"
                SPUtils.put(this, bean.orderNo, "1")
                BusUtils.post(0x123, extraInt("pos", 0))
            }
        }

        ivBack.click {
            finish()
        }
    }
}