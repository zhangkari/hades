package com.class100.yunshixun

import android.app.Application
import com.class100.atropos.AtAbilityManager
import com.class100.hades.http.HaHttpClient

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AtAbilityManager.initialize(this, BuildConfig.DEBUG)
        HaHttpClient.enableLog(BuildConfig.DEBUG)
    }
}