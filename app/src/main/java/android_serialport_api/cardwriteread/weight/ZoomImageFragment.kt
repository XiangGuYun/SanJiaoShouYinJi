package android_serialport_api.cardwriteread.weight

import android_serialport_api.cardwriteread.R
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_zoom_image.*

@LayoutId(R.layout.fragment_zoom_image)
class ZoomImageFragment : BaseDialogFragment() {

    override fun setDialogSize(): Pair<Int, Int> {
        return 500.dp to 500.dp
    }

    override fun init() {
    }
}