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
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.goTo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.musashi.claymore.spike.spike.Slot
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.slot_card.view.*
import java.util.*

class HomePage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/Mix%2Fslot-header-img-min-min.jpg?alt=media&token=6648de0a-3cd6-402f-9ada-a961cf893c2a")
                .into(slidingImageView)

        homeAppar.addOnOffsetChangedListener(collapseLayoutBehaviours())
        viewPager.adapter=TabsAdapter(supportFragmentManager, listOf(FirstFragment(),SecondFragment()))
        tabs.setupWithViewPager(viewPager)
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

    class SlotAdapter(var slotList: List<Slot>, val activity: Activity) : RecyclerView.Adapter<SlotAdapter.SlotViewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewholder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.slot_card, parent, false)
            return SlotViewholder(view)

        }

        override fun getItemCount(): Int = slotList.size

        override fun onBindViewHolder(holder: SlotViewholder, position: Int) {
            holder.bindView(slotList[position])
        }

        fun updateList(newList: List<Slot>) {
            this.slotList = newList
            notifyDataSetChanged()
        }

        inner class SlotViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(data: Slot) {
                itemView.cardSlotTitle.text = data.name
                itemView.setOnClickListener {
                    (activity as AppCompatActivity).goTo<DetailRoot>()
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
