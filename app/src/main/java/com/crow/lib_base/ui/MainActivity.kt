package com.crow.lib_base.ui

import android.graphics.Color
import android.os.Bundle
import android.webkit.WebSettings
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.crow.lib_base.databinding.AppActivityMainBinding
import com.crow.lib_base.ui.view.StaticTextLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mBinding by lazy { AppActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        /*        mBinding.button.setOnClickListener {
                    mBinding.marklayout.activeMask(MaskAnimation.COLLAPSED) {
                        mBinding.marklayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                    }
                }
                mBinding.button2.setOnClickListener {
                    mBinding.marklayout.activeMask(MaskAnimation.EXPANDED) {
                        mBinding.marklayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    }
                }*/
//        createStaticMarView(0f,0f,128,64)
                 /*val width = 128
                val height = 64
                val list = mutableListOf<StaticTextLayout>()
               repeat(10) { x ->
                    repeat(2) { y ->
                        list.add(createStaticMarView(width.toFloat() * x, height.toFloat() * y ,width, height))
                    }
                }*/
        /*mBinding.webview.apply {
            settings.let { ws ->

                ws.setJavaScriptEnabled(true);
                ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
                ws.setDatabaseEnabled(false);
                ws.setDomStorageEnabled(true);
                ws.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
                ws.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");//加载网页版，不加载手机版
            }
            layoutParams.width = 256
            layoutParams.height = 256
            setInitialScale(0)
            loadUrl("https://www.baidu.com")
        }*/
        createStaticMarView2(0f, 0f, 128, 64)
        /*val view = createStaticMarView2(0f, 0f, 128, 64)
        mBinding.button.setOnClickListener {
            view.mEnableSingleTextAnimation = false
            view.applyOption()
        }*/
    }

    private fun createStaticMarView(x: Float, y: Float, width: Int, height: Int): StaticTextLayout {
        val text =
            "123456789-ABCYPJI-!&*#(()-OW\nPXU啊这样-好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！！"
//        val text = "好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！！"
        val layout = StaticTextLayout(this)

        layout.x = x
        layout.y = y
        mBinding.root.addView(layout)
        layout.layoutParams = FrameLayout.LayoutParams(width, height)
        layout.mMultipleLineEnable = (0..2).random() != 1
        layout.mGravity = StaticTextLayout.GRAVITY_BOTTOM_CENTER
        layout.mEnableSingleTextAnimation = false
        layout.mResidenceTime = 2000L
        layout.mAnimationMode =StaticTextLayout.ANIMATION_MOVE_X
        layout.mAnimationStrategy = StaticTextLayout.STRATEGY_ANIMATION_UPDATE_DEFAULT
        layout.mScrollSpeed = 13
        layout.mText = text
        return layout
    }

    private fun createStaticMarView2(
        x: Float,
        y: Float,
        width: Int,
        height: Int,
    ): StaticTextLayout {
        val text1 = "静"
//           val text1 = "CROW/n自己/n自己/n写的一/n个静态/n文本组/n件，包含了静态文本/n布局 静态文/n本视图，手动计算文本位置 进行对应的绘制！代码量共计1300行左右，十分的简单！算是自定义View中的入门基础了！！/n！/n"
//        val text = "好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！qweiqx@%!xTIQNAQWENXOQWEM#&IA我阿斯顿维拉4i9992188nnaduqwuzxucqwbdq!@$@#@snajaiw"
//        val text = "好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！qweiqx@%!xTIQNAQWENXOQWEM#&IA"
        val layout = StaticTextLayout(this)
        layout.x = x
        layout.y = y
        mBinding.root.addView(layout)
        layout.layoutParams = FrameLayout.LayoutParams(width, height)
        layout.mGravity = StaticTextLayout.GRAVITY_CENTER
        layout.mEnableSingleTextAnimation = true
        layout.mMultipleLineEnable = true
        layout.mResidenceTime = 3000
        layout.mFontSize = 14f
        layout.mAnimationMode = StaticTextLayout.ANIMATION_MOVE_Y
        layout.mAnimationTop = false
        layout.mFontMonoSpace = true
        layout.mGravity = StaticTextLayout.GRAVITY_CENTER
        layout.mUpdateStrategy = StaticTextLayout.STRATEGY_TEXT_UPDATE_LAZY
        layout.mAnimationStrategy = StaticTextLayout.STRATEGY_ANIMATION_UPDATE_RESTART
        layout.mScrollSpeed = 14
        layout.mText = text1
        lifecycleScope.launch {
            /*            delay(4000)
            layout.mFontSize = 11f
            layout.mFontColor = Color.WHITE
            layout.mEnableAntiAlias = false
            layout.mFontItalic = true
            layout.mFontMonoSpace = true
            layout.applyOption()*/
        }
        /*lifecycleScope.launch {
            delay(3000)
            layout.mEnableSingleTextAnimation = false
            layout.mMultipleLineEnable = false
            layout.applyOption()
        }*/
        return layout
    }
}