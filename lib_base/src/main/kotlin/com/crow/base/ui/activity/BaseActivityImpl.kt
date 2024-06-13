package com.crow.base.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.crow.base.app.APP_DIALOG
import com.crow.base.component.IMaterialBindingDialog
import com.crow.base.tools.coroutine.FlowBus
import com.crow.base.tools.extensions.newMaterialDialog

/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: lib_base/src/main/java/com/barry/base/activity
 * @Time: 2022/11/14 19:20
 * @Author: CrowForKotlin
 * @Description: BaseViewBindingActivityImpl 实现
 * @formatter:on
 **************************/
abstract class BaseActivityImpl : AppCompatActivity(), IBaseActivity {

    init {
        FlowBus.with<IMaterialBindingDialog<ViewBinding>>(APP_DIALOG).register(lifecycleScope) { callback ->
            if (isDestroyed) return@register
            if (!mIsPause)
            callback.onDialog(newMaterialDialog { dialog -> callback.onDialogConfig(dialog, callback.getBinding(layoutInflater)) })
        }
    }

    private var mIsPause: Boolean = false

    override var mHandler: Handler? = Handler(Looper.getMainLooper())

    override fun isNeedLazyData(): Boolean = false
    override fun doLazyDataDelayTime(): Long = 300L
    override fun doLazyData() {}

    open fun initData(savedInstanceState: Bundle?) {}

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun initListener()

    // 获取加载动画 是否需要延时执行数据
    override fun onResume() {
        super.onResume()
        mIsPause = false
        if (isNeedLazyData()) mHandler?.postDelayed({ doLazyData() }, doLazyDataDelayTime())
    }

    // 置空 mHandler
    override fun onDestroy() {
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mIsPause = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
        initView(savedInstanceState)
        initListener()
    }

    override fun onStop() {
        super.onStop()
    }
}