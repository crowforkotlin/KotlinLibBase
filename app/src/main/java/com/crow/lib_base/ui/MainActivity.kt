package com.crow.lib_base.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.ave.vastgui.tools.view.masklayout.MaskAnimation
import com.crow.lib_base.R
import com.crow.lib_base.databinding.AppActivityMainBinding
import com.crow.lib_base.ui.masklayout.MaskLayout
import com.crow.lib_base.ui.masklayout.MaskView
import com.crow.lib_base.ui.view.StaticMarView
import com.tencent.bugly.proguard.y
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
        /*val list = arrayListOf<Pair<FrameLayout, StaticMarView>>()
        repeat(4) { x ->
            repeat(4) { y ->
                list.add(initStaticMarView((x * 32).toFloat(), (y * 16).toFloat()))
            }
        }
        lifecycleScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(20)
                list.random().second.apply {
                    mText = it.toString() + "qweu"
                    cancelAnimator()
                }
            }
        }*/

        initStaticMarView(0f, 0f).second.apply {
            mText = "Hello,Yd, yp!"
            mGravity = StaticMarView.GRAVITY_CENTER
            lifecycleScope.launch {
                repeat(Int.MAX_VALUE) {
                    delay(20)
                    mText = it.toString() + "2346xcverQWEqweYI\rOPu"
                }
            }
        }
    }

    private fun initStaticMarView(x: Float, y: Float): Pair<FrameLayout, StaticMarView> {
        val view = StaticMarView(this)
        val frame = FrameLayout(this)
        frame.layoutParams = FrameLayout.LayoutParams(128, 64)
        frame.x = x
        frame.y = y
        view.layoutParams = ViewGroup.LayoutParams(128, 64)
        frame.addView(view)
        mBinding.root.addView(frame)
        view.mText = "欢迎来到静态文本区域显示一些简单的内容"
/*        lifecycleScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(100)
            }
        }*/
/*        lifecycleScope.launch {
            repeat(20) {
                delay(3000)
                view.mTextSize = it + 12f
            }
        }*/
        return frame to view
    }
}