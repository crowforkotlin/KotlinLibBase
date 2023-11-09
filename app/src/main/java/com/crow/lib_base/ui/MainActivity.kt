package com.crow.lib_base.ui

import android.graphics.Color
import android.os.Bundle
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
        /*         val width = 128
                val height = 64
                val list = mutableListOf<StaticMarLayout>()
               repeat(15) { x ->
                    repeat(16) { y ->
                        list.add(createStaticMarView(width.toFloat() * x, height.toFloat() * y ,width, height))
                    }
                }
                val text = "123456789-ABCYPJI-!&*#(()-OWPXU啊这样-好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！！"
                lifecycleScope.launch {
                    var count = 0
                    while(true) {
                        delay(25L)
                        list.random().mText = "$text$count"
                        count++
                    }
                }*/
        createStaticMarView2(0f, 0f, 128, 64)
        /*val view = createStaticMarView2(0f, 0f, 128, 64)
        mBinding.button.setOnClickListener {
            view.mEnableSingleTextAnimation = false
            view.applyOption()
        }*/
    }

    private fun createStaticMarView(x: Float, y: Float, width: Int, height: Int): StaticTextLayout {
        val text = "123456789-ABCYPJI-!&*#(()-OWPXU啊这样-好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！！"
//        val text = "好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！！"
        val layout = StaticTextLayout(this)

        layout.x = x
        layout.y = y
        mBinding.root.addView(layout)
        layout.layoutParams = FrameLayout.LayoutParams(width,height)
        layout.mMultipleLineEnable = (0..2).random() != 1
        layout.mGravity = (StaticTextLayout.GRAVITY_TOP_START..StaticTextLayout.GRAVITY_BOTTOM_END).random()
        layout.mEnableSingleTextAnimation = false
        layout.mResidenceTime = (0..3000).random().toLong()
        layout.mAnimationMode = (StaticTextLayout.ANIMATION_DEFAULT..StaticTextLayout.ANIMATION_FADE_SYNC).random()
        layout.mAnimationStrategy = StaticTextLayout.STRATEGY_ANIMATION_UPDATE_DEFAULT
        layout.mScrollSpeed = 15
        layout.mText = text
        /*lifecycleScope.launch {
            delay(3000)
            layout.mEnableSingleTextAnimation = false
            layout.mMultipleLineEnable = false
            layout.applyOption()
        }*/
        return layout
    }

    private fun createStaticMarView2(x: Float, y: Float, width: Int, height: Int): StaticTextLayout {
//        val text = "12345678988U！"
        val text = "好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！qweiqx@%!xTIQNAQWENXOQWEM#&IA我阿斯顿维拉4i9992188nnaduqwuzxucqwbdq!@$@#@snajaiw"
        val layout = StaticTextLayout(this)
        layout.x = x
        layout.y = y
        mBinding.root.addView(layout)
        layout.layoutParams = FrameLayout.LayoutParams(width,height)
        layout.mMultipleLineEnable = true
        layout.mGravity = StaticTextLayout.GRAVITY_CENTER
        layout.mEnableSingleTextAnimation = true
        layout.mResidenceTime = 1000
        layout.mAnimationMode = StaticTextLayout.ANIMATION_MOVE_X
        layout.mAnimationStrategy = StaticTextLayout.STRATEGY_ANIMATION_UPDATE_DEFAULT
        layout.mScrollSpeed = 10
        layout.mText = text
        lifecycleScope.launch {
            delay(3500)
            layout.mFontSize = 11f
            layout.mFontColor = Color.WHITE
            layout.mEnableAntiAlias = true
            layout.applyOption()
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