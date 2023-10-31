/*
package com.crow.lib_base.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.annotation.AnimRes
import com.crow.lib_base.R

class StaticMarView<T> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewFlipper(context, attrs) {
    private var mInterval = 4000
    private val mHasSetAnimDuration = true
    private var mAnimationDuration = 2000
    private var mTextSize = 14
    private var mbold = 0
    private var mTextColor = "#000000"
    private var mBackground = "#000000"
    private val mSingleLine = true
    private var mGravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
    private var mFontFamily: String? = null
    private val mDirection = DIRECTION_BOTTOM_TO_TOP
    private var mTypeface: Typeface? = null

    @AnimRes
    private val mInAnimResId: Int = R.anim.anim_bottom_in_static

    @AnimRes
    private val outAnimResId: Int = R.anim.anim_top_out_static
    var currentPosition = 0
        private set
    private var messages: MutableList<T>? = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    var textViewsMap: MutableMap<Int, View?> = HashMap()
    private var times = 0

    var ktDelegate: StaticMarViewKtDelegate<T> =
        StaticMarViewKtDelegate(this, messages, textViewsMap)
    val currentTextView: TextView?
        get() = textViewsMap[currentPosition] as TextView?
    val currentText: String
        get() = messages!![currentPosition].toString()

    fun updateText(messages: MutableList<T>) {
        setMessages(messages)
        val textView = currentTextView ?: return
        val text = currentText
        val size = messages.size
        if (!textView.getText().toString().contentEquals(text)) {
            if (size > 1 && currentPosition == size - 1) {
                textView.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        startFlipping()
                        textView.removeTextChangedListener(this)
                    }
                })
                stopFlipping()
            }
            textView.text = text
        } else {
            if (!isFlipping && size > 1) {
                startFlipping()
            }
        }
    }

    fun updateText(message: String?) {
        val textView = textViewsMap[currentPosition] as TextView?
        if (textView != null) {
            ktDelegate.updateText(textView, message)
        }
    }
    */
/**
     * 根据字符串，启动翻页公告
     *
     * @param message      字符串
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     *//*

    */
/**
     * 根据字符串，启动翻页公告
     *
     * @param message 字符串
     *//*

    @JvmOverloads
    fun startWithText(
        message: String,
        @AnimRes inAnimResId: Int = this.mInAnimResId,
        @AnimRes outAnimResID: Int = outAnimResId
    ) {
        if (TextUtils.isEmpty(message)) return
        getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this)
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this)
                }
                startWithFixedWidth(message, inAnimResId, outAnimResID)
            }
        })
    }

    */
/**
     * 根据字符串和宽度，启动翻页公告
     *
     * @param message 字符串
     *//*

    private fun startWithFixedWidth(
        message: String,
        @AnimRes inAnimResId: Int,
        @AnimRes outAnimResID: Int
    ) {
        val messageLength = message.length
        val width: Int = StaticMarUtil.px2dip(context, width)
        if (width == 0) {
            throw RuntimeException("Please set the width of MarqueeView !")
        }
        val tvPaint = Paint() //获取TextView的Paint
        tvPaint.textSize = mTextSize.toFloat()
        val v = tvPaint.measureText(message, 0, messageLength).toInt()
        val v1 = v / messageLength
        val limit = width / v1
        val list: MutableList<*> = ArrayList<String>()
        if (messageLength <= limit) {
            list.add(message)
        } else {
            val size = messageLength / limit + if (messageLength % limit != 0) 1 else 0
            for (i in 0 until size) {
                val startIndex = i * limit
                val endIndex =
                    if ((i + 1) * limit >= messageLength) messageLength else (i + 1) * limit
                list.add(message.substring(startIndex, endIndex))
            }
        }
        if (messages == null) {
            messages = ArrayList()
        }
        messages!!.clear()
        messages!!.addAll(list)
        postStart(inAnimResId, outAnimResID)
    }

    */
/**
     * 根据字符串列表，启动翻页公告
     *
     * @param messages 字符串列表
     *//*

    fun startWithList(messages: MutableList<T>?) {
        startWithList(messages, mInAnimResId, outAnimResId)
    }

    */
/**
     * 根据字符串列表，启动翻页公告
     *
     * @param messagesList 字符串列表
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     *//*

    fun startWithList(
        messagesList: MutableList<T>?,
        @AnimRes inAnimResId: Int,
        @AnimRes outAnimResID: Int
    ) {
        if (StaticMarUtil.isEmpty(messagesList)) return
        setMessages(messagesList)
        postStart(inAnimResId, outAnimResID)
    }

    private fun postStart(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        post { start(inAnimResId, outAnimResID) }
    }

    private var isAnimStart = false

    init {
        init(context, attrs, 0)
    }

    private fun start(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        try {
            val displayedChild = displayedChild
            val childAt = getChildAt(displayedChild)
            if (childAt != null) {
                childAt.setAnimation(null)
                childAt.visibility = INVISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            removeAllViews()
            clearAnimation()
            stopFlipping()

            // 检测数据源
            if (messages == null || messages!!.isEmpty()) {
                throw RuntimeException("The messages cannot be empty!")
            }
            currentPosition = 0
            val textView = createTextView(messages!![currentPosition], 1)
            addView(textView)
            LogUtil.i(TipsExtKt.CROW_TAG, "◉ Textview Text is " + textView!!.getText())
            textViewsMap[currentPosition] = textView
            if (messages!!.size >= 1) {
                setInAndOutAnimation(inAnimResId, outAnimResID)
                startFlipping()
            }
            if (inAnimation != null) {
                inAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        LogUtil.i("test1107", "onAnimationStart")
                        if (isAnimStart) {
                            animation.cancel()
                        }
                        isAnimStart = true
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        LogUtil.i("test1107", "onAnimationEnd")
                        currentPosition++
                        if (currentPosition >= messages!!.size) {
                            currentPosition = 0
                        }
                        val view: View? = createTextView(messages!![currentPosition], 2)
                        if (view!!.parent == null) {
                            addView(view)
                            textViewsMap[currentPosition] = textView
                        }
                        isAnimStart = false
                    }

                    override fun onAnimationRepeat(animation: Animation) {
                        LogUtil.i("test1107", "onAnimationRepeat")
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setGravity(mGravity: Int) {
        when (mGravity) {
            GRAVITY_LEFT_TOP -> this.mGravity = Gravity.LEFT
            GRAVITY_CENTER_TOP -> this.mGravity = Gravity.CENTER_HORIZONTAL
            GRAVITY_RIGHT_TOP -> this.mGravity = Gravity.RIGHT
            GRAVITY_LEFT -> this.mGravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            GRAVITY_CENTER -> this.mGravity = Gravity.CENTER or Gravity.CENTER_VERTICAL
            GRAVITY_RIGHT -> this.mGravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
            GRAVITY_LEFT_BOTTOM -> this.mGravity = Gravity.BOTTOM
            GRAVITY_CENTER_BOTTOM -> this.mGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            GRAVITY_RIGHT_BOTTPM -> this.mGravity = Gravity.BOTTOM or Gravity.RIGHT
        }
    }

    private fun createTextView(marqueeItem: T, type: Int): TextView? {
        var textView: MyZZTextView? = getChildAt((displayedChild + 1) % 3) as MyZZTextView
        //        NoPaddingTextView textView = (NoPaddingTextView) getChildAt((getDisplayedChild() + 1) % 3);
        if (textView == null) {
            textView = MyZZTextView(context, mFontFamily)
            //            textView = new NoPaddingTextView(getContext(), null, fontFamily);
            textView.setGravity(mGravity)
            textView.setTextColor(Color.parseColor(mTextColor))
            textView.getPaint().setAntiAlias(true)
            textView.getPaint().setFlags(0)
            if (mBackground == "#000000") textView.setBackgroundColor(Color.TRANSPARENT)
            val fontPath: String = textView.initFont(context, mFontFamily)
            if (!TextUtils.isEmpty(fontPath)) {
                textView.setFont(fontPath)
            }
            textView.setTextSize(mTextSize)
            if (mbold == 1) textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)) //设置加粗
            else textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL)) //设置不加粗
            textView.setIncludeFontPadding(true)
            textView.setSingleLine(mSingleLine)
            if (mSingleLine) {
                textView.setMaxLines(1)
                //                textView.setEllipsize(TextUtils.TruncateAt.END);
            }
            if (mTypeface != null) {
                textView.setTypeface(mTypeface)
            }
            textView.setOnClickListener(OnClickListener { v ->
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(getPosition(), v as TextView)
                }
            })
        }
        //        if(type == 2){
        var message: CharSequence = ""
        if (marqueeItem is CharSequence) {
            message = marqueeItem
        } else if (marqueeItem is StaticMqrItem) {
            message = (marqueeItem as StaticMqrItem).marqueeMessage()
        }
        textView.setText(message)
        textView.setTag(currentPosition)
        //        }
        return textView
    }

    fun getPosition(): Int {
        return currentView.tag as Int
    }

    fun getMessages(): List<T>? {
        return messages
    }

    fun setMessages(messages: MutableList<T>?) {
        this.messages = messages
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, textView: TextView?)
    }

    */
/**
     * 设置进入动画和离开动画
     *
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     *//*

    private fun setInAndOutAnimation(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        val inAnim = AnimationUtils.loadAnimation(context, inAnimResId)
        if (mHasSetAnimDuration) inAnim.setDuration(mAnimationDuration.toLong())
        inAnimation = inAnim
        val outAnim = AnimationUtils.loadAnimation(context, outAnimResID)
        if (mHasSetAnimDuration) outAnim.setDuration(mAnimationDuration.toLong())
        outAnimation = outAnim
        if (times > 0) {
            val alphaAnimation1 = AlphaAnimation(0.0f, 1.0f)
            alphaAnimation1.setDuration(800)
            alphaAnimation1.setRepeatCount(times - 1)
            alphaAnimation1.repeatMode = Animation.RESTART
            setAnimation(alphaAnimation1)
            alphaAnimation1.start()
        }
    }

    fun setTypeface(typeface: Typeface?) {
        this.mTypeface = typeface
    }

    */
/**
     * 如果type：true  则speed值赋给mAnimDuration
     *
     * @param speed
     * @param type
     *//*

    fun setScrollSpeed(speed: Int, type: Boolean, setFlag: Boolean) {
        if (type) {
            mAnimationDuration = speed
        } else {
            when (speed) {
                0 -> mAnimationDuration = 12000
                1 -> mAnimationDuration = 11000
                2 -> mAnimationDuration = 10000
                3 -> mAnimationDuration = 9000
                4 -> mAnimationDuration = 8000
                5 -> mAnimationDuration = 7000
                6 -> mAnimationDuration = 6000
                7 -> mAnimationDuration = 5000
                8 -> mAnimationDuration = 4000
                9 -> mAnimationDuration = 3000
                10 -> mAnimationDuration = 2000
                11 -> mAnimationDuration = 1000
                12 -> mAnimationDuration = 800
                13 -> mAnimationDuration = 500
                14 -> mAnimationDuration = 300
                else -> {}
            }
        }
        //
//        if (interval <= animDuration)
//            interval = animDuration + 1000;
        flipInterval = mInterval + mAnimationDuration
    }

    fun setAnimDuration(duration: Int) {
        mAnimationDuration = duration
    }

    fun setAnimInterval(minterval: Int) {
        mInterval = minterval
    }

    fun setTextColor(textColor: String) {
        this.mTextColor = textColor
    }

    fun setBold(bold: Int) {
        this.mbold = bold
    }

    fun setFontFamily(fontFamily: String?) {
        this.mFontFamily = fontFamily
    }

    fun setBackgroundT(backgroundT: String) {
        this.mBackground = backgroundT
    }

    fun getAnimIn(type: Int): Int {
        //        0:静止；1：向左，3：向右；5：向上；6：向下
        var returnType: Int = R.anim.anim_static_in
        when (type) {
            0 -> returnType = R.anim.anim_static_in
            1 -> returnType = R.anim.anim_right_in_static
            2 -> returnType = R.anim.anim_right_in_static
            3 -> returnType = R.anim.anim_left_in_static
            4 -> returnType = R.anim.anim_left_in_static
            5 -> returnType = R.anim.anim_bottom_in_static
            6 -> returnType = R.anim.anim_top_in_static
            else -> {}
        }
        return returnType
    }

    fun getAnimOut(type: Int): Int {
        //        0:静止；1：向左，3：向右；5：向上；6：向下
        var returnType: Int = R.anim.anim_static_out
        when (type) {
            0 -> returnType = R.anim.anim_static_out
            1 -> returnType = R.anim.anim_left_out_static
            2 -> returnType = R.anim.anim_left_out_static
            3 -> returnType = R.anim.anim_right_out_static
            4 -> returnType = R.anim.anim_right_out_static
            5 -> returnType = R.anim.anim_top_out_static
            6 -> returnType = R.anim.anim_bottom_out_static
            else -> {}
        }
        return returnType
    }

    fun setTimes(times: Int) {
        this.times = times
    }

    companion object {
        private const val GRAVITY_LEFT_TOP = 0
        private const val GRAVITY_CENTER_TOP = 1
        private const val GRAVITY_RIGHT_TOP = 2
        private const val GRAVITY_LEFT = 3
        private const val GRAVITY_CENTER = 4
        private const val GRAVITY_RIGHT = 5
        private const val GRAVITY_LEFT_BOTTOM = 6
        private const val GRAVITY_CENTER_BOTTOM = 7
        private const val GRAVITY_RIGHT_BOTTPM = 8
        private const val DIRECTION_BOTTOM_TO_TOP = 0
        private const val DIRECTION_TOP_TO_BOTTOM = 1
        private const val DIRECTION_RIGHT_TO_LEFT = 2
        private const val DIRECTION_LEFT_TO_RIGHT = 3
    }
}
*/
