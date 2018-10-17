package com.musashi.claymore.spike.spike.homepage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.goTo
import aqua.extensions.log
import aqua.extensions.showInfo
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.Bonus
import com.musashi.claymore.spike.spike.BonusGuideActivity

import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.R.id.bonusRc
import com.musashi.claymore.spike.spike.getImageLinkFromName
import kotlinx.android.synthetic.main.bonus_card.view.*
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {

    var bonusList:MutableList<Bonus> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBonusList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bonusRc.layoutManager = LinearLayoutManager(activity)
        bonusRc.adapter = BonusAdapter(bonusList,activity as FragmentActivity)
    }

    private fun getBonusList(){
        FirebaseDatabase.getInstance().reference.child("Bonus").child("it")
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        bindData(snapshot.children)
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    private fun bindData(fetchedList:Iterable<DataSnapshot>){
        fetchedList.mapNotNullTo(bonusList) {it.getValue<Bonus>(Bonus::class.java)}
        (bonusRc.adapter as BonusAdapter).updateList(bonusList)
    }

    class BonusAdapter(var bonusList: MutableList<Bonus>, val activity: Activity) : RecyclerView.Adapter<SecondFragment.BonusAdapter.BonusViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.bonus_card, parent, false)
            return BonusViewHolder(view)
        }

        override fun getItemCount(): Int = bonusList.size

        override fun onBindViewHolder(holder: BonusViewHolder, position: Int) {
            holder.bindView(bonusList[position])
        }

        fun updateList(newList: MutableList<Bonus>) {
            this.bonusList = newList
            notifyDataSetChanged()
        }

        inner class BonusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(data: Bonus) {
                // solo se c'è una guida fai questa roba
                if(data.guideId != null){
                    itemView.bonusCardGuideButton.visibility = View.VISIBLE
                    itemView.bonusCardGuideButton.setOnClickListener{
                        val bundle = Bundle()
                        bundle.putSerializable("BONUS_DATA", data)
                        (activity as AppCompatActivity).goTo<BonusGuideActivity>(bundle)
                    }
                }
                itemView.bonusTitle.text = data.name
                itemView.bonusDescription.text = data.bonus
                Glide.with(activity).load(data.getImageLinkFromName()).into(itemView.bonusImage)
                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(data.link)
                    activity.startActivity(intent)
                }

            }
        }
    }
}
