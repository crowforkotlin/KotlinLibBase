package com.crow.base.android.activity

import android.os.Bundle

/*************************
 * @ProjectName: JetpackApp
 * @Dir_Path: lib_base/src/main/java/cn/barry/base/activity
 * @Time: 2022/4/26 9:32
 * @Author: CrowForKotlin
 * @Description: BaseViewBindingActivity 父类
 * @formatter:on
 **************************/
abstract class BaseActivity : com.crow.base.android.activity.BaseActivityImpl() {

    override fun initView(savedInstanceState: Bundle?) {}

    override fun initListener() {}
}