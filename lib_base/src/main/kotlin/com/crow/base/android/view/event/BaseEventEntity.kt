package com.crow.base.android.view.event

data class BaseEventEntity<T>(
    val mType: T,
    val mBaseEvent: BaseEvent,
)
