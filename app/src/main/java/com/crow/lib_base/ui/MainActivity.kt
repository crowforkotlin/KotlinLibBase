package com.crow.lib_base.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crow.lib_base.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main)
    }
}