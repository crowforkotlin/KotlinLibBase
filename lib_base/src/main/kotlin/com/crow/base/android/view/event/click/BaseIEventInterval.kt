package com.crow.base.android.view.event.click

import com.crow.base.android.view.event.BaseEventEntity

// 点击事件接口
fun interface BaseIEventInterval<T> { fun onIntervalOk(baseEventEntity: BaseEventEntity<T>) }
