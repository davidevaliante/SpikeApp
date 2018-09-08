package com.musashi.claymore.spike.spike.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import aqua.extensions.*
import com.musashi.claymore.spike.spike.Slot
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.constants.DetailStatus
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    lateinit var viewModel : DetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        makeStatusbarTranslucent()

        setContentView(R.layout.activity_detail)




        viewModel = ViewModelProviders.of(this).get(DetailActivityViewModel::class.java)

        if (viewModel.state.value==null)
            viewModel.state.postValue(DetailStatus.INITIAL)

        viewModel.state.observe(this, Observer { changedState ->
            changedState?.let {
                changedState logFrom this
                when(changedState){
                    DetailStatus.INITIAL -> bindInitialFragment()
                    DetailStatus.PLAYING_DEMO -> bindTrySlotFragment()
                }
            }
        })
    }

    fun bindInitialFragment(){
        val fragment = DescriptionDetailFragment()
//        fragment.assignDataToDisplay(descrizioneEsempio)
        replaceFrag(detailActivityContainer,fragment)
    }

    fun bindTrySlotFragment(){
        val fragment = TrySlotFragment()
        replaceFrag(detailActivityContainer,fragment)
    }

    fun unbindTrySlotFragment(){
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel.state.postValue(DetailStatus.INITIAL)
    }

    override fun onBackPressed() {
        when(viewModel.state.value){
            DetailStatus.INITIAL -> super.onBackPressed()
            DetailStatus.PLAYING_DEMO -> unbindTrySlotFragment()
        }
    }
}
