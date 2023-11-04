package com.crow.lib_base.ui.view

import android.graphics.Paint
import kotlin.math.abs

interface IMarExt {

    fun getTextHeight(fontMetrics: Paint.FontMetrics) : Float {
        return abs(fontMetrics.ascent) + fontMetrics.descent
    }
}