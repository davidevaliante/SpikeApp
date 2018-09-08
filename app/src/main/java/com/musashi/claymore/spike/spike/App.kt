package com.musashi.claymore.spike.spike

import android.app.Application
import android.support.multidex.MultiDex
import com.google.firebase.database.FirebaseDatabase


class App : Application() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
