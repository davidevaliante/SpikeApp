package com.musashi.claymore.spike.spike

import android.app.Application
import android.support.multidex.MultiDex
import com.google.firebase.database.FirebaseDatabase
import com.squareup.leakcanary.LeakCanary


class App : Application() {


    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...
        MultiDex.install(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
