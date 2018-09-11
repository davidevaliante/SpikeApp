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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.constants.ImgSize
import com.musashi.claymore.spike.spike.getImageLinkFromName
import fromHtml
import kotlinx.android.synthetic.main.tip_card.view.*
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.musashi.claymore.spike.spike.SlotCard
import com.musashi.claymore.spike.spike.homepage.HomePage
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.bonus_card.view.*


class DetailRoot : AppCompatActivity() {

    // container posizione iniziale Floating action button
    private var fabInitialPosition = IntArray(2)
    private val point : DisplayMetrics by lazy { DisplayMetrics() }
    // per il controllo movimento Fab
    private var expandedModeShouldBePlayed = true
    private var collapsedModeShouldBePlayed = true
    // per rimuovere/aggiungere il listner a seconda del lifecycle
    private val customOffsetChangedListener by lazy { collapseLayoutBehaviours() }
    private val AAMS_LOGO_URL = "https://seeklogo.com/images/A/AAMS_Timone_Gioco_Sicuro-logo-8525C3341F-seeklogo.com.png"
    private val screenDensity by lazy { resources.displayMetrics.density }
    var searchResults:MutableList<SlotCard> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusbarTranslucent()
        setContentView(R.layout.activity_detail_root)
        val id = intent.extras.getString("SLOT_ID")
        getSlotWithId(id)
        layoutSetup()
        setAllOnClickListners()
        playImageOverLayEffect()
        appBarLayout.addOnOffsetChangedListener(customOffsetChangedListener)
        detailSearchRc.layoutManager= LinearLayoutManager(this)
        detailSearchRc.adapter= HomePage.SearchSlotAdapter(searchResults, this)
        searchFieldEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Do.after(200).milliseconds {
                    if(s?.toString()?.length!! >=1) searchSlotByName(s?.toString())
                    else {
                        (detailSearchRc.adapter as HomePage.SearchSlotAdapter).updateList(emptyList())
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onStop() {
        super.onStop()
        searchFieldEditText.setText("")
        (detailSearchRc.adapter as HomePage.SearchSlotAdapter).updateList(searchResults)
    }

    override fun onDestroy() {
        appBarLayout.removeOnOffsetChangedListener(customOffsetChangedListener)
        super.onDestroy()
    }

    override fun onResume() {
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onResume()
    }

    private fun searchSlotByName(s:String){
        val string = s.toUpperCase()
        FirebaseDatabase.getInstance().reference.child("SlotsCard").child("it")
                .orderByChild("name").startAt(string).endAt("$string\uf8ff")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        searchResults.removeAll { it!=null }
                        snapshot.children.mapNotNullTo(searchResults, {
                            val result = it.getValue<SlotCard>(SlotCard::class.java)
                            result?.id=it.key
                            result
                        })
                        (detailSearchRc.adapter as HomePage.SearchSlotAdapter).updateList(searchResults)
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    private fun getSlotWithId(id:String?){
        FirebaseDatabase.getInstance().reference.child("Slots/it/$id")
        .addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue(Slot::class.java)!= null) {
                    val currentSlot = snapshot.getValue(Slot::class.java)
                    if(currentSlot!=null){
                        // per i pulsanti
                        val bundle = Bundle()
                        bundle.putString("PLAY_LINK",currentSlot.linkPlay)
                        bundle.putSerializable("BONUS_LIST", currentSlot.bonus)
                        playButtonCenter.setOnClickListener { goTo<TrySlotActivity>(bundle) }
                        fab.setOnClickListener { goTo<TrySlotActivity>(bundle) }

                        fabYoutube.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(currentSlot.linkYoutube)
                            startActivity(intent)
                        }

                        // per l'UI
                        bindDataToUI(currentSlot)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun bindDataToUI(slot: Slot?){
        // aggiunge dinamicamente le carte dei consigli
        fun addViewsToTipsLayout(){
            val tipsList = slot?.tips?.split("@")
            tipsList?.slice(1 until (tipsList.size))?.forEach {
                val cardToAdd = LayoutInflater.from(this).inflate(R.layout.tip_card, tipsCardGroup, false)
                cardToAdd.tipsText.text = it.fromHtml()
                tipsCardGroup.addView(cardToAdd)
            }
        }

        // aggiunge dinamicamente le carte dei bonus
        fun addViewsToBonusLayout(){
            slot?.bonus?.entries?.forEach {
                val currentBonus = it.value
                val cardToAdd = LayoutInflater.from(this).inflate(R.layout.bonus_card, bonusGroup, false)
                cardToAdd.bonusTitle.text=currentBonus.name
                cardToAdd.bonusDescription.text=currentBonus.bonus
                Glide.with(this).load(currentBonus.getImageLinkFromName(ImgSize.BIG)).into(cardToAdd.bonusImage)
                cardToAdd.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(currentBonus.link)
                    startActivity(intent)
                }
                bonusGroup.addView(cardToAdd)
            }
        }


        Glide.with(this).load(slot?.getImageLinkFromName(size = ImgSize.BIG)).into(collapsingImg)
        Glide.with(this).load(AAMS_LOGO_URL).into(detail_aams_logo)
        descriptionText.text = slot?.description?.fromHtml()
        tecnicalsText.text = buildTecnicalString(slot?.tecnicals)
        addViewsToTipsLayout()
        addViewsToBonusLayout()
        supportActionBar?.title=slot?.name?.toLowerCase()?.capitalize()
    }

    private fun layoutSetup(){
        windowManager.defaultDisplay.getMetrics(point)
        // funzioni per settare il layout in modo giusto per API < 21
        complexScrollView.verticalScrollbarPosition = View.SCROLLBAR_POSITION_LEFT
        myToolbar.title=""
        setSupportActionBar(myToolbar)
        setToolbarFont()
        fab.getLocationOnScreen(fabInitialPosition)
    }

    private fun setAllOnClickListners(){
        fabShare.setOnClickListener { showInfo("share pagina su whatsapp/telegram/Facebook") }
        backArrow.setOnClickListener {
            appBarLayout.removeOnOffsetChangedListener(customOffsetChangedListener)
            finish()
        }
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
                        hidePlayButtonCenter()
                        fabGroupAnimateIn()
                    }
                }
                verticalOffset == 0 -> {
                    if(collapsedModeShouldBePlayed){
                        expandedModeShouldBePlayed=true
                        collapsedModeShouldBePlayed=false
                        backArrowFadeOut()
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

    private fun buildTecnicalString(string: String?):Spanned{
        var s =""
        string?.slice(1 until string.length)?.split("$")?.forEach {
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
