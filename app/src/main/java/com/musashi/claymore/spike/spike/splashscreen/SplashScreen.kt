package com.musashi.claymore.spike.spike.splashscreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import aqua.extensions.Do
import aqua.extensions.goTo
import aqua.extensions.onClick
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.R.id.timeIndicator
import com.musashi.claymore.spike.spike.homepage.HomePage
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        timeIndicator.show()

        Do after 500 milliseconds  {
            goTo<HomePage>()
            finish()
        }

    }
}
