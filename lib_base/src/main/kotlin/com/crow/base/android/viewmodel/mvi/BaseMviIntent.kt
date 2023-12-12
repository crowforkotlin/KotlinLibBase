package com.crow.base.android.viewmodel.mvi

import com.crow.base.android.viewmodel.BaseViewState

/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: lib_base/src/main/kotlin/com/crow/base/viewmodel/mvi
 * @Time: 2023/3/9 19:18
 * @Author: CrowForKotlin
 * @Description: BaseMviEvent
 * @formatter:on
 **************************/
open class BaseMviIntent(var mViewState: BaseViewState = BaseViewState.Default)