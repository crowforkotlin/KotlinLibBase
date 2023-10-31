@file:Suppress("MemberVisibilityCanBePrivate", "KotlinConstantConditions", "unused",
    "SpellCheckingInspection"
)

package com.crow.lib_base.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Typeface
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.crow.base.ext.log
import kotlin.math.abs
import kotlin.properties.Delegates

/**
 * ● 静态文本组件
 *
 * ● 2023/10/30 15:53
 * @author: crowforkotlin
 * @formatter:on
 */
class StaticMarView(context: Context) : View(context) {

    companion object {

        /**
         * ● 调试模式
         *
         * ● 2023-10-31 14:09:00 周二 下午
         * @author crowforkotlin
         */
        private const val DEBUG = true

        private const val FLAG_TEXT = 0x00
        private const val FLAG_TEXT_SIZE = 0x01
        private const val FLAG_GRAVITY = 0x02
        private const val FLAG_REFRESH = 0x03
        private const val FLAG_SCROLL_SPEED = 0x04

        const val GRAVITY_TOP_START = 1000
        const val GRAVITY_TOP_CENTER = 1001
        const val GRAVITY_TOP_END = 1002
        const val GRAVITY_CENTER_START = 1003
        const val GRAVITY_CENTER = 1004
        const val GRAVITY_CENTER_END = 1005
        const val GRAVITY_BOTTOM_START = 1006
        const val GRAVITY_BOTTOM_CENTER = 1007
        const val GRAVITY_BOTTOM_END = 1008

        /**
         * ● 默认更新策略：当文本发生改变触发绘制需求时会直接更新绘制视图
         *
         * ● 2023-10-31 14:09:24 周二 下午
         * @author crowforkotlin
         */
        const val STRATEGY_TEXT_UPDATE_DEFAULT = 0x00 shl 16

        /**
         * ● 懒加载更新策略：当文本发生改变时 并不会触发绘制视图的需求 只有下次动画到来 或者 切换到下一个文本才会重新绘制视图
         * 如果 文本列表只有一个元素 那么此策略将失效
         *
         * ● 2023-10-31 14:09:59 周二 下午
         * @author crowforkotlin
         */
        const val STRATEGY_TEXT_UPDATE_LAZY = 0x01 shl 16
    }

    private val mTextPaint : Paint = Paint()

    /**
     * ● 更新策略 详细可看定义声明
     *
     * ● 2023-10-31 14:07:36 周二 下午
     * @author crowforkotlin
     */
    private var mUpdateStrategy : Int = STRATEGY_TEXT_UPDATE_DEFAULT

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
     * ● 当前正在执行的视图动画，没有动画则为空
     *
     * ● 2023-10-31 14:08:33 周二 下午
     * @author crowforkotlin
     */
    private var mViewAnimator : ViewPropertyAnimator? = null

    /**
     * ● 文本列表 -- 存储屏幕上可显示的字符串集合 实现原理是 动态计算字符串宽度和 视图View做判断
     * First : 文本，Second：测量宽度
     *
     * ● 2023-10-31 14:04:26 周二 下午
     * @author crowforkotlin
     */
    private var mList : MutableList<Pair<String, Float>> = mutableListOf()

    /**
     * ● 文本列表位置 -- 设置后会触发重新绘制
     *
     * ● 2023-10-31 14:06:16 周二 下午
     * @author crowforkotlin
     */
    private var mListPosition : Int by Delegates.observable(0) { _, oldPosition, newPosition -> onVariableChanged(FLAG_REFRESH, oldPosition, newPosition) }

    /**
     * ● 动画持续时间
     *
     * ● 2023-10-31 13:59:35 周二 下午
     * @author crowforkotlin
     */
    private var mAnimationDuration: Long by Delegates.observable(1000L) { _, oldDuration, newDuration -> onVariableChanged(FLAG_SCROLL_SPEED, oldDuration, newDuration) }

    /**
     * ● 旋转角度 -- 设置后会触发重新绘制
     *
     * ● 2023-10-31 14:02:02 周二 下午
     * @author crowforkotlin
     */
    @get:FloatRange(from = 0.0, to = 360.0)
    var mRotation: Float by Delegates.observable(0f) { _, oldRotation, newRotation -> onVariableChanged(FLAG_REFRESH, oldRotation, newRotation) }

    /**
     * ● 文字大小 -- 设置后会触发重新绘制
     *
     * ● 2023-10-31 14:03:04 周二 下午
     * @author crowforkotlin
     */
    @get:FloatRange(from = 12.0, to = 768.0)
    var mTextSize: Float by Delegates.observable(13f) { _, oldSize, newSize -> onVariableChanged(FLAG_TEXT_SIZE, oldSize, newSize) }

    /**
     * ● 视图对齐方式 -- 上中下
     *
     * ● 2023-10-31 15:24:43 周二 下午
     * @author crowforkotlin
     */
    @get:IntRange(from = 1000, to = 1008)
    var mGravity: Int by Delegates.observable(GRAVITY_TOP_START) { _, oldSize, newSize -> onVariableChanged(FLAG_REFRESH, oldSize, newSize) }

    /**
     * ● 是否开启换行
     *
     * ● 2023-10-31 17:31:20 周二 下午
     * @author crowforkotlin
     */
    var mNewLineEnable: Boolean = false

    private var mAnimation = 0f

    /**
     * ● 滚动速度 --- 设置滚动速度实际上是对动画持续时间进行设置 重写SET函数，实现滚动速度设置 对动画时间进行相对的设置，设置后会触发重新绘制
     *
     * ● 2023-10-31 13:59:53 周二 下午
     * @author crowforkotlin
     */
    @IntRange(from = 1, to = 15) var mScrollSpeed: Int = 1
        set(value) {
            field = value

            // 根据 mScrollSpeed 动态调整 mAnimationDuration
            mAnimationDuration =  2000L + (400 * (15 - value))
        }

    /**
     * ● 停留时间
     *
     * ● 2023-10-31 13:59:29 周二 下午
     * @author crowforkotlin
     */
    @IntRange(from = 1, to = 255) var mResidenceTime: Int = 1

    /**
     * ● 文本内容 -- 设置后会触发重新绘制
     *
     * ● 2023-10-31 14:03:56 周二 下午
     * @author crowforkotlin
     */
    var mText: String by Delegates.observable("") { _, oldText, newText -> onVariableChanged(FLAG_TEXT, oldText, newText) }

    init {
        mScrollSpeed = 15
        mTextPaint.color = Color.WHITE
        mTextPaint.textSize = mTextSize
        mTextPaint.typeface = Typeface.MONOSPACE
    }

    /**
     * ● 绘制文本
     *
     * ● 2023-10-31 13:33:44 周二 下午
     * @author crowforkotlin
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 获取文本
        val text = mList[mListPosition]

        // 设置X和Y的坐标 ，Paint绘制的文本在descent位置 进行相对应的计算即可
        when(mGravity) {
            GRAVITY_TOP_START -> {
                val fontMetrics = mTextPaint.fontMetrics
                mTextX = 0f
                mTextY = abs(fontMetrics.ascent)
            }
            GRAVITY_TOP_CENTER -> {
                val fontMetrics = mTextPaint.fontMetrics
                mTextX = (width shr 1) - text.second / 2
                mTextY = abs(fontMetrics.ascent)
            }
            GRAVITY_TOP_END -> {
                val fontMetrics = mTextPaint.fontMetrics
                mTextX = width - text.second
                mTextY = abs(fontMetrics.ascent)
            }

            GRAVITY_CENTER_START -> {
                mTextX = 0f
                mTextY = (height shr 1) +calculateBaselineOffsetY(mTextPaint.fontMetrics)
            }
            GRAVITY_CENTER -> {
                mTextX = (width shr 1) - text.second / 2
                mTextY = (height shr 1) + calculateBaselineOffsetY(mTextPaint.fontMetrics)
            }
            GRAVITY_CENTER_END -> {
                mTextX = width - text.second
                mTextY = (height shr 1) + calculateBaselineOffsetY(mTextPaint.fontMetrics)
            }

            GRAVITY_BOTTOM_START -> {
                mTextX = 0f
                mTextY = height - calculateBaselineOffsetY(mTextPaint.fontMetrics)
            }
            GRAVITY_BOTTOM_CENTER -> {
                mTextX =  (width shr 1) - text.second / 2
                mTextY = height - calculateBaselineOffsetY(mTextPaint.fontMetrics)
            }
            GRAVITY_BOTTOM_END -> {
                mTextX = width - text.second
                mTextY = height - calculateBaselineOffsetY(mTextPaint.fontMetrics)
            }
        }

        // 绘制
        canvas.drawText(text.first, 0, text.first.length, mTextX, mTextY, mTextPaint)

        // DEBUG 模式
        if (DEBUG) {
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
     * ● 动态计算可容纳字符个数获取文本列表
     *
     * ● 2023-10-31 13:34:58 周二 下午
     * @author crowforkotlin
     */
    private fun getTextLists(originText: String): MutableList<Pair<String, Float>> {
        var textStringWidth = 0f
        var textStringBuilder = StringBuilder()
        val textList: MutableList<Pair<String, Float>> = mutableListOf()
        val textMaxIndex = originText.length - 1
        mTextPaint.textSize = mTextSize
        originText.forEachIndexed { index, char ->
            val textWidth = mTextPaint.measureText(char.toString(), 0, 1)
            textStringWidth += textWidth
            if (textStringWidth <= layoutParams.width) {
                textStringBuilder.append(char)
                if (index == textMaxIndex) {
                    textList.add(textStringBuilder.toString() to textStringWidth)
                    textStringWidth = 0f
                }
            } else {
                textList.add(textStringBuilder.toString() to textStringWidth - textWidth)
                textStringBuilder = StringBuilder()
                textStringBuilder.append(char)
                if (index == textMaxIndex) {
                    textList.add(textStringBuilder.toString() to textStringWidth)
                } else {
                    textStringWidth = textWidth
                }
            }
        }
        return textList
    }

    /**
     * ● 值发生变化 执行对于的Logic
     *
     * ● 2023-10-31 14:14:18 周二 下午
     * @author crowforkotlin
     */
    private fun<T : Any> onVariableChanged(flag: Int, oldValue: T, newValue: T) {
        when(flag) {
            FLAG_REFRESH -> {
                if (oldValue != newValue) { invalidate() }
            }
            FLAG_TEXT -> {
                if (oldValue != newValue) {
                    val texts = getTextLists(newValue as String)
                    mList.clear()
                    mList.addAll(texts)
                    val size = mList.size
                    if (size <= mListPosition) { mListPosition = size - 1 }
                    if (mUpdateStrategy == STRATEGY_TEXT_UPDATE_DEFAULT || size == 1) { invalidate() }
                }
            }
            FLAG_TEXT_SIZE -> {
                if (mTextPaint.textSize != oldValue) {
                    val texts = getTextLists(mText)
                    mTextPaint.textSize = newValue as Float
                    mListPosition = 0
                    mList.clear()
                    mList.addAll(texts)
                    invalidate()
                }
            }
            FLAG_SCROLL_SPEED -> {
                mViewAnimator?.also { animator ->
                    animator.cancel()
                    animator.start()
                }
            }
            FLAG_GRAVITY -> {
                if (oldValue != newValue) {

                    mTextPaint.textAlign = newValue as Paint.Align
                    invalidate()
                }
            }
        }
    }

    fun animateDx() {
        mViewAnimator = animate()
            .setDuration(mAnimationDuration)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // 动画结束后的处理
                }
            })
            .translationX(mTextX + layoutParams.width)
            .withEndAction {
                // 动画结束后的处理
                translationX = -width.toFloat()
                mList.log()
                if (mListPosition < mList.size - 1) {
                    mListPosition ++
                } else {
                    mListPosition = 0
                }
                animateDx()
            }
            .withStartAction {
                // 动画开始时的处理
            }
    }

    fun cancelAnimator() {
        mViewAnimator?.also { animator ->
            animator.cancel()
            translationX = mTextX
        }
    }
}