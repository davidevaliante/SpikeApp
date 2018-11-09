package com.musashi.claymore.spike.spike.homepage


import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import aqua.extensions.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.crashlytics.android.Crashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.*
import com.musashi.claymore.spike.spike.R.id.*
import com.musashi.claymore.spike.spike.constants.ImgSize
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import com.musashi.claymore.spike.spike.extensions.FixAppBarLayoutBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_root.*
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.popular_slot_card.view.*
import kotlinx.android.synthetic.main.search_card.view.*
import kotlinx.android.synthetic.main.slot_card.view.*
import java.util.*

class HomePage : AppCompatActivity() {

    var searchResults:MutableList<SlotCard> = mutableListOf()
    private val customSearchListener by lazy {searchSlot()}
    private var isSearchingSlot=false
    private val HEADER_IMG_URL = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/Mix%2Fslot-header-img-min-min.jpg?alt=media&token=6648de0a-3cd6-402f-9ada-a961cf893c2a"
    private var popularList = mutableListOf<SlotCard?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // makeSlotsMenuNameUpperCase()

        val node= FirebaseDatabase.getInstance().reference.child("SlotsCard").child("it")

        node.orderByChild("isPopular").equalTo(true).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                for(slot in data.children){
                    val s = slot.getValue(SlotCard::class.java)
                    s?.id = slot.key
                    popularList.add(s)
                    (popularSlotsRc.adapter as PopularSlotAdapter).updateList(popularList)
                }
            }
        })

        // header
        (homeAppar.layoutParams as CoordinatorLayout.LayoutParams).behavior = FixAppBarLayoutBehavior()
        homeAppar.addOnOffsetChangedListener(collapseLayoutBehaviours())
        Glide.with(this).load(HEADER_IMG_URL).thumbnail(0.1f).into(slidingImageView)

        // tabs
        viewPager.adapter=TabsAdapter(supportFragmentManager, listOf(FirstFragment(),SecondFragment(), ThirdFragment()))
        tabs.setupWithViewPager(viewPager)

        // recyclerview
        searchRc.layoutManager=LinearLayoutManager(this)
        searchRc.adapter=SearchSlotAdapter(searchResults,this)
        searchFieldEditTextHome.addTextChangedListener(customSearchListener)

        popularSlotsRc.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        popularSlotsRc.adapter= PopularSlotAdapter(popularList, this)
        popularSlotsRc.hasFixedSize()
        popularSlotsRc.isNestedScrollingEnabled=true
    }

    private fun makeSlotsMenuNameUpperCase(){
        val fb = FirebaseDatabase.getInstance().reference

        fb.child("SlotsMenu").child("it").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snap: DataSnapshot) {
                val x = mutableListOf<SlotMenu>()
                snap.children.forEach {
                    val x = it.getValue(SlotMenu::class.java)
                    x?.name = x?.name?.toUpperCase()
                    val k = it?.key
                    fb.child("SlotsMenu").child("it").child("$k").setValue(x)
                }
                x.forEach { it?.logFrom(this@HomePage) }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun searchSlotByName(s:String){
        isSearchingSlot=true
        homeSearchIndicator.smoothToShow()
        val string = s.toUpperCase()
        FirebaseDatabase.getInstance().reference.child("SlotsMenu").child("it")
                .orderByChild("name").startAt(string).endAt("$string" +"\uf8ff")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        searchResults.removeAll { it!=null }
                        snapshot.children.forEach {
                            (it.getValue(SlotMenu::class.java))!!.logFrom(this@HomePage)
                        }
                        snapshot.children.mapNotNullTo(searchResults) {
                            val result = it.getValue<SlotCard>(SlotCard::class.java)
                            result?.id=it.key
                            result
                        }
                        (searchRc.adapter as SearchSlotAdapter).updateList(searchResults)
                        isSearchingSlot=false
                        homeSearchIndicator.smoothToHide()
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        searchFieldEditTextHome.setText("")
        (searchRc.adapter as SearchSlotAdapter).updateList(searchResults)
    }

    private fun searchSlot() : TextWatcher {
        return object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                // se non sta facendo già una richiesta
                Do.after(300).milliseconds {
                    if (!isSearchingSlot)
                        if (s != null && s.isNotEmpty())
                            searchSlotByName(s?.toString())
                        else {
                            (searchRc.adapter as HomePage.SearchSlotAdapter).updateList(emptyList())
                        }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

class TabsAdapter(fragmentManager: FragmentManager, private val frags: List<Fragment>) :
        FragmentStatePagerAdapter(fragmentManager) {
    val tabTitles = listOf("Slot", "Bonus", "Articoli")
    // 2
    override fun getItem(position: Int): Fragment {
        return frags[position]
    }

    // 3
    override fun getCount(): Int {
        return frags.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}

class PopularSlotAdapter(var slotList: List<SlotCard?>, val activity: Activity) : RecyclerView.Adapter<PopularSlotAdapter.PopularSlotViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularSlotViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_slot_card, parent, false)
        return PopularSlotViewholder(view)
    }

    override fun getItemCount(): Int = slotList.size

    override fun onBindViewHolder(holder: PopularSlotViewholder, position: Int) {
        holder.bindView(slotList[position])
    }

    fun updateList(newList: List<SlotCard?>) {
        this.slotList=newList
        notifyDataSetChanged()
    }

    inner class PopularSlotViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: SlotCard?) {
            itemView.popularSlotTitle.text = data?.name?.toLowerCase()?.capitalize()
            Picasso.get()
                    .load(data?.getImageLinkFromName())
                    .resize(150, 150)
                    .centerCrop()
                    .into(itemView.popularSlotImage)
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("SLOT_ID", data?.id)
                (activity as AppCompatActivity).goTo<DetailRoot>(bundle)
            }
        }
    }
}

class SearchSlotAdapter(var slotList: List<SlotCard>, val activity: Activity)
    : RecyclerView.Adapter<SearchSlotAdapter.SlotCardViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotCardViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        return SlotCardViewholder(view)
    }

    override fun getItemCount(): Int = slotList.size

    override fun onBindViewHolder(holder: SlotCardViewholder, position: Int) {
        holder.bindView(slotList[position])
    }

    fun updateList(newList: List<SlotCard>) {
        this.slotList=newList
        notifyDataSetChanged()
    }

    inner class SlotCardViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: SlotCard) {
            itemView.apply {
                if (data?.name?.length!! > 20)
                    searchCardTitle.text = "${data.name?.toLowerCase()?.capitalize()?.take(20)}..."
                else
                    searchCardTitle.text = "${data.name?.toLowerCase()?.capitalize()?.take(20)}"
                searchCardProducer.text = data.producer
                Picasso.get()
                        .load(data?.getImageLinkFromName())
                        .resize(150, 150)
                        .centerCrop()
                        .into(searchCardImage)
                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("SLOT_ID", data.id)
                    (activity as AppCompatActivity).goTo<DetailRoot>(bundle)
                }
            }

        }
    }
}

private fun collapseLayoutBehaviours(): AppBarLayout.OnOffsetChangedListener {

    return AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        when {
            Math.abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                if (toolbarAppName.visibility == View.GONE) {
                    val t = Fade(Fade.MODE_IN)
                    t.duration = 600
                    t.startDelay = 200
                    t.interpolator = FastOutSlowInInterpolator()
                    TransitionManager.beginDelayedTransition(homeCoordinatot, t)
                    toolbarAppName.visibility = View.VISIBLE
                    searchFieldEditTextHome.visibility=View.VISIBLE
                }
            }
            Math.abs(verticalOffset) == 0 -> {

                if (toolbarAppName.visibility == View.VISIBLE) {
                    val t = Fade(Fade.MODE_IN)
                    t.duration = 400
                    TransitionManager.beginDelayedTransition(homeCoordinatot, t)
                    toolbarAppName.visibility = View.GONE
                    searchFieldEditTextHome.visibility=View.GONE
                }
            }

            else -> {
                // qualche punto in mezzo
            }
        }
    }
}


}
