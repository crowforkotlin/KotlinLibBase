@file:Suppress("unused", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")

package com.crow.lib_base.ui.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.crow.base.ext.log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.properties.Delegates


/**
 * ● 静态文本组件 -- 布局
 *
 * ● 2023/10/30 15:53
 * @author crowforkotlin
 * @formatter:on
 */
class StaticMarLayout(context: Context) : FrameLayout(context), IMarExt  {

    companion object {

        /**
         * ● 调试模式
         *
         * ● 2023-10-31 14:09:00 周二 下午
         * @author crowforkotlin
         */
        internal const val DEBUG = false

        /**
         * ● 缓存VIEW个数 勿动改了后会出问题
         *
         * ● 2023-11-02 15:20:10 周四 下午
         * @author crowforkotlin
         */
        private const val REQUIRED_CACHE_SIZE = 2

        private const val FLAG_TEXT = 0x00
        private const val FLAG_TEXT_SIZE = 0x01
        private const val FLAG_CHILD_REFRESH = 0x03
        private const val FLAG_LAYOUT_REFRESH = 0x04
        private const val FLAG_SCROLL_SPEED = 0x05
        private const val FLAG_GRAVITY = 0x06
        private const val FLAG_NEWLINE = 0x07

        const val ANIMATION_DEFAULT = 2000
        const val ANIMATION_MOVE_X = 2001
        const val ANIMATION_MOVE_Y = 2002
        const val ANIMATION_FADE = 2003
        const val ANIMATION_FADE_SYNC = 2004

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

    /**
     * ● 文本画笔
     *
     * ● 2023-11-01 09:51:41 周三 上午
     * @author crowforkotlin
     */
    private val mTextPaint : Paint = Paint()

    /**
     * ● 文本列表 -- 存储屏幕上可显示的字符串集合 实现原理是 动态计算字符串宽度和 视图View做判断
     * First : 文本，Second：测量宽度
     *
     * ● 2023-10-31 14:04:26 周二 下午
     * @author crowforkotlin
     */
    private val mList : MutableList<Pair<String, Float>> = mutableListOf()

    /**
     * ● 动画持续时间
     *
     * ● 2023-10-31 13:59:35 周二 下午
     * @author crowforkotlin
     */
    private var mAnimationDuration: Long = 8000L

    /**
     * ● 当前正在执行的视图动画，没有动画则为空
     *
     * ● 2023-10-31 14:08:33 周二 下午
     * @author crowforkotlin
     */
    private var mViewAnimatorSet : AnimatorSet? = null

    /**
     * ● 更新策略 详细可看定义声明
     *
     * ● 2023-10-31 14:07:36 周二 下午
     * @author crowforkotlin
     */
    private var mUpdateStrategy : Int = STRATEGY_TEXT_UPDATE_DEFAULT

    /**
     * ● 动画任务
     *
     * ● 2023-10-31 18:10:59 周二 下午
     * @author crowforkotlin
     */
    private var mAnimationJob: Job? = null

    /**
     * ● 上一个动画值
     *
     * ● 2023-11-02 17:16:40 周四 下午
     * @author crowforkotlin
     */
    private var mLastAnimation = -1

    /**
     * ● UI 协程
     *
     * ● 2023-10-31 18:09:55 周二 下午
     * @author crowforkotlin
     */
    private val mViewScope = MainScope()

    /**
     * ● 缓存StaticMarView （默认两个）
     *
     * ● 2023-11-01 09:53:01 周三 上午
     * @author crowforkotlin
     */
    private val mCacheViews = ArrayList<StaticMarView>(REQUIRED_CACHE_SIZE)

    /**
     * ● 当前视图的位置
     *
     * ● 2023-11-01 10:12:30 周三 上午
     * @author crowforkotlin
     */
    private var mCurrentViewPos: Int by Delegates.observable(0) { _, oldViewPos, newViewPos -> onVariableChanged(FLAG_LAYOUT_REFRESH, oldViewPos, newViewPos) }

    /**
     * ● 文本列表位置 -- 设置后会触发重新绘制
     *
     * ● 2023-10-31 14:06:16 周二 下午
     * @author crowforkotlin
     */
    private var mListPosition : Int by Delegates.observable(0) { _, oldPosition, newPosition -> onVariableChanged(FLAG_CHILD_REFRESH, oldPosition, newPosition) }

    /**
     * ● 多行文本（换行）位置
     *
     * ● 2023-11-03 18:19:24 周五 下午
     * @author crowforkotlin
     */
    private var mMultipleLinePos: Int by Delegates.observable(0) { _, oldPosition, newPosition -> onVariableChanged(FLAG_CHILD_REFRESH, oldPosition, newPosition)}

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
    var mGravity: Int by Delegates.observable(GRAVITY_TOP_START) { _, oldSize, newSize -> onVariableChanged(FLAG_GRAVITY, oldSize, newSize) }

    /**
     * ● 滚动速度 --- 设置滚动速度实际上是对动画持续时间进行设置 重写SET函数，实现滚动速度设置 对动画时间进行相对的设置，设置后会触发重新绘制
     *
     * ● 2023-10-31 13:59:53 周二 下午
     * @author crowforkotlin
     */
    @get:IntRange(from = 1, to = 15)
    var mScrollSpeed: Int by Delegates.observable(1) { _, oldSpeed, newSpeed -> onVariableChanged(FLAG_SCROLL_SPEED, oldSpeed, newSpeed) }

    /**
     * ● 文本内容 -- 设置后会触发重新绘制
     *
     * ● 2023-10-31 14:03:56 周二 下午
     * @author crowforkotlin
     */
    var mText: String by Delegates.observable("") { _, oldText, newText -> onVariableChanged(FLAG_TEXT, oldText, newText) }

    /**
     * ● 是否开启换行
     *
     * ● 2023-10-31 17:31:20 周二 下午
     * @author crowforkotlin
     */
    var mMultipleLineEnable: Boolean by Delegates.observable(false) { _, oldValue, newValue -> onVariableChanged(FLAG_NEWLINE, oldValue, newValue) }

    /**
     * ● 动画模式（一般是默认）
     *
     * ● 2023-10-31 18:06:32 周二 下午
     * @author crowforkotlin
     */
    var mAnimationMode = ANIMATION_DEFAULT

    /**
     * ● 是否启用单行动画（当文本 刚好当前页面显示完 根据该值决定是否启用动画）
     *
     * ● 2023-11-02 17:13:40 周四 下午
     * @author crowforkotlin
     */
    var mEnableSingleTextAnimation: Boolean = false
        set(value) {
            mLastAnimation = -1
            field = value
        }

    /**
     * ● 停留时间 静止时生效
     *
     * ● 2023-10-31 13:59:29 周二 下午
     * @author crowforkotlin
     */
    var mResidenceTime: Long = 5000

    /**
     * ● 动画X方向
     *
     * ● 2023-11-02 14:53:24 周四 下午
     * @author crowforkotlin
     */
    var mAnimationLeft: Boolean = false

    /**
     * ● 动画Y方向
     *
     * ● 2023-11-02 14:53:45 周四 下午
     * @author crowforkotlin
     */
    var mAnimationTop: Boolean = false

    init {
        mTextPaint.color = Color.WHITE
        mTextPaint.textSize = mTextSize
        mTextPaint.typeface = Typeface.MONOSPACE
    }



    /**
     * ● 窗口分离
     *
     * ● 2023-10-31 18:11:26 周二 下午
     * @author crowforkotlin
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        "onDetachedFromWindow".log()
        cancelAnimationJob()
        cancelAnimator()
        mCacheViews.clear()
        mList.clear()
    }

    private fun initStaticMarView(): StaticMarView {
        return StaticMarView(context).also { view ->
            view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            view.mTextPaint = mTextPaint
            view.mMultiLineEnable = mMultipleLineEnable
            view.mGravity = mGravity
            addView(view)
        }
    }

    /**
     * ● 值发生变化 执行对于的Logic
     *
     * ● 2023-10-31 14:14:18 周二 下午
     * @author crowforkotlin
     */
    private fun<T : Any> onVariableChanged(flag: Int, oldValue: T, newValue: T) {

        // 根据FLAG 执行对于Logic
        when(flag) {
            FLAG_LAYOUT_REFRESH -> { onNotifyLayoutUpdate() }
            FLAG_CHILD_REFRESH -> { onNotifyViewUpdate() }
            FLAG_TEXT -> {
                onUpdateTextLists(getTextLists(newValue as String))
                // 如果缓存View < 2个 则初始化缓存View
                val currentCacheViewSize = mCacheViews.size
                if (currentCacheViewSize < REQUIRED_CACHE_SIZE) {
                    val viewsToAdd = REQUIRED_CACHE_SIZE - currentCacheViewSize
                    for (index in 0 until  viewsToAdd) {
                        val view = initStaticMarView()
                        if (index == 1) { view.isInvisible = true }
                        mCacheViews.add(view)
                    }
                }
                onUpdatePosOrView()
                onNotifyLayoutUpdate()
            }
            FLAG_TEXT_SIZE -> {
                mTextPaint.textSize = mTextSize
                onUpdateTextLists(getTextLists(mText))
                onUpdatePosOrView()
            }
            FLAG_SCROLL_SPEED -> {
                // 根据 mScrollSpeed 动态调整 mAnimationDuration
                val baseDuration = (16 - newValue as Int)
                mAnimationDuration = if (baseDuration == 1) {
                    2000L
                } else {
                    2000L + (400 * baseDuration)
                }
            }
            FLAG_NEWLINE -> {
                mCacheViews.forEach {  view -> view.mMultiLineEnable = mMultipleLineEnable }
            }
            FLAG_NEWLINE -> {
                mCacheViews.forEach {  view -> view.mGravity = mGravity }
            }
        }
    }

    /**
     * ● 更新文本列表
     *
     * ● 2023-11-01 17:37:01 周三 下午
     * @author crowforkotlin
     */
    private fun onUpdateTextLists(texts: MutableList<Pair<String, Float>>) {
        mList.clear()
        mList.addAll(texts)
    }


    /**
     * ● 更新当前文本位置 或者 视图 （当列表被更新 可能 会小于当前的列表位置 就直接 替换成最后一个， 相对会继续触发更新ChildView 否则也是更新ChildView 所以用else if 继续做个判断）
     *
     * ● 2023-11-01 17:34:08 周三 下午
     * @author crowforkotlin
     */
    private fun onUpdatePosOrView() {
        val size = mList.size
        if (size <= mListPosition) { mListPosition = size - 1 }
        else if (mUpdateStrategy == STRATEGY_TEXT_UPDATE_DEFAULT) { onNotifyViewUpdate() }
    }


    /**
     * ● 通知视图更新
     *
     * ● 2023-11-01 10:15:07 周三 上午
     * @author crowforkotlin
     */
    private fun onNotifyLayoutUpdate() {
        if ((mList.size == 1 && !mEnableSingleTextAnimation) || mLastAnimation == mAnimationMode) return
        else { mLastAnimation = mAnimationMode }
        cancelAnimator()
        cancelAnimationJob()
        mAnimationJob = mViewScope.launch(CoroutineExceptionHandler { _, throwable -> throwable.stackTraceToString().log() }) {
            while(true) {
                when(mAnimationMode) {
                    ANIMATION_DEFAULT -> {
                        launchDefaultAnimation()
                    }
                    ANIMATION_MOVE_X -> {
                        delay(mResidenceTime)
                        launchMoveXAnimation()
                    }
                    ANIMATION_MOVE_Y -> {
                        delay(mResidenceTime)
                        launchMoveYAnimation()
                    }
                    ANIMATION_FADE -> {
                        delay(mResidenceTime)
                        launchFadeAnimation(sync = false)
                    }
                    ANIMATION_FADE_SYNC -> {
                        delay(mResidenceTime)
                        launchFadeAnimation(sync = true)
                    }
                }
            }
        }
    }

    /**
     * ● 通知视图View的可见性改变
     *
     * ● 2023-11-01 19:13:58 周三 下午
     * @author crowforkotlin
     */
    private fun onNotifyViewVisibility(pos: Int) {
        val viewCurrentA = mCacheViews[pos]
        val viewNextB = getNextView(pos)
        viewCurrentA.translationX = 0f
        viewNextB.translationX = 0f
        viewCurrentA.isVisible = true
        viewNextB.isInvisible = true
    }

    /**
     * ● 获取上一个 或 下一个 View
     *
     * ● 2023-11-02 10:44:24 周四 上午
     * @author crowforkotlin
     */
    private fun getNextView(pos: Int): StaticMarView {
        return if (pos < mCacheViews.size - 1) {
            mCacheViews[pos + 1]
        } else {
            mCacheViews[pos - 1]
        }
    }

    /**
     * ● 通知视图View更新 如果动画模式 不是 静止切换 代表 当前视图和（上一个视图）需要动态更新 否则 只有当前视图才更新
     *
     * ● 2023-11-01 19:13:46 周三 下午
     * @author crowforkotlin
     */
    private fun onNotifyViewUpdate() {
        if (mList.isEmpty()) return
        val viewCurrentA = mCacheViews[mCurrentViewPos]
        val list : MutableList<Pair<String, Float>> = mList.toMutableList()
        viewCurrentA.mList = list
        if (mMultipleLineEnable) {
            viewCurrentA.mListPosition = mMultipleLinePos
        } else {
            viewCurrentA.mListPosition = mListPosition
        }
        if (mEnableSingleTextAnimation &&  mAnimationMode != ANIMATION_DEFAULT) {
/*            val viewNextB = getNextView(mCurrentViewPos)
            viewNextB.mList = list
            viewNextB.mListPosition = listPosition*/
        }
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
                    textList.add(textStringBuilder.toString() to textWidth)
                } else {
                    textStringWidth = textWidth
                }
            }
        }
        return textList
    }

    /**
     * ● 更新ChildView的位置
     *
     * ● 2023-11-02 17:24:43 周四 下午
     * @author crowforkotlin
     */
    private fun updateViewPosition() {
        if (mCurrentViewPos < mCacheViews.size - 1) {
            mCurrentViewPos ++
        } else {
            mCurrentViewPos = 0
        }
    }

    /**
     * ● 更新文本列表的位置
     *
     * ● 2023-11-02 17:24:58 周四 下午
     * @author crowforkotlin
     */
    private fun updateTextListPosition() {
        if (mList.isEmpty()) return
        when(mMultipleLineEnable) {
            true -> {
                val maxLine = (measuredHeight / getTextHeight(mTextPaint.fontMetrics)).toInt()
                val listSize = mList.size
                var pos: Int = listSize / maxLine
                if (listSize  % maxLine != 0) { pos ++ }
                if (mMultipleLinePos < pos - 1) {
                    mMultipleLinePos ++
                } else {
                    mMultipleLinePos = 0
                }
            }
            false ->{
                if (mListPosition < mList.size - 1) {
                    mListPosition ++
                } else {
                    mListPosition = 0
                }
            }
        }
    }

    /**
     * ● 默认的动画
     *
     * ● 2023-11-01 09:51:05 周三 上午
     * @author crowforkotlin
     */
    private suspend fun launchDefaultAnimation() {
        onNotifyViewVisibility(mCurrentViewPos)
        delay(mResidenceTime)
        updateViewPosition()
        updateTextListPosition()
    }


    /**
     * ● X方向移动
     *
     * ● 2023-11-01 09:51:11 周三 上午
     * @author crowforkotlin
     */
    private suspend fun launchMoveXAnimation() = suspendCancellableCoroutine { continuation ->
        mViewAnimatorSet?.cancel()
        mViewAnimatorSet = AnimatorSet()
        val viewCurrentA = mCacheViews[mCurrentViewPos]
        val viewNextB = getNextView(mCurrentViewPos)
        val viewAEnd : Float
        val viewBStart : Float
        val viewX = layoutParams.width.toFloat()
        if (mAnimationLeft) {
            viewAEnd = -viewX
            viewBStart = viewX
        } else {
            viewAEnd = viewX
            viewBStart = -viewX
        }
        val viewAnimationA = ObjectAnimator.ofFloat(viewCurrentA, "translationX", 0f , viewAEnd)
        val viewAnimationB = ObjectAnimator.ofFloat(viewNextB, "translationX", viewBStart, 0f)
        mViewAnimatorSet?.let { animatorSet ->
            animatorSet.duration = mAnimationDuration
            animatorSet.interpolator = LinearInterpolator()
            animatorSet.playTogether(viewAnimationA, viewAnimationB)
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    viewNextB.isVisible = true
                    updateViewPosition()
                    updateTextListPosition()
                }
                override fun onAnimationEnd(animation: Animator) {
                    viewCurrentA.isInvisible = true
                    if (!continuation.isCompleted) continuation.resume(Unit)
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animatorSet.start()
        }
    }

    /**
     * ● X方向移动
     *
     * ● 2023-11-01 09:51:11 周三 上午
     * @author crowforkotlin
     */
    private suspend fun launchMoveYAnimation() = suspendCancellableCoroutine { continuation ->
        mViewAnimatorSet?.cancel()
        mViewAnimatorSet = AnimatorSet()
        val viewCurrentA = mCacheViews[mCurrentViewPos]
        val viewNextB = getNextView(mCurrentViewPos)
        val viewAEnd : Float
        val viewBStart : Float
        val viewY = layoutParams.height.toFloat()
        if (mAnimationTop) {
            viewAEnd = -viewY
            viewBStart = viewY
        } else {
            viewAEnd = viewY
            viewBStart = -viewY
        }
        val viewAnimationA = ObjectAnimator.ofFloat(viewCurrentA, "translationY", 0f , viewAEnd)
        val viewAnimationB = ObjectAnimator.ofFloat(viewNextB, "translationY", viewBStart, 0f)
        mViewAnimatorSet?.let { animatorSet ->
            animatorSet.duration = mAnimationDuration
            animatorSet.interpolator = LinearInterpolator()
            animatorSet.playTogether(viewAnimationA, viewAnimationB)
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    viewNextB.isVisible = true
                    updateViewPosition()
                    updateTextListPosition()
                }
                override fun onAnimationEnd(animation: Animator) {
                    viewCurrentA.isInvisible = true
                    if (!continuation.isCompleted) continuation.resume(Unit)
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animatorSet.start()
        }
    }

    /**
     * ● 但如淡出动画
     *
     * ● 2023-11-02 17:24:05 周四 下午
     * @param sync 是否同步
     * @author crowforkotlin
     */
    private suspend fun launchFadeAnimation(sync: Boolean) = suspendCancellableCoroutine { continuation ->
        mViewAnimatorSet?.cancel()
        mViewAnimatorSet = AnimatorSet()
        val viewCurrentA = mCacheViews[mCurrentViewPos]
        val viewNextB = getNextView(mCurrentViewPos)
        val viewAnimationA = ObjectAnimator.ofFloat(viewCurrentA, "alpha", 1f , 0f)
        val viewAnimationB = ObjectAnimator.ofFloat(viewNextB, "alpha", 0f, 1f)
        mViewAnimatorSet?.let { animatorSet ->
            animatorSet.duration = mAnimationDuration
            animatorSet.interpolator = LinearInterpolator()
            if (sync) { animatorSet.playSequentially(viewAnimationA, viewAnimationB) }
            else { animatorSet.playTogether(viewAnimationA, viewAnimationB) }
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    viewNextB.alpha = 0f
                    viewNextB.isVisible = true
                    updateViewPosition()
                    updateTextListPosition()
                }
                override fun onAnimationEnd(animation: Animator) {
                    viewCurrentA.isInvisible = true
                    if (!continuation.isCompleted) {
                        "Resume".log()
                        continuation.resume(Unit)
                    }
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animatorSet.start()
        }
    }

    /**
     * ● 取消动画任务
     *
     * ● 2023-11-02 17:24:00 周四 下午
     * @author crowforkotlin
     */
    private fun cancelAnimationJob() {
        mAnimationJob?.cancel()
        mAnimationJob = null
    }

    /**
     * ● 取消动画
     *
     * ● 2023-11-01 09:51:21 周三 上午
     * @author crowforkotlin
     */
    private fun cancelAnimator() {
        mViewAnimatorSet?.cancel()
        mViewAnimatorSet = null
    }

    /**
     * ● 应用配置 -- 触发View的更新
     *
     * ● 2023-11-02 17:25:43 周四 下午
     * @author crowforkotlin
     */
    fun applyOption() {
        runCatching {
            mViewScope.launch {
                if (mCacheViews.isEmpty()) return@launch
                cancelAnimator()
                cancelAnimationJob()
                val viewCurrentA = mCacheViews[mCurrentViewPos]
                val viewNextB = getNextView(mCurrentViewPos)
                viewCurrentA.alpha = 1f
                viewCurrentA.isVisible = true
                viewNextB.alpha = 1f
                viewNextB.isInvisible = true
                mLastAnimation = -1
                onNotifyLayoutUpdate()
                onNotifyViewUpdate()
            }
        }
            .onFailure { cause -> "● StaticMarLayout apply option failure : ${cause.stackTraceToString()}".log() }
    }
}