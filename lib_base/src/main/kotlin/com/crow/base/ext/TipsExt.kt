package com.crow.base.ext

import android.util.Log

const val CROW_TAG = "crowforkotlin"

fun Any?.log(tag: String = CROW_TAG, level: Int = Log.INFO) {
    Log.println(level, tag, this.toString())
}