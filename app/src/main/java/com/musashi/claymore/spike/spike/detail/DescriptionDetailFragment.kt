package com.musashi.claymore.spike.spike.detail


import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.transition.*
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import aqua.extensions.*
import com.bumptech.glide.Glide
import com.musashi.claymore.spike.spike.DetailedDescription
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.R.id.mainDescription
import com.musashi.claymore.spike.spike.SlotDetailDisplayer
import com.musashi.claymore.spike.spike.constants.DetailStatus
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_description_detail.*
import kotlinx.android.synthetic.main.fragment_description_detail.view.*
import java.time.Duration


class DescriptionDetailFragment : Fragment(),SlotDetailDisplayer {

    lateinit var rootLayout:ViewGroup
    var data : DetailedDescription = DetailedDescription()
    var isTransitioning : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vg = inflateInContainer(R.layout.fragment_description_detail, container)
        rootLayout=vg

        bindDetailDataToUI(data,vg)
        setAllOnClickListeners()
        playEnterAnimation()

        return vg
    }

    fun playEnterAnimation(){
        //Viene chiamata dopo 1100 ms e dura 1300 ms TOT = 2400 ms
        fun arrowFadeInAnimation(startafter:Int, duration: Long, interval:Int){

            val interpolator =  FastOutLinearInInterpolator()
            val t = Fade()
            t.duration=duration
            t.interpolator= interpolator


            Do after startafter milliseconds {
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.firstArrow.visibility=View.VISIBLE
            }

            Do after startafter+interval milliseconds {
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.secondArrow.visibility=View.VISIBLE
            }

            Do after startafter+interval*2 milliseconds {
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.thirdArrow.visibility=View.VISIBLE
            }

            Do after startafter+interval*3 milliseconds {
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.fourtArrow.visibility=View.VISIBLE
            }

        }

        // Tutto dura 1100 ms
        fun buttonSlideInFromLeftAnimation(){
            // Tutto dura 1100 ms
            Do after 800 milliseconds {
                val t = Slide(Gravity.RIGHT)
                t.duration=300
                t.interpolator= (FastOutSlowInInterpolator())
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.descriptionButtonGroup.visibility=View.VISIBLE

            }

            Do after 600 milliseconds {
                val t = Slide(Gravity.RIGHT)
                t.duration=300
                t.interpolator= (FastOutSlowInInterpolator())
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.tecnicalButtonGroup.visibility=View.VISIBLE

            }

            Do after 400 milliseconds {
                val t = Slide(Gravity.RIGHT)
                t.duration=300
                t.interpolator= (FastOutSlowInInterpolator())
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.tipsButtonGroup.visibility=View.VISIBLE

            }

            Do after 200 milliseconds {
                val t = Slide(Gravity.RIGHT)
                t.duration=300
                t.interpolator= (FastOutSlowInInterpolator())
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.relatedLinksButtonGroup.visibility=View.VISIBLE

            }
        }

        fun imageAnimation(){
            Do after 1200 milliseconds {
                val interpolator =  FastOutLinearInInterpolator()
                val t = Fade()
                t.duration=800
                t.interpolator= interpolator
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.imageOverlay.visibility=View.VISIBLE
            }
        }

        fun animatePlayButton(){
            Do after 1200 milliseconds {
                val interpolator =  FastOutLinearInInterpolator()
                val t = Fade()
                t.duration=1500
                t.interpolator= interpolator
                TransitionManager.beginDelayedTransition(rootLayout,t)
                rootLayout.playButton.visibility=View.VISIBLE
                rootLayout.line.visibility=View.VISIBLE
            }
        }

        buttonSlideInFromLeftAnimation()
        arrowFadeInAnimation(startafter = 1100,duration = 1000, interval = 200)
        imageAnimation()
        animatePlayButton()
    }

    fun setAllOnClickListeners(){
        // CLICK LISTENERS
        rootLayout.img.setOnClickListener {
            // cambia solo lo stato del viewmodel, poi è l'activity a fare quello che serve (instanziare il fragment)
            (activity as DetailActivity).viewModel.updateState(DetailStatus.PLAYING_DEMO)
            // richiede il landscape (potrebbe e forse dovrebbe essere chiamato direttamente dall'activity)
            activity?.requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        rootLayout.descriptionButtonGroup.setOnClickListener {
            rootLayout.mainDescription.expandOrCollapse() // 100 millisecondi durata di base
        }

        rootLayout.tecnicalButtonGroup.setOnClickListener {
            rootLayout.tecnical.expandOrCollapse() // 100 millisecondi durata di base
        }

        rootLayout.tipsButtonGroup.setOnClickListener {
            rootLayout.tips.expandOrCollapse() // 100 millisecondi durata di base
        }

        rootLayout.relatedLinksButtonGroup.setOnClickListener {
            rootLayout.relatedLinks.expandOrCollapse() // 100 millisecondi durata di base
        }
    }

    fun TextView.expandOrCollapse(expandDuration:Long=100, collapseDuration:Long=100){

        // funzione interna con funzione interna (solo per questione di ordine
        fun TextView.collapseButtonTransition(duration:Long=100){
            fun switchToDownArrowRelativeTo(textView: TextView){
                val downIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_down_arrow)
                when(textView){
                    rootLayout.mainDescription -> rootLayout.firstArrow.setImageDrawable(downIcon)
                    rootLayout.tecnical -> rootLayout.secondArrow.setImageDrawable(downIcon)
                    rootLayout.tips -> rootLayout.thirdArrow.setImageDrawable(downIcon)
                    rootLayout.relatedLinks -> rootLayout.fourtArrow.setImageDrawable(downIcon)
                }
            }
            rootView.nestedScrollView.smoothScrollTo(0, 0)

            val t = AutoTransition()
            t.duration=duration
            t.addListener(transitionEndedNotifier())
            TransitionManager.beginDelayedTransition(rootLayout,t)
            rootView.headerImgGroup.visibility=View.VISIBLE
            rootView.firstArrow.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_down_arrow))
            this.invertVisibility()
            switchToDownArrowRelativeTo(this)
        }
        fun TextView.expandandButtonTransition(duration:Long=100){
            fun switchToUpArrowRelativeTo(textView: TextView){
                val upArrow = ContextCompat.getDrawable(activity!!, R.drawable.ic_up_arrow)
                when(textView){
                    rootLayout.mainDescription -> rootLayout.firstArrow.setImageDrawable(upArrow)
                    rootLayout.tecnical -> rootLayout.secondArrow.setImageDrawable(upArrow)
                    rootLayout.tips -> rootLayout.thirdArrow.setImageDrawable(upArrow)
                    rootLayout.relatedLinks -> rootLayout.fourtArrow.setImageDrawable(upArrow)
                }
            }

            val scrollTo = this.top
            rootView.nestedScrollView.smoothScrollTo(0, scrollTo)

            val t = AutoTransition()
            t.duration=duration
            t.addListener(transitionEndedNotifier())
            TransitionManager.beginDelayedTransition(rootLayout,t)
            rootView.headerImgGroup.visibility=View.GONE
            this.invertVisibility()
            switchToUpArrowRelativeTo(this)
        }



        if (this.visibility == View.GONE && !isTransitioning)
            this.expandandButtonTransition(expandDuration)
        else
            this.collapseButtonTransition(collapseDuration)
    }

    fun bindDetailDataToUI(data : DetailedDescription, vg:ViewGroup){
        Glide.with(this).load(data.imageLink).into(vg.img)

        vg.mainDescription.text = data.description
        var tecnicalString=""
        data.technicals?.split("@")?.forEach { tecnicalString += "- $it\n" }
        vg.tecnical.text = tecnicalString
        var tipsString = ""
        data.playTips?.split("@")?.forEach { tipsString += "- $it\n" }
        vg.tips.text = tipsString
    }

    override fun assignDataToDisplay(data: DetailedDescription){
        this.data = data
    }

    fun transitionEndedNotifier():Transition.TransitionListener{
        return object : Transition.TransitionListener{
            override fun onTransitionEnd(transition: Transition) {
                isTransitioning=false
            }

            override fun onTransitionResume(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionStart(transition: Transition) {
                isTransitioning=true
            }

        }
    }

}
