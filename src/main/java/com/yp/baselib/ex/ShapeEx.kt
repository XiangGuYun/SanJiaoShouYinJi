package com.yp.baselib.ex

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View

interface ShapeEx {

    /**
     * 在代码中给View设置shape的背景
     */
    fun View.setShape(cornerRadius: Float,
                      color: Int = Color.WHITE,
                      shapeType: Int = GradientDrawable.RECTANGLE) {
        val drawable = GradientDrawable()
        drawable.cornerRadius = cornerRadius
        drawable.setColor(color)
        drawable.shape = shapeType
    }


}