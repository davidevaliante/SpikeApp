package com.musashi.claymore.spike.spike.homepage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import aqua.extensions.goTo
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.detail.DetailActivity
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        layout_light.setOnClickListener { goTo<DetailActivity>() }
        layout_dark.setOnClickListener { goTo<DetailRoot>() }
    }
}
