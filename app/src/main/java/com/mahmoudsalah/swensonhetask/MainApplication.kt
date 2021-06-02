package com.mahmoudsalah.swansontask

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.widget.LinearLayout
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    companion object {
        lateinit var instance: MainApplication
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun scaleDialogue(_dialog: Dialog, act: Activity) {
        val display = act.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        //int height = size.y / 2;
        _dialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }


}