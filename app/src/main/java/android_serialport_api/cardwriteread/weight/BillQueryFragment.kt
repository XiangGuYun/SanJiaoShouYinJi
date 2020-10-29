package android_serialport_api.cardwriteread.weight

import android_serialport_api.cardwriteread.R
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseDialogFragment
import kotlinx.android.synthetic.main.frag_bill_query.*

/**
 * 人脸查询账单
 */
@LayoutId(R.layout.frag_bill_query)
class BillQueryFragment : BaseDialogFragment() {

    override fun setDialogSize(): Pair<Int, Int> {
        return 960.dp to 480.dp
    }

    override fun init() {
        ivClose.click {
            dismiss()
        }

        rvOrder.wrap.rvAdapter(listOf(1),
                {
                    holder, pos ->
                },
                R.layout.item_order
        )

        rvOrderDetail.wrap.rvAdapter(listOf(1,1,1),
                {
                    holder, pos ->
                }, R.layout.item_order_detail)

    }

}