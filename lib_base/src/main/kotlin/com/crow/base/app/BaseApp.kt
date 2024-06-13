package com.crow.base.app

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.datastore.preferences.PreferencesMapCompat
import androidx.datastore.preferences.PreferencesProto.PreferenceMap
import com.crow.base.tools.extensions.log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

val app = BaseApp.context

const val APP_DIALOG = "I1liIlLiONXUWBO"

open class BaseApp : Application() {

    companion object { lateinit var context: Application }

    @SuppressLint("RestrictedApi")
    override fun onCreate() {
        super.onCreate()
        context = this
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}