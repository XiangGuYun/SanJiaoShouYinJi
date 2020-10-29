package com.yp.baselib.utils

import android.view.Gravity
import android.widget.Toast
import com.yp.baselib.base.BaseApplication

class ToastUtils {

    fun toast(str: String,
              isLong: Boolean = false,
              gravity: Int = Gravity.BOTTOM,
              xOffSet: Int = 0,
              yOffset: Int = 0)
    {
        Toast.makeText(BaseApplication.getInstance().context, str,
                if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
                .apply {
                    setGravity(gravity, xOffSet, yOffset)
                }.show()
    }

}