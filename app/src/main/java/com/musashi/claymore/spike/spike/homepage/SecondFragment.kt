package com.musashi.claymore.spike.spike.homepage

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.showInfo
import com.bumptech.glide.Glide
import com.musashi.claymore.spike.spike.Bonus

import com.musashi.claymore.spike.spike.R
import kotlinx.android.synthetic.main.bonus_card.view.*
import kotlinx.android.synthetic.main.fragment_second.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class SecondFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val randomBonus = (1..19).map { Bonus("Bonus ${it+1}", "Descrizione esempio ${it+1}") }

        bonusRc.layoutManager = LinearLayoutManager(activity)
        bonusRc.adapter = BonusAdapter(randomBonus,activity as FragmentActivity)
    }

    class BonusAdapter(var bonusList: List<Bonus>, val activity: Activity) : RecyclerView.Adapter<SecondFragment.BonusAdapter.BonusViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.bonus_card, parent, false)
            return BonusViewHolder(view)

        }

        override fun getItemCount(): Int = bonusList.size

        override fun onBindViewHolder(holder: BonusViewHolder, position: Int) {
            holder.bindView(bonusList[position])
        }

        fun updateList(newList: List<Bonus>) {
            this.bonusList = newList
            notifyDataSetChanged()
        }


        inner class BonusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(data: Bonus) {
//                itemView.bonusTitle.text = data.title
//                itemView.bonusDescription.text = data.desc
                Glide.with(activity).load("http://www.playbit.it/wp-content/uploads/2018/04/888casino-696x398.jpg")
                        .into(itemView.bonusImage)
                itemView.bonusImage
                itemView.setOnClickListener {
                    activity.showInfo("Va alla pagina relativa")
                }
            }

        }
    }
}
