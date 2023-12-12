package com.crow.base

import android.app.Application

val app = BaseApp.mContext

/**
 * ● BaseApp
 *
 * ● 2023/12/11 01:12
 * @author crowforkotlin
 * @formatter:on
 */
open class BaseApp : Application() {

    companion object { internal  lateinit var mContext: Application }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}