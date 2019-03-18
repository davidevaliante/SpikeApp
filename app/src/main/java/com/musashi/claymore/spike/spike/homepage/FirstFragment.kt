package com.musashi.claymore.spike.spike.homepage


import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.goTo
import aqua.extensions.log
import aqua.extensions.logFrom
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.*
import com.musashi.claymore.spike.spike.R.id.popularSlotsRc
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.popular_slot_card.view.*
import kotlinx.android.synthetic.main.slot_card.view.*
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment : Fragment() {

    private var recyclerViewLoading = true
    private var slotList = mutableListOf<SlotCard>()
    private val slotNode = FirebaseDatabase.getInstance().reference.child("SlotsCard").child("it")
    private var chunkSize = 20
    private var lastChunkSize = 20
    private val customScrollListener by lazy { customScrollListener() }
    private val homeLoading by lazy { (activity as HomePage).homeLoaderIndicator }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slotRc.layoutManager = GridLayoutManager(activity, 2)
        slotRc.adapter = SlotAdapter(slotList, activity as Activity)

        slotRc.addOnScrollListener(customScrollListener)
    }

    override fun onStart() {
        super.onStart()
        if(slotList.size==0) loadNextChunk()
    }

    private fun customScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 4
                val totalItemCount = slotRc.layoutManager.itemCount
                val lastVisibleItem = (slotRc.layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                if (!recyclerViewLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) && lastChunkSize==20) {
                    if (slotList.last().time != null)
                        loadNextChunk((slotList.last().time as Double))
                }
            }
        }
    }

    private fun loadNextChunk(upperBoundtime: Double = System.currentTimeMillis().toDouble()) {
        recyclerViewLoading = true
        homeLoading.show()

        slotNode.orderByChild("time")
                .endAt(upperBoundtime)
                .limitToLast(chunkSize)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.reversed().forEach {
                            it.logFrom(this@FirstFragment)
                            val slot = it.getValue(SlotCard::class.java)
                            slot?.id = it.key
                            if(slot!=null && slot !in slotList) slotList.add(slot)
                        }
                        lastChunkSize=snapshot.children.count()
                        slotRc.adapter.notifyDataSetChanged()

                        recyclerViewLoading = false
                        homeLoading.hide()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.toString() log "FB ERROR"
                    }
                })
    }
}

class SlotAdapter(private var slotList: List<SlotCard>, val activity: Activity) : RecyclerView.Adapter<SlotAdapter.SlotViewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewholder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.slot_card, parent, false)
            return SlotViewholder(view)
        }

        override fun getItemCount(): Int = slotList.size

        override fun onBindViewHolder(holder: SlotViewholder, position: Int) {
            holder.bindView(slotList[position])
        }

        fun updateList(newList: List<SlotCard>) {
            this.slotList = newList
            notifyDataSetChanged()
        }

        inner class SlotViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(data: SlotCard) {
                if(data.time!=null){
                    val time = Date((data.time as Double).toLong())
                    val format = SimpleDateFormat("MMM/YYYY")
                    val dateString = format.format(time)
                    itemView.cardSlotTime.text= "Aggiornato $dateString"
                }
                if(data?.type == "BAR"){
                    itemView.slotCardType.text = "Slot Bar"
                    itemView.slotCardType.background = ColorDrawable(ContextCompat.getColor(activity!!, R.color.redPrimary))
                }else {
                    itemView.slotCardType.text = "Slot Online"
                    itemView.slotCardType.background = ColorDrawable(ContextCompat.getColor(activity!!, R.color.colorPrimary))

                }
                itemView.cardSlotTitle.text = data.name?.toLowerCase()?.capitalize()
                itemView.cardSlotRating.text = "Voto ${data.rating}"
                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("SLOT_ID", data.id)
                    (activity as AppCompatActivity).goTo<DetailRoot>(bundle)
                }
                Glide.with(activity)
                        .load(data.getImageLinkFromName())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .thumbnail(0.1f)
                        .into(itemView.cardSlotImage)
            }
        }
    }



