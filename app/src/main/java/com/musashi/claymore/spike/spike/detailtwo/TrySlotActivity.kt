package com.musashi.claymore.spike.spike.detailtwo

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import aqua.extensions.Do
import aqua.extensions.addFragment
import aqua.extensions.log
import com.musashi.claymore.spike.spike.Bonus
import com.musashi.claymore.spike.spike.R
import kotlinx.android.synthetic.main.activity_try_slot.*
import kotlinx.android.synthetic.main.activity_try_slot.view.*
import random
import java.util.*

class TrySlotActivity : AppCompatActivity() {
    val PLAY_LINK = "PLAY_LINK"
    val BONUS_LIST = "BONUS_LIST"
    lateinit var bonusList:HashMap<String,Bonus>
    lateinit var lastBonus:Bonus
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_slot)
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        bonusList = intent.extras.getSerializable(BONUS_LIST) as HashMap<String, Bonus>
        bonusList.toString() log "HASHMAP"
        addFragment(slotContainer, TrySlotFragment.newInstance(intent.extras.getString(PLAY_LINK)))
        startAdCycle(bonusList)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    fun startAdCycle(bonusList:HashMap<String,Bonus>, interval:Int=15){
        Do.after(interval).seconds {
            val size = bonusList.values.toList().size
            val randomBonus = bonusList.values.toList()[(0 until size).random()]
            ad.adTitle.text="Prova il bonus ${randomBonus.name}"
            ad.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(randomBonus.link)
                startActivity(intent)
            }
            ad.closeAd.setOnClickListener {
                ad.visibility=View.GONE
            }
            ad.visibility=View.VISIBLE
            startAdCycle(bonusList)
        }
    }
}
