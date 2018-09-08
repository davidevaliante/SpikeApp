package com.musashi.claymore.spike.spike.homepage


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.goTo
import aqua.extensions.log
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.Slot

import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.SlotCard
import com.musashi.claymore.spike.spike.constants.AppTypes
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import com.musashi.claymore.spike.spike.getImageLinkFromName
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.slot_card.view.*


class FirstFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slotNode = FirebaseDatabase.getInstance().reference.child("SlotsCard").child("it")

        slotNode.limitToLast(20).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children
                val list = mutableListOf<SlotCard>()
                snapshot.children.mapNotNullTo(list, {it.getValue<SlotCard>(SlotCard::class.java)})

                slotRc.adapter=SlotAdapter(list, activity as Activity)
            }
            override fun onCancelled(error: DatabaseError) {
                error.toString() log "FB ERROR"
            }
        })
        slotRc.layoutManager=GridLayoutManager(activity,2)
        slotRc.adapter=SlotAdapter(emptyList(), activity as Activity)
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
                    (activity as AppCompatActivity).goTo<DetailRoot>()
                }
                Glide.with(activity).load(data.getImageLinkFromName()).into(itemView.cardSlotImage)
            }

        }
    }


}
