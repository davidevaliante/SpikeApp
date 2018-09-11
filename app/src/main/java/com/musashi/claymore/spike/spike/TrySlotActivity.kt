package com.musashi.claymore.spike.spike

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import aqua.extensions.addFragment
import com.musashi.claymore.spike.spike.detail.TrySlotFragment
import kotlinx.android.synthetic.main.activity_try_slot.*

class TrySlotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_slot)
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        addFragment(slotContainer,TrySlotFragment.newInstance(intent.extras.getString("PLAY_LINK")))
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
