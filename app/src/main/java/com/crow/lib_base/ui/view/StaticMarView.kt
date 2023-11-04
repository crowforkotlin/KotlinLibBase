@file:Suppress("MemberVisibilityCanBePrivate", "KotlinConstantConditions", "unused",
    "SpellCheckingInspection"
)

package com.crow.lib_base.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.view.View
import androidx.annotation.IntRange
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.properties.Delegates

/**
 * ● 静态文本组件
 *
 * ● 2023/10/30 15:53
 * @author: crowforkotlin
 * @formatter:on
 */
class StaticMarView(context: Context) : View(context), IMarExt {

    companion object {

        private const val FLAG_REFRESH = 0x01

    }

    /**
     * ● ChildView 文本画笔
     *
     * ● 2023-11-03 15:27:29 周五 下午
     * @author crowforkotlin
     */
    lateinit var mTextPaint : Paint

    /**
     * ● 文本X坐标
     *
     * ● 2023-10-31 14:08:08 周二 下午
     * @author crowforkotlin
     */
    private var mTextX : Float = 0f

    /**
     * ● 文本Y坐标
     *
     * ● 2023-10-31 16:18:51 周二 下午
     * @author crowforkotlin
     */
    private var mTextY : Float = 0f

    /**
     * ● 文本列表 -- 存储屏幕上可显示的字符串集合 实现原理是 动态计算字符串宽度和 视图View做判断
     * First : 文本，Second：测量宽度
     *
     * ● 2023-10-31 14:04:26 周二 下午
     * @author crowforkotlin
     */
    var mList : MutableList<Pair<String, Float>> = mutableListOf()

    /**
     * ● 文本列表位置 -- 设置后会触发重新绘制
     *
     * ● 2023-10-31 14:06:16 周二 下午
     * @author crowforkotlin
     */
    var mListPosition : Int by Delegates.observable(0) { _, oldPosition, newPosition -> onVariableChanged(FLAG_REFRESH, oldPosition, newPosition, skipSameCheck = true) }

    /**
     * ● 视图对齐方式 -- 上中下
     *
     * ● 2023-10-31 15:24:43 周二 下午
     * @author crowforkotlin
     */
    @get:IntRange(from = 1000, to = 1008)
    var mGravity: Int by Delegates.observable(StaticMarLayout.GRAVITY_TOP_START) { _, oldSize, newSize -> onVariableChanged(FLAG_REFRESH, oldSize, newSize) }

    /**
     * ● 是否开启换行
     *
     * ● 2023-10-31 17:31:20 周二 下午
     * @author crowforkotlin
     */
    var mMultiLineEnable: Boolean by Delegates.observable(false) { _, oldValue, newValue -> onVariableChanged(FLAG_REFRESH, oldValue, newValue) }

    /**
     * ● 绘制文本
     *
     * ● 2023-10-31 13:33:44 周二 下午
     * @author crowforkotlin
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 画笔未初始化
        if (!::mTextPaint.isInitialized || mList.isEmpty()) return

        // 获取文本
        val text = if (mListPosition > mList.size - 1) { mList.last() } else mList[mListPosition]

        // 设置X和Y的坐标 ，Paint绘制的文本在descent位置 进行相对应的计算即可
        when(mGravity) {
            StaticMarLayout.GRAVITY_TOP_START -> {
                val fontMetrics = mTextPaint.fontMetrics
                mTextY = abs(fontMetrics.ascent)
                drawTopText(canvas, text.first) { mTextX = 0f }
            }
            StaticMarLayout.GRAVITY_TOP_CENTER -> {
                val fontMetrics = mTextPaint.fontMetrics
                mTextY = abs(fontMetrics.ascent)
                drawTopText(canvas, text.first) { mTextX = (width shr 1) - it / 2 }
            }
            StaticMarLayout.GRAVITY_TOP_END -> {
                val fontMetrics = mTextPaint.fontMetrics
                mTextY = abs(fontMetrics.ascent)
                drawTopText(canvas, text.first) { mTextX = width - it }
            }
            StaticMarLayout.GRAVITY_CENTER_START -> {
                drawCenterText(canvas, text.first) { mTextX = 0f }
            }
            StaticMarLayout.GRAVITY_CENTER -> {
                drawCenterText(canvas, text.first) { mTextX = (width shr 1) - it / 2 }
            }
            StaticMarLayout.GRAVITY_CENTER_END -> {
                drawCenterText(canvas, text.first) { mTextX = width - it }
            }
            StaticMarLayout.GRAVITY_BOTTOM_START -> {
                mTextY = height - calculateBaselineOffsetY(mTextPaint.fontMetrics)
                drawBottomText(canvas, text.first) { mTextX = 0f }
            }
            StaticMarLayout.GRAVITY_BOTTOM_CENTER -> {
                mTextY = height - calculateBaselineOffsetY(mTextPaint.fontMetrics)
                drawBottomText(canvas, text.first) { mTextX =  (width shr 1) - it /  2 }
            }
            StaticMarLayout.GRAVITY_BOTTOM_END -> {
                mTextY = height - calculateBaselineOffsetY(mTextPaint.fontMetrics)
                drawBottomText(canvas, text.first) { mTextX = width - it }
            }
        }

        // DEBUG 模式
        if (StaticMarLayout.DEBUG) {
            val paintColor = mTextPaint.color
            // 绘制2条参考线
            mTextPaint.color = Color.RED
            canvas.drawLine(0f, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat(), mTextPaint)

            mTextPaint.color = Color.YELLOW
            canvas.drawLine(0f, mTextY, width.toFloat(), mTextY, mTextPaint)

            mTextPaint.color = Color.GREEN
            val ascentY = mTextY - abs(mTextPaint.fontMetrics.ascent)
            canvas.drawLine(0f, ascentY, width.toFloat(), ascentY, mTextPaint)

            mTextPaint.color = Color.BLUE
            mTextPaint.style = Paint.Style.STROKE
            canvas.drawRect(0f, 0f, layoutParams.width.toFloat(), layoutParams.height.toFloat(), mTextPaint)

            mTextPaint.color = Color.parseColor("#9575cd")
            mTextPaint.style = Paint.Style.FILL
            mTextPaint.alpha = 80
            canvas.drawRect(1f, 1f, layoutParams.width.toFloat() - 1, layoutParams.height.toFloat() - 1, mTextPaint)
            mTextPaint.color = paintColor
        }
    }

    /**
     * ● 绘制顶部文本
     *
     * ● 2023-11-04 17:53:43 周六 下午
     * @author crowforkotlin
     */
    private inline fun drawTopText(canvas: Canvas, text: String, onIniTextX: (Float) -> Unit) {
        if (mMultiLineEnable) {
            val textHeight = ceil(getTextHeight(mTextPaint.fontMetrics))
            val maxLine = (measuredHeight / textHeight).toInt()
            var pos = mListPosition * maxLine
            repeat(maxLine) {
                if (pos < mList.size) {
                    val currentText = mList[pos]
                    onIniTextX(currentText.second)
                    canvas.drawText(currentText.first, mTextX, mTextY, mTextPaint)
                }
                else return@repeat
                pos ++
                mTextY += textHeight
            }
        } else {
            canvas.drawText(text, 0, text.length, mTextX, mTextY, mTextPaint)
        }
    }

    /**
     * ● 绘制中心文本
     *
     * ● 2023-11-04 17:54:09 周六 下午
     * @author crowforkotlin
     */
    private inline fun drawCenterText(canvas: Canvas, text: String, onIniTextX: (Float) -> Unit) {
        if (mMultiLineEnable) {
            val screenHeightHalf = height shr 1
            val fontMetrics = mTextPaint.fontMetrics
            val textHeight = ceil(getTextHeight(fontMetrics))
            val textHeightHalf = textHeight / 2
            val validSpace = screenHeightHalf - textHeightHalf
            val topCount = (validSpace / textHeight).toInt()
            val bottomCount = (validSpace / textHeight).toInt()
            val totalCount = topCount + bottomCount + 1
            val centetY = screenHeightHalf + calculateBaselineOffsetY(fontMetrics)
            mTextY = centetY - textHeight * topCount
            val listSize = mList.size
            var startPos =mListPosition * totalCount
            repeat(totalCount) {
                if (startPos < listSize) {
                    val currentText = mList[startPos]
                    onIniTextX(currentText.second)
                    canvas.drawText(currentText.first, mTextX, mTextY, mTextPaint)
                } else {
                    return@repeat
                }
                startPos ++
                mTextY += textHeight
            }
        } else {
            mTextY = (height shr 1) + calculateBaselineOffsetY(mTextPaint.fontMetrics)
            canvas.drawText(text, 0, text.length, mTextX, mTextY, mTextPaint)
        }
    }

    /**
     * ● 绘制底部文本
     *
     * ● 2023-11-04 17:54:00 周六 下午
     * @author crowforkotlin
     */
    private inline fun drawBottomText(canvas: Canvas, text: String, onIniTextX: (Float) -> Unit) {
        if (mMultiLineEnable && mList.size > 1) {
            val textHeight = ceil(getTextHeight(mTextPaint.fontMetrics))
            val maxLine = (measuredHeight / textHeight).toInt()
            val listSize = mList.size
            val startPos = (mListPosition + 1) * maxLine
            val endPos = mListPosition * maxLine
            var pos = if (listSize >= startPos) startPos - 1 else listSize - 1
            repeat(maxLine) {
                if (pos >= endPos) {
                    val currentText = mList[pos]
                    onIniTextX(currentText.second)
                    canvas.drawText(currentText.first, mTextX, mTextY, mTextPaint)
                }
                else return@repeat
                pos --
                mTextY -= textHeight
            }
        } else {
            canvas.drawText(text, 0, text.length, mTextX, mTextY, mTextPaint)
        }
    }

    /**
     * ● 计算 baseline 的相对文字中心的偏移量
     *
     * ● 2023-10-31 13:34:50 周二 下午
     * @author crowforkotlin
     */
    private fun calculateBaselineOffsetY(fontMetrics: FontMetrics): Float {

        val ascent = fontMetrics.ascent
        val descent = fontMetrics.descent

        return -ascent / 2 - descent / 2
    }

    /**
     * ● 值发生变化 执行对于的Logic
     *
     * ● 2023-10-31 14:14:18 周二 下午
     * @author crowforkotlin
     */
    private fun<T : Any> onVariableChanged(flag: Int, oldValue: T?, newValue: T?, skipSameCheck: Boolean = false) {
        if (oldValue == newValue && !skipSameCheck) return
        when(flag) {
            FLAG_REFRESH -> { if (mList.isNotEmpty()) invalidate() }
        }
    }
}