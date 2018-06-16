package com.musashi.claymore.spike.spike

import android.app.Application
import android.support.multidex.MultiDex

import com.google.firebase.firestore.FirebaseFirestoreSettings

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

    }
}
