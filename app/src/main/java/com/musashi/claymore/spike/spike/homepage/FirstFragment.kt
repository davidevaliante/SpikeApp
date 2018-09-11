package com.musashi.claymore.spike.spike.homepage


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.Do
import aqua.extensions.goTo
import aqua.extensions.log
import aqua.extensions.showInfo
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.*

import com.musashi.claymore.spike.spike.constants.AppTypes
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.slot_card.view.*


class FirstFragment : Fragment() {

    var loading = false
    var slotList = mutableListOf<SlotCard>()
    val slotNode = FirebaseDatabase.getInstance().reference.child("SlotsCard").child("it")
    var chunkSize = 20
    var chunkNumber = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        slotRc.layoutManager=GridLayoutManager(activity,2)
        slotRc.adapter=SlotAdapter(slotList, activity as Activity)

        slotRc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var visibleThreshold = 4
                var totalItemCount = slotRc.layoutManager.itemCount
                var lastVisibleItem = (slotRc.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if(slotList.last().time!=null)
                    loadNextChunk((slotList.last().time as Long).toDouble())
                }
            }
        })


    }

    override fun onStart() {
        super.onStart()
        loadNextChunk()
    }



    private fun loadNextChunk (time:Double=System.currentTimeMillis().toDouble()) {
        loading=true
        slotNode.orderByChild("time").endAt(time).limitToLast(chunkSize*chunkNumber)
        .addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.reversed().mapNotNullTo(slotList, {
                    val s = it.getValue<SlotCard>(SlotCard::class.java)
                    s?.id= it.key
                    s
                })
                chunkNumber++
                loading=false
                slotRc.adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                error.toString() log "FB ERROR"
            }
        })
    }
}



    class SlotAdapter(var slotList: List<SlotCard>, val activity: Activity) : RecyclerView.Adapter<SlotAdapter.SlotViewholder>() {
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
                itemView.cardSlotTitle.text = data.name
                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("SLOT_ID", data.id)
                    (activity as AppCompatActivity).goTo<DetailRoot>(bundle)
                }
                Glide.with(activity).load(data.getImageLinkFromName()).into(itemView.cardSlotImage)
            }

        }
    }



