package android_serialport_api.cardwriteread.jiedan

import android.content.Intent
import android.os.CountDownTimer
import android.os.Message
import android_serialport_api.cardwriteread.Constant
import android_serialport_api.cardwriteread.R
import android_serialport_api.cardwriteread.helper.SoundHelper
import android_serialport_api.cardwriteread.jiedan.api.response.OrderVO
import android_serialport_api.cardwriteread.jiedan.net.Req
import android_serialport_api.cardwriteread.jiedan.net.Req.loginJieDan
import android_serialport_api.cardwriteread.jiedan.print.PrinterHelper
import com.yp.baselib.utils.SPUtils
import android_serialport_api.cardwriteread.meal.BusUtils
import android_serialport_api.cardwriteread.util.SharedUtil
import com.yp.baselib.annotation.Bus
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseFragment
import com.yp.baselib.utils.AppUtils
import com.yp.baselib.ex.RvEx
import com.yp.baselib.utils.TimerUtils
import kotlinx.android.synthetic.main.frag_jie_dan.*
import kotlinx.android.synthetic.main.header.*
import org.greenrobot.eventbus.Subscribe
import java.util.*
import kotlin.collections.ArrayList

/**
 * 接单主界面
 */
@Bus
@LayoutId(R.layout.frag_jie_dan)
class JieDanFragment : BaseFragment(), RvEx {

    @Subscribe
    fun handle(msg:Message){
        if(msg.what == 0x123){
            rvJieDan.getChildAt(msg.obj.toString().toInt()).tv(R.id.btnPrint).text = "再次打印"
        }
    }

    private var orderList = ArrayList<OrderVO>()
    private lateinit var task: CountDownTimer

    /**
     * 用于轮询新订单
     */
    lateinit var timer: Timer

    var isAutoPrint = false

    override fun init() {

        val username = SharedUtil.getInstatnce().getShared(activity).getString("username", "")
        val password = SharedUtil.getInstatnce().getShared(activity).getString("password", "")

        rvJieDan.wrap.rvAdapter(orderList,
                { h, p ->
                    // 订单号
                    h.tv(R.id.tvDingDanHao).text = "订单号：${orderList[p].orderNo}"
                    // 时间戳
                    h.tv(R.id.tvDate).text = orderList[p].paytime.fmtDate("yyyy-MM-dd HH:mm:ss")
                    // 取餐号
                    h.tv(R.id.tvQuCanHao).text = "取餐号：" + orderList[p].serial
                    // 价格
                    h.tv(R.id.tvPrice).text = "￥${orderList[p].realfee.toBigDecimal().divide(100.toBigDecimal())}"
                    // 打印
                    if(SPUtils.get(activity!!, orderList[p].orderNo, "").toString() == "1"){
                        h.tv(R.id.btnPrint).text = "再次打印"
                    } else {
                        h.tv(R.id.btnPrint).text = "打印"
                    }
                    // 查看详情
                    h.v(R.id.btnViewDetail).click {
                        startActivity(Intent(it.context, JieDanDetailActivity::class.java)
                                .putExtra("bean", orderList[p]).putExtra("pos", p) )
                    }
                    h.tv(R.id.btnPrint).click{
                        PrinterHelper.getInstance().printTicket(orderList[p])
                        h.tv(R.id.btnPrint).text = "再次打印"
                        SPUtils.put(activity!!, orderList[p].orderNo, "1")
                    }
                }, R.layout.item_jie_dan)

        loginJieDan(activity!!, username!!, password!!, {
            tvShopName?.text = "${it.data.shopName}(${Constant.shopName})"
        }, {
            orderList.addAll(it.data)
            rvJieDan.update()
        })

        task = TimerUtils.countdown(Long.MAX_VALUE, 1000, {
            tvDate.text = System.currentTimeMillis().fmtDate("yyyy-MM-dd HH:mm:ss")
        })
        task.start()

        tvVersion.text = "V ${AppUtils.getVersionName(activity)}"

        btnKuaiSuShouYin.click {
            MainActHelper.getInstance().hideFragment(true)
        }

        btnDianCanMoShi.click {
            MainActHelper.getInstance().hideFragment(false)
        }

        ivBack.click {
            MainActHelper.getInstance().hide()
        }

        timer = TimerUtils.schedule(1000, 5000){
            Req.getNewOrder(activity!!){
                if(it.isNotEmpty()){
                    BusUtils.sendMsg(0x123)
                    orderList.addAll(0, it)
                    if(isAutoPrint){
                        it.forEach {
                            SPUtils.put(activity!!, it.orderNo, "1")
                            PrinterHelper.getInstance().printTicket(it)
                        }
                    }
                    rvJieDan.update()
                    SoundHelper.getInstance().play(activity!!, R.raw.neworder)
                }
            }
        }

        btnAutoPrint.setOnCheckedChangeListener { buttonView, isChecked ->
            isAutoPrint = !isAutoPrint
            buttonView.isChecked = isChecked
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        task.cancel()
        timer.cancel()
        SoundHelper.getInstance().release()
    }

}