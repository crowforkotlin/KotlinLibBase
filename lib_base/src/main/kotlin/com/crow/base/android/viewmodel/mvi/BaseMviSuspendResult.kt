package com.crow.base.android.viewmodel.mvi

fun interface BaseMviSuspendResult<T> { suspend fun onResult(value: T) }