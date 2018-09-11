package com.musashi.claymore.spike.spike.homepage


import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.Do
import aqua.extensions.goTo
import aqua.extensions.log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.Slot
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.R.id.*
import com.musashi.claymore.spike.spike.SlotCard
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.search_card.view.*
import kotlinx.android.synthetic.main.slot_card.view.*
import java.util.*

class HomePage : AppCompatActivity() {

    var searchResults:MutableList<SlotCard> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/Mix%2Fslot-header-img-min-min.jpg?alt=media&token=6648de0a-3cd6-402f-9ada-a961cf893c2a")
                .into(slidingImageView)

        homeAppar.addOnOffsetChangedListener(collapseLayoutBehaviours())
        viewPager.adapter=TabsAdapter(supportFragmentManager, listOf(FirstFragment(),SecondFragment()))
        tabs.setupWithViewPager(viewPager)
        searchRc.layoutManager=LinearLayoutManager(this)
        searchRc.adapter=SearchSlotAdapter(searchResults,this)
        searchFieldEditTextHome.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Do.after(200).milliseconds {
                    if(s?.toString()?.length!! >=1) searchSlotByName(s?.toString())
                    else {
                        (searchRc.adapter as SearchSlotAdapter).updateList(emptyList())
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
                        (searchRc.adapter as SearchSlotAdapter).updateList(searchResults)
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

    class TabsAdapter(fragmentManager: FragmentManager, private val frags: List<Fragment>) :
            FragmentStatePagerAdapter(fragmentManager) {
        val tabTitles = listOf("Slot", "Bonus")
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

    class SearchSlotAdapter(var slotList: List<SlotCard>, val activity: Activity) : RecyclerView.Adapter<SearchSlotAdapter.SlotCardViewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotCardViewholder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
            return SlotCardViewholder(view)

        }

        override fun getItemCount(): Int = slotList.size

        override fun onBindViewHolder(holder: SlotCardViewholder, position: Int) {
            holder.bindView(slotList[position])
        }

        fun updateList(newList: List<SlotCard>) {
            this.slotList = newList
            notifyDataSetChanged()
        }

        inner class SlotCardViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(data: SlotCard) {
                itemView.searchCardTitle.text = data.name?.toLowerCase()?.capitalize()
                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("SLOT_ID", data.id)
                    (activity as AppCompatActivity).goTo<DetailRoot>(bundle)
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
                        searchFieldBottomLine.visibility = View.GONE
                    }
                }
                Math.abs(verticalOffset) == 0 -> {

                    if (toolbarAppName.visibility == View.VISIBLE) {
                        val t = Fade(Fade.MODE_IN)
                        t.duration = 400
                        TransitionManager.beginDelayedTransition(homeCoordinatot, t)
                        toolbarAppName.visibility = View.GONE
                        searchFieldBottomLine.visibility = View.VISIBLE
                    }
                }

                else -> {
                    // qualche punto in mezzo
                }
            }
        }
    }

    fun loadRandomImage() {

        val dilettaList = listOf(
                "http://slotmachinesgratisonline.com/wp-content/themes/SlotMachines/scripts/timthumb.php?src=http://slotmachinesgratisonline.com/wp-content/uploads/2013/05/Fowl_play_gold_2-292x242.jpg&w=292&h=242",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDWTXa9YR8BWwYdBg9jb-BXFndAq6jB4sJQDGuGBJDf3HQkBmtWQ",
                "https://images.images4us.com/888Casino_It/It/slots_teaser-1479629038684.jpg",
                "http://www.slotmachinesitalia.it/wp-content/uploads/2018/06/come-si-gioca-slot-machine.jpg",
                "https://it.slotsup.com/wp-content/uploads/default/joker-pro-netent-slot-machine.png")

        val random = Random().nextInt(dilettaList.size - 1)
        val randomlyPickedImage = dilettaList[random]
        if (!this.isFinishing || !this.isDestroyed)
            Glide.with(this).load(randomlyPickedImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(slidingImageView)


    }
}
