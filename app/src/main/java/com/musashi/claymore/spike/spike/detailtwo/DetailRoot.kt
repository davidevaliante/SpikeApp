package com.musashi.claymore.spike.spike.detailtwo



import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import com.musashi.claymore.spike.spike.R
import kotlinx.android.synthetic.main.activity_detail_root.*
import kotlinx.android.synthetic.main.detail_scrollview.*
import android.support.transition.*
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.*
import com.bumptech.glide.Glide
import com.musashi.claymore.spike.spike.Slot
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import aqua.extensions.*
import com.musashi.claymore.spike.spike.TrySlotActivity
import fromHtml
import kotlinx.android.synthetic.main.tip_card.view.*


class DetailRoot : AppCompatActivity() {


    // container posizione iniziale Floating action button
    private var fabInitialPosition = IntArray(2)
    private val point : DisplayMetrics by lazy { DisplayMetrics() }

    // per il controllo movimento Fab
    private var expandedModeShouldBePlayed = true
    private var collapsedModeShouldBePlayed = true
    // per rimuovere/aggiungere il listner a seconda del lifecycle
    private val customOffsetChangedListener by lazy { collapseLayoutBehaviours() }

    private val screenDensity by lazy { resources.displayMetrics.density }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusbarTranslucent()
        setContentView(R.layout.activity_detail_root)

        bindDataToUI()
        layoutSetup()
        setAllOnClickListners()

        playImageOverLayEffect()

        appBarLayout.addOnOffsetChangedListener(customOffsetChangedListener)

    }

    override fun onDestroy() {
        appBarLayout.removeOnOffsetChangedListener(customOffsetChangedListener)
        super.onDestroy()
    }

    override fun onResume() {
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onResume()
    }

    private fun bindDataToUI(){
        fun addViewsToTipsLayout(){
//            val tipsList = descrizioneEsempio.playTips?.split("@")
//            tipsList?.forEach {
//                val cardToAdd = LayoutInflater.from(this).inflate(R.layout.tip_card, tipsCardGroup, false)
//                cardToAdd.tipsText.text = it.fromHtml()
//
//                tipsCardGroup.addView(cardToAdd)
//            }
        }

        // caricamento dati dalla classe finta
//        Glide.with(this).load(descrizioneEsempio.imageLink).into(collapsingImg)
//        Glide.with(this).load("https://seeklogo.com/images/A/AAMS_Timone_Gioco_Sicuro-logo-8525C3341F-seeklogo.com.png")
//                .into(detail_aams_logo)
//        descriptionText.text = descrizioneEsempio.description?.fromHtml()
//        tecnicalsText.text = buildTecnicalString()
//        addViewsToTipsLayout()
//        footerText.text= footer.fromHtml()
//        myToolbar.title="Pyramid: Quest for immortality"
    }

    private fun layoutSetup(){
        windowManager.defaultDisplay.getMetrics(point)
        // funzioni per settare il layout in modo giusto per API < 21
        complexScrollView.verticalScrollbarPosition = View.SCROLLBAR_POSITION_LEFT
        setSupportActionBar(myToolbar)
        setToolbarFont()
        fab.getLocationOnScreen(fabInitialPosition)
    }

    private fun setAllOnClickListners(){
        fabShare.setOnClickListener { showInfo("share pagina su whatsapp/telegram/Facebook") }
        fabYoutube.setOnClickListener { showInfo("va alla pagina con il video di youtube correlato") }
        fab.setOnClickListener { goTo<TrySlotActivity>() }
        backArrow.setOnClickListener {
            appBarLayout.removeOnOffsetChangedListener(customOffsetChangedListener)
            finish()
        }
        playButtonCenter.setOnClickListener { goTo<TrySlotActivity>() }
        descriptionHeader.setOnClickListener {
            if(descriptionText.visibility==View.GONE) {
                descriptionText.visibility = View.VISIBLE
                descriptionHeader.setCompoundDrawablesWithIntrinsicBounds(null,null, getSupportDrawable(R.drawable.ic_up_arrow_red) ,null)
            } else {
                descriptionText.visibility=View.GONE
                descriptionHeader.setCompoundDrawablesWithIntrinsicBounds(null,null, getSupportDrawable(R.drawable.ic_down_arrow_red) ,null)
            }
        }
        tecnicalsHeader.setOnClickListener {
            if (tecnicalsText.visibility == View.GONE) {
                tecnicalsText.visibility = View.VISIBLE
                tecnicalsHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getSupportDrawable(R.drawable.ic_up_arrow_red), null)
            } else {
                tecnicalsText.visibility = View.GONE
                tecnicalsHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getSupportDrawable(R.drawable.ic_down_arrow_red), null)
            }
        }
        tipsHeader.setOnClickListener {
            if(tipsCardGroup.visibility==View.GONE) {
                tipsCardGroup.visibility = View.VISIBLE
                tipsHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getSupportDrawable(R.drawable.ic_up_arrow_red), null)
            } else {
                tipsCardGroup.visibility = View.GONE
                tipsHeader.setCompoundDrawablesWithIntrinsicBounds(null,null, getSupportDrawable(R.drawable.ic_down_arrow_red) ,null)
            }
        }
    }

    private fun collapseLayoutBehaviours():AppBarLayout.OnOffsetChangedListener{

        fun backArrowFadeIn(){
            Do after 100 milliseconds {
                val t = Fade(Fade.MODE_IN)
                t.duration=700
                TransitionManager.beginDelayedTransition(coordinator,t)
                backArrow.visibility= View.VISIBLE
            }
        }
        fun backArrowFadeOut(){
            Do after 100 milliseconds {
                val t = Fade(Fade.MODE_OUT)
                t.duration=700
                TransitionManager.beginDelayedTransition(coordinator,t)
                backArrow.visibility= View.INVISIBLE
            }
        }
        fun searchBarFadeIn(){
            Do after 300 milliseconds {
                val t = Fade(Fade.MODE_IN)
                t.duration=1000
                TransitionManager.beginDelayedTransition(coordinator,t)
                searchFieldGroup.visibility=View.VISIBLE
            }
        }
        fun searchBarFadeOut(){
            Do after 10 milliseconds {
                val t = Fade(Fade.MODE_OUT)
                t.duration=100
                TransitionManager.beginDelayedTransition(coordinator,t)
                searchFieldGroup.visibility=View.GONE
            }
        }
        fun reverseGradientFadeIn(){
            Do after 200 milliseconds {
                val t = Fade(Fade.MODE_IN)
                t.duration=1600
                TransitionManager.beginDelayedTransition(coordinator,t)
                reverse_gradient.visibility=View.VISIBLE
            }
        }
        fun reverseGradientFadeOut(){
            Do after 100 milliseconds {
                val t = Fade(Fade.MODE_OUT)
                t.duration=800
                TransitionManager.beginDelayedTransition(coordinator,t)
                reverse_gradient.visibility=View.INVISIBLE
            }
        }
        fun hidePlayButtonCenter(){
            playButtonCenter.visibility=View.GONE
        }
        fun showPlayButtonCenter(){
            val t = Fade(Fade.MODE_IN)
            t.duration=700
            TransitionManager.beginDelayedTransition(coordinator,t)
            playButtonCenter.visibility=View.VISIBLE
        }
        fun fabGroupAnimateIn(){
            Do after 200 milliseconds {
                val t = Fade(Fade.MODE_IN)
                t.duration=1600
                TransitionManager.beginDelayedTransition(coordinator,t)
                fabGroup.visibility=View.VISIBLE
                fab.visibility=View.VISIBLE
                fabYoutube.visibility=View.VISIBLE
                fabShare.visibility=View.VISIBLE
            }

        }
        fun fabGroupAnimateOut(){
            Do after 200 milliseconds {
                val t = Fade(Fade.MODE_OUT)
                TransitionManager.beginDelayedTransition(coordinator,t)
                fabGroup.visibility=View.GONE
                fab.visibility=View.GONE
                fabYoutube.visibility=View.GONE
                fabShare.visibility=View.GONE
            }
        }




        return AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                Math.abs(verticalOffset) - appBarLayout.totalScrollRange==0 -> {
                    if(expandedModeShouldBePlayed){
                        expandedModeShouldBePlayed=false
                        collapsedModeShouldBePlayed=true
                        "collapsed" log "COLLAPSEBAR"
                        backArrowFadeIn()
                        searchBarFadeOut()
                        reverseGradientFadeOut()
                        hidePlayButtonCenter()
                        fabGroupAnimateIn()
                    }
                }
                verticalOffset == 0 -> {
                    if(collapsedModeShouldBePlayed){
                        expandedModeShouldBePlayed=true
                        collapsedModeShouldBePlayed=false
                        backArrowFadeOut()
                        reverseGradientFadeIn()
                        showPlayButtonCenter()
                        searchBarFadeIn()
                        fabGroupAnimateOut()
                    }
                }
                else -> {
                    // qualche punto in mezzo
                }
            }
        }
    }

    private fun setToolbarFont(){
        collapsingToolbarLayout
        .setCollapsedTitleTypeface(Typeface.createFromAsset(this.assets, "raleway_medium.ttf"))

        collapsingToolbarLayout
        .setExpandedTitleTypeface(Typeface.createFromAsset(this.assets, "raleway_medium.ttf"))
    }

    private fun playImageOverLayEffect(){
        // overlay
        Do after 700 milliseconds {
            val t = Fade(Fade.MODE_IN)
            t.duration=700
            TransitionManager.beginDelayedTransition(coordinator,t)
            collapsingImgOverlay.visibility= View.VISIBLE
        }
    }

    private fun buildTecnicalString():Spanned{
        var s =""
//        descrizioneEsempio.technicals?.split("@")?.forEach {
//            s+="<p>- $it\n\n</p>"
//        }
        return s.fromHtml()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    // helpers
    private fun Int.fromDpiToPixelsBasedOnScreenSize()=Math.round(this*screenDensity)
}
