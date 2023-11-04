package com.crow.lib_base.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.crow.lib_base.databinding.AppActivityMainBinding
import com.crow.lib_base.ui.view.StaticMarLayout
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

        val view = createStaticMarView(128, 64)
        mBinding.button.setOnClickListener {
            view.mEnableSingleTextAnimation = false
            view.applyOption()
        }
/*
        val views = mutableListOf<StaticMarLayout>()
        repeat(8) { x ->
            repeat(4) { y ->
                val view = createStaticMarView(16, 16)
                views.add(view)
                view.x = 16f * x
                view.y = 16f * y
                mBinding.root.addView(view)
            }
            lifecycleScope.launch {
                repeat(Int.MAX_VALUE) {
                    delay(100)
                    views.random().mText = it.toString()
                }
            }
        }
*/
    }

    private fun createStaticMarView(width: Int, height: Int): StaticMarLayout {
//        val text = "ABCDpqg-123456789-ABCYPJI-!&*#(()-OWPXU啊这样-好吧我觉得有BUG-确定吗？？？？"
        val text = "123456789-ABCYPJI-!&*#(()-OWPXU啊这样-好吧我觉得有BUG-确定吗？？？？我觉得是肯定的！！！"
        val layout = StaticMarLayout(this)
        mBinding.root.addView(layout)
        layout.layoutParams = FrameLayout.LayoutParams(width,height)
        layout.mMultipleLineEnable = true
        layout.mGravity = StaticMarLayout.GRAVITY_CENTER_START
        layout.mEnableSingleTextAnimation = true
        layout.mAnimationMode = StaticMarLayout.ANIMATION_FADE_SYNC
        layout.mScrollSpeed = 15
        layout.mText = text
        return layout
    }
}