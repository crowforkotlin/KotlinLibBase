package com.crow.lib_base.ui.view

class StaticMarViewConfig {



    fun apply(apply : StaticMarViewConfig.() -> Unit) : StaticMarViewConfig {
        apply.invoke(this)
        return this
    }
}