package com.crow.lib_base

import android.app.Application

val app = App.mContext
/*************************
 * @Package: com.crow.lib_base
 * @Time: 2023/9/14 11:03
 * @Author: CrowForKotlin
 * @Description: Application
 * @formatter:on
 **************************/
class App : Application() {

    companion object { internal  lateinit var mContext: Application }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}