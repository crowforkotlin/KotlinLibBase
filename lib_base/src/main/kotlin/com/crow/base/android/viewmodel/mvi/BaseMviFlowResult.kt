package com.crow.base.android.viewmodel.mvi

fun interface BaseMviFlowResult<R : BaseMviIntent, T> { suspend fun onResult(value: T): R }
