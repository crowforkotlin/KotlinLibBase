package com.crow.lib_base.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.crow.lib_base.databinding.AppActivityMainBinding
import com.crow.lib_base.ui.view.StaticMarLayout
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

        initStaticMarView()
    }

    private fun initStaticMarView(): StaticMarLayout {
        val layout = StaticMarLayout(this)
//        layout.mAnimationMode = StaticMarLayout.ANIMATION_MOVE_X
        layout.mAnimationMode = StaticMarLayout.ANIMATION_DEFAULT
        layout.layoutParams = FrameLayout.LayoutParams(128,64)
        mBinding.root.addView(layout)
        val text = "123456你好 --- 456789哈哈"
        layout.mScrollSpeed = 15
        layout.mText = text
        lifecycleScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(500)
                layout.mText = "$text$it"
            }
        }
        lifecycleScope.launch {
            delay(8000)
            layout.mAnimationMode = StaticMarLayout.ANIMATION_MOVE_X
            delay(1500)
            layout.mAnimationMode = StaticMarLayout.ANIMATION_DEFAULT
            delay(5000)
            layout.mAnimationMode = StaticMarLayout.ANIMATION_MOVE_Y
            layout.mAnimationLeft = true
        }
        return layout
    }
}