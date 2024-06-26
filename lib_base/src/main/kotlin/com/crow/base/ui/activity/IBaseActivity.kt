package com.crow.base.ui.activity

import android.os.Handler

/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: lib_base/src/main/java/com/barry/base/activity
 * @Time: 2022/10/4 16:55
 * @Author: CrowForKotlin
 * @Description: IBaseActivity 接口基类
 * @formatter:on
 **************************/
interface IBaseActivity {

    /* Handler异步 */
    var mHandler: Handler?

    /* 是否需要延时加载数据 */
    fun isNeedLazyData(): Boolean

    /* 延时加载数据的时间 */
    fun doLazyDataDelayTime(): Long

    /* 执行延时逻辑 */
    fun doLazyData()
}