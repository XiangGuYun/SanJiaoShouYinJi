package android_serialport_api.cardwriteread.weight

import android.os.CountDownTimer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android_serialport_api.cardwriteread.Constant
import android_serialport_api.cardwriteread.R
import android_serialport_api.cardwriteread.jiedan.MainActHelper
import android_serialport_api.cardwriteread.view.MainActivity
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseFragment
import com.yp.baselib.ex.RvEx
import com.yp.baselib.helper.SoundHelper
import com.yp.baselib.utils.AppUtils
import com.yp.baselib.utils.BusUtils
import kotlinx.android.synthetic.main.frag_weight.*
import kotlinx.android.synthetic.main.frag_weight.btnDianCanMoShi
import kotlinx.android.synthetic.main.frag_weight.btnKuaiSuShouYin
import kotlinx.android.synthetic.main.header.*

/**
 * 称重模式
 * @author YeXuDong
 */
@LayoutId(R.layout.frag_weight)
class WeightFragment : BaseFragment(), RvEx {
    private lateinit var task: CountDownTimer

    lateinit var dialogQueryBill: BillQueryFragment
    lateinit var dialogShowFace: ZoomImageFragment

    override fun init() {
        doDialog()
        doHeader()
        doLeftViewPager()
        doRightButtons()
        doBottomButtons()
    }

    private fun doDialog() {
        dialogQueryBill = BillQueryFragment()
        dialogShowFace = ZoomImageFragment()
    }

    /**
     * 处理底部按钮区域
     */
    private fun doBottomButtons() {
        btnKuaiSuShouYin.click {
            MainActHelper.getInstance().hideFragment(true)
        }

        btnDianCanMoShi.click {
            MainActHelper.getInstance().hideFragment(false)
        }
    }

    /**
     * 处理右侧按钮区域
     */
    private fun doRightButtons() {
        btnFaceQueryBill.click {
            dialogQueryBill.show(activity!!)
        }

        btnFacePay.click {
            act<MainActivity>().presenter.apply {
                SoundHelper.getInstance().play(activity!!, R.raw.pleasepay)
                setMoney(100f)
                setisFacePay(true)
                updateViceScreenDisplay()
                BusUtils.post(0x7793)
                uploadPayState(true) //更新支付状态
                enableBackgroundUpdate(false) //停止后台更新
            }
        }
    }

    /**
     * 处理顶部标题栏
     */
    private fun doHeader() {
        tvShopName?.text = Constant.shopName

        tvVersion.text = "V ${AppUtils.getVersionName(activity)}"

        ivBack.click {
            MainActHelper.getInstance().hide()
        }

        task = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onFinish() {}
            override fun onTick(millisUntilFinished: Long) {
                tvDate.text = System.currentTimeMillis().fmtDate("yyyy-MM-dd HH:mm:ss")
            }
        }
        task.start()
    }

    /**
     * 处理左侧“已处理订单”和“未处理订单”
     */
    private fun doLeftViewPager() {
        val tabNames = listOf("未支付订单", "已支付订单")

        vpWeight.apply {
            setViewAdapter(tabNames.size) {
                (LayoutInflater.from(activity!!).inflate(R.layout.rv_weight, null) as RecyclerView).apply {
                    wrap.rvMultiAdapter((1..10).toList(), { h, p ->
                        h.iv(R.id.ivFace).click {
                            dialogShowFace.show(activity!!)
                        }
                    }, {
                        0
                    }, R.layout.item_unpay, R.layout.item_payed)
                }
            }
            bindTabLayout(tlWeight, tabNames.size, false) { tab, i ->
                tab?.text = tabNames[i]
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        task.cancel()
    }
}