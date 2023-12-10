/*
 * Copyright 2022 VastGui guihy2019@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crow.base.view.attr_mask_layout

import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.get
import com.ave.vastgui.tools.view.masklayout.MaskAnimation
import kotlin.math.hypot

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/20
// Description: 
// Documentation:
// Reference:

/**
 * Mask Layout.
 *
 * @since 0.5.6
 */
class MaskLayout @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private var mAnimationRunning: Boolean = false
    private var mTargetMaskRadius: Float = 0f

    var mMaskCenterX: Float = context.resources.displayMetrics.widthPixels / 2f

    var mMaskCenterY: Float = context.resources.displayMetrics.heightPixels / 2f

    var mMaskDuration: Long = 1000L
        set(value) {
            field = value.coerceAtLeast(0L)
        }

    var mMaskTimeInterpolator: TimeInterpolator =
        AccelerateDecelerateInterpolator()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mTargetMaskRadius =
            hypot(measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    /**
     * Active mask animation.
     *
     * @since 0.5.6
     */
    fun activeMask(animation: MaskAnimation, onMaskComplete: () -> Unit) {
        if (mAnimationRunning) {
            if (childCount != 0) {
                if (get(0) is MaskView) {
                    removeView(get(0))
                }
            }
        }
        val maskView = MaskView(context, attrs).apply {
            mBitmap = viewSnapshot(this@MaskLayout)
            mTargetMaskRadius = this@MaskLayout.mTargetMaskRadius
            mMaskDuration = this@MaskLayout.mMaskDuration
            mMaskTimeInterpolator = this@MaskLayout.mMaskTimeInterpolator
            mMaskCenterX = this@MaskLayout.mMaskCenterX
            mMaskCenterY = this@MaskLayout.mMaskCenterY
        }
        addView(maskView)
        mAnimationRunning = true
        onMaskComplete()
        maskView.activeMask(animation) {
            removeView(maskView)
            mAnimationRunning = false
        }

    }

    /**
     * Snapshot option.
     *
     * @since 0.5.4
     */
    data class SnapshotOption(val width: Int, val height: Int)

    /**
     * View snapshot.
     *
     * @param view The [View] or [ViewGroup] that needs to get snapshot.
     * @param option If the view is not inflated, you need to specify the size
     *     through this parameter.
     * @since 0.5.4
     */
    @JvmOverloads
    fun viewSnapshot(view: View, option: SnapshotOption? = null): Bitmap {
        if (null != option) {
            val measuredWidth = MeasureSpec.makeMeasureSpec(option.width, View.MeasureSpec.EXACTLY)
            val measuredHeight = MeasureSpec.makeMeasureSpec(option.height, View.MeasureSpec.EXACTLY)
            view.measure(measuredWidth, measuredHeight)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            viewSnapshotApi28Impl(view)
        } else {
            viewSnapshotImpl(view)
        }
    }

    /** @since 0.5.4 */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun viewSnapshotApi28Impl(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    /** @since 0.5.4 */
    @Suppress("DEPRECATION")
    private fun viewSnapshotImpl(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        var drawingCache = view.drawingCache
        drawingCache = Bitmap.createBitmap(drawingCache!!)
        view.isDrawingCacheEnabled = false
        return drawingCache
    }

    init {
        mMaskDuration = 1000L
        mMaskTimeInterpolator = AccelerateDecelerateInterpolator()
    }
}