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
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.AnimationSet
import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator
import com.bumptech.glide.Glide
import com.musashi.claymore.spike.spike.DetailedDescription
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import aqua.extensions.*
import com.musashi.claymore.spike.spike.R.id.*
import com.musashi.claymore.spike.spike.TrySlotActivity
import fromHtml
import kotlinx.android.synthetic.main.tip_card.view.*


class DetailRoot : AppCompatActivity() {
    // string a fine pagina
    private val footer = "<p>Se avete preso dimestichezza e volete sfruttare un ottimo&nbsp;<strong>Bonus di Benvenuto</strong>&nbsp;per giocare a questa slot, vi invito a registrarvi su questi casinò che dispongono dei migliori Bonus esclusivi.</p>"
    // classe d'esempio
    private val descrizioneEsempio = DetailedDescription(
            shortName ="pyramid",
    fullName="pyramidSlot",
    description="<p>La Slot&nbsp;<strong>Pyramid: Quest</strong>&nbsp;<strong>for Immortality&nbsp;</strong>è una slot a tema egiziano del produttore NetEnt. Ha 720 linee di pagamento, in modalità SuperPlay. Ossia qualsiasi combinazione di 3 o più simboli uguali da sinistra a destra è pagante.</p>" +
    "\n" +
    "<p>Non c’è un vero e proprio gioco Bonus da attivare, ma la caratteristica principale di questa slot è il <strong>Moltiplicatore a cascata</strong>. Ogni 3 pagamenti consecutivi, il moltiplicatore delle vincite viene incrementato. E’ possibile arrivare <strong>fino a x10</strong>. I Wild ossia i simboli Jolly possono apparire casualmente nei rulli 2, 3 e 4 unicamente dopo l’ottenimento di una vincita e successiva cascata.</p>\n" +
    "\n" +
    "<p>In questa pagina puoi provare la slot <strong>Pyramid Quest for Immortality&nbsp;Gratis&nbsp;</strong>in modalità a soldi finti. La modalità Demo serve a prendere dimestichezza con la slot prima di tentare la fortuna a soldi veri.&nbsp;Essendo questa una partita di “allenamento” a soldi finti, non è richiesta alcuna registrazione. Vi consiglio comunque di <strong>registrarvi al Casinò Online</strong> consigliato per <strong>sfruttare il Bonus di Benvenuto SENZA DEPOSITO</strong> esclusivo ottenibile tramite i link su questa pagina.</p>",
    imageLink="http://www.netentstalker.com/wp-content/uploads/2015/09/Pyramid-Quest-For-Immortality-NetEnt.jpg",
    embedLink="t",
    technicals= "Produttore: <strong>NetEnt</strong>@" +
    "RTP (percentuale di payout): <strong>96.48%</Strong>@" +
    "E’ una slot a <strong>singolo spin</strong>, ossia non da la possibilità di tenere le combinazioni che più ci piacciono, ma verrà pagato direttamente ogni Spin effettuato.@" +
    "Le linee di pagamento sono <strong>720</Strong>@" +
    "Funzione speciale <strong>Moltiplicatore Progressivo</strong>@" +
    "Funzione speciale <strong>Pagamenti a cascata</strong>",
    playTips= "Per questa slot consiglio un rapporto Bet/Budget di <strong>0,5/100</strong>. Ad esempio giocando 100€ il Bet ideale di partenza è tra 0,5€ e 1€.@" +
    "Partire testando la slot, se non dovesse rispondere da subito con vincite anche piccole in una cinquantina di Spin, è il caso di rimandare la partita a soldi veri.@" +
    "In caso di frequenti vincite buone (file o pagine intere) dobbiamo <strong>incrementare il Bet</strong> il più possibile in base alle nostre possibilità e tentare qualche tiro. In caso di esito negativo si potrà tornare a giocare con il Bet di partenza. Non di rado potremmo avere ottime combinazioni, con buone possibilità di vincita nei momenti caldi.@" +
    "Come per tutte le slot della NetEnt consiglio di <strong>non eccedere troppo con il Bet</strong> rispetto al Budget destinato al gioco. Queste slot hanno bisogno di effettuare numerosi Spin, ma in casi fortunati è possibile anche ottenere una vincita molto alta da 1000 e più volte il Bet.",
    relatedWebSites = "http://bit.ly/SNAIBONUS100@http://bit.ly/BonusLOTTOMATICA@")

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
            val tipsList = descrizioneEsempio.playTips?.split("@")
            tipsList?.forEach {
                val cardToAdd = LayoutInflater.from(this).inflate(R.layout.tip_card, tipsCardGroup, false)
                cardToAdd.tipsText.text = it.fromHtml()

                tipsCardGroup.addView(cardToAdd)
            }
        }

        // caricamento dati dalla classe finta
        Glide.with(this).load(descrizioneEsempio.imageLink).into(collapsingImg)
        Glide.with(this).load("https://seeklogo.com/images/A/AAMS_Timone_Gioco_Sicuro-logo-8525C3341F-seeklogo.com.png")
                .into(detail_aams_logo)
        descriptionText.text = descrizioneEsempio.description?.fromHtml()
        tecnicalsText.text = buildTecnicalString()
        addViewsToTipsLayout()
        footerText.text= footer.fromHtml()
        myToolbar.title="Pyramid: Quest for immortality"
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
        descrizioneEsempio.technicals?.split("@")?.forEach {
            s+="<p>- $it\n\n</p>"
        }
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
