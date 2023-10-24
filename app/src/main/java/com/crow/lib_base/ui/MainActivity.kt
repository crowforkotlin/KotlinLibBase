package com.crow.lib_base.ui

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.FrameLayout
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.ave.vastgui.tools.view.masklayout.MaskAnimation
import com.crow.lib_base.R
import com.crow.lib_base.databinding.AppActivityMainBinding
import com.crow.lib_base.ui.masklayout.MaskLayout
import com.crow.lib_base.ui.masklayout.MaskView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mBinding by lazy { AppActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.button.setOnClickListener {
            mBinding.marklayout.activeMask(MaskAnimation.COLLAPSED) {
                mBinding.marklayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            }
        }
        mBinding.button2.setOnClickListener {
            mBinding.marklayout.activeMask(MaskAnimation.EXPANDED) {
                mBinding.marklayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }
}