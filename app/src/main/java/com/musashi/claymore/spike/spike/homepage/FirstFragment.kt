package com.musashi.claymore.spike.spike.homepage


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.goTo
import com.bumptech.glide.Glide
import com.musashi.claymore.spike.spike.DetailedDescription

import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.detailtwo.DetailRoot
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.slot_card.view.*


class FirstFragment : Fragment() {
    private val descrizioneEsempio = DetailedDescription(
            shortName = "pyramid",
            fullName = "Pyramid, Quest for immortality",
            description = "<p>La Slot&nbsp;<strong>Pyramid: Quest</strong>&nbsp;<strong>for Immortality&nbsp;</strong>è una slot a tema egiziano del produttore NetEnt. Ha 720 linee di pagamento, in modalità SuperPlay. Ossia qualsiasi combinazione di 3 o più simboli uguali da sinistra a destra è pagante.</p>" +
                    "\n" +
                    "<p>Non c’è un vero e proprio gioco Bonus da attivare, ma la caratteristica principale di questa slot è il <strong>Moltiplicatore a cascata</strong>. Ogni 3 pagamenti consecutivi, il moltiplicatore delle vincite viene incrementato. E’ possibile arrivare <strong>fino a x10</strong>. I Wild ossia i simboli Jolly possono apparire casualmente nei rulli 2, 3 e 4 unicamente dopo l’ottenimento di una vincita e successiva cascata.</p>\n" +
                    "\n" +
                    "<p>In questa pagina puoi provare la slot <strong>Pyramid Quest for Immortality&nbsp;Gratis&nbsp;</strong>in modalità a soldi finti. La modalità Demo serve a prendere dimestichezza con la slot prima di tentare la fortuna a soldi veri.&nbsp;Essendo questa una partita di “allenamento” a soldi finti, non è richiesta alcuna registrazione. Vi consiglio comunque di <strong>registrarvi al Casinò Online</strong> consigliato per <strong>sfruttare il Bonus di Benvenuto SENZA DEPOSITO</strong> esclusivo ottenibile tramite i link su questa pagina.</p>",
            imageLink = "http://www.netentstalker.com/wp-content/uploads/2015/09/Pyramid-Quest-For-Immortality-NetEnt.jpg",
            embedLink = "t",
            technicals = "Produttore: <strong>NetEnt</strong>@" +
                    "RTP (percentuale di payout): <strong>96.48%</Strong>@" +
                    "E’ una slot a <strong>singolo spin</strong>, ossia non da la possibilità di tenere le combinazioni che più ci piacciono, ma verrà pagato direttamente ogni Spin effettuato.@" +
                    "Le linee di pagamento sono <strong>720</Strong>@" +
                    "Funzione speciale <strong>Moltiplicatore Progressivo</strong>@" +
                    "Funzione speciale <strong>Pagamenti a cascata</strong>",
            playTips = "Per questa slot consiglio un rapporto Bet/Budget di <strong>0,5/100</strong>. Ad esempio giocando 100€ il Bet ideale di partenza è tra 0,5€ e 1€.@" +
                    "Partire testando la slot, se non dovesse rispondere da subito con vincite anche piccole in una cinquantina di Spin, è il caso di rimandare la partita a soldi veri.@" +
                    "In caso di frequenti vincite buone (file o pagine intere) dobbiamo <strong>incrementare il Bet</strong> il più possibile in base alle nostre possibilità e tentare qualche tiro. In caso di esito negativo si potrà tornare a giocare con il Bet di partenza. Non di rado potremmo avere ottime combinazioni, con buone possibilità di vincita nei momenti caldi.@" +
                    "Come per tutte le slot della NetEnt consiglio di <strong>non eccedere troppo con il Bet</strong> rispetto al Budget destinato al gioco. Queste slot hanno bisogno di effettuare numerosi Spin, ma in casi fortunati è possibile anche ottenere una vincita molto alta da 1000 e più volte il Bet.",
            relatedWebSites = "http://bit.ly/SNAIBONUS100@http://bit.ly/BonusLOTTOMATICA@")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // lista finta di descrizioni
        var listaRandom = mutableListOf<DetailedDescription>()
        for (i in 0..40) {
            listaRandom.add(descrizioneEsempio)
        }
        slotRc.layoutManager = GridLayoutManager(activity,2)
        slotRc.adapter = SlotAdapter(listaRandom, activity as FragmentActivity)
    }

    class SlotAdapter(var slotList: List<DetailedDescription>, val activity: Activity) : RecyclerView.Adapter<SlotAdapter.SlotViewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewholder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.slot_card, parent, false)
            return SlotViewholder(view)

        }

        override fun getItemCount(): Int = slotList.size

        override fun onBindViewHolder(holder: SlotViewholder, position: Int) {
            holder.bindView(slotList[position])
        }

        fun updateList(newList: List<DetailedDescription>) {
            this.slotList = newList
            notifyDataSetChanged()
        }


        inner class SlotViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(data: DetailedDescription) {
                itemView.cardSlotTitle.text = data.fullName
                itemView.setOnClickListener {
                    (activity as AppCompatActivity).goTo<DetailRoot>()
                }
                Glide.with(activity).load(data.imageLink).into(itemView.cardSlotImage)
            }

        }
    }


}
