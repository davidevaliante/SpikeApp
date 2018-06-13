package com.musashi.claymore.spike.spike.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import aqua.extensions.*
import assignViewModel
import com.musashi.claymore.spike.spike.DetailedDescription
import com.musashi.claymore.spike.spike.R
import com.musashi.claymore.spike.spike.R.id.detailActivityContainer
import com.musashi.claymore.spike.spike.constants.DetailStatus
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var viewModel : DetailActivityViewModel
    val descrizioneEsempio = DetailedDescription(
            shortName ="pyramid",
            fullName="pyramidSlot",
            description="La Slot Pyramid: Quest for Immortality è una slot a tema egiziano del produttore NetEnt. Ha 720 linee di pagamento, in modalità SuperPlay. Ossia qualsiasi combinazione di 3 o più simboli uguali da sinistra a destra è pagante.\n" +
                    "\n" +
                    "Non c’è un vero e proprio gioco Bonus da attivare, ma la caratteristica principale di questa slot è il Moltiplicatore a cascata. Ogni 3 pagamenti consecutivi, il moltiplicatore delle vincite viene incrementato. E’ possibile arrivare fino a x10. I Wild ossia i simboli Jolly possono apparire casualmente nei rulli 2, 3 e 4 unicamente dopo l’ottenimento di una vincita e successiva cascata.\n" +
                    "\n" +
                    "In questa pagina puoi provare la slot Pyramid Quest for Immortality Gratis in modalità a soldi finti. La modalità Demo serve a prendere dimestichezza con la slot prima di tentare la fortuna a soldi veri. Essendo questa una partita di “allenamento” a soldi finti, non è richiesta alcuna registrazione. Vi consiglio comunque di registrarvi al Casinò Online consigliato per sfruttare il Bonus di Benvenuto SENZA DEPOSITO esclusivo ottenibile tramite i link su questa pagina.",
            imageLink="http://cdn.vegasslotsonline.com/netent/images/pyramid-quest-for-immortality.jpg",
            embedLink="t",
            technicals= "Produttore: NetEnt@" +
                    "RTP (percentuale di payout): 96.48%@" +
                    "E’ una slot a singolo spin, ossia non da la possibilità di tenere le combinazioni che più ci piacciono, ma verrà pagato direttamente ogni@ Spin effettuato.@" +
                    "Le linee di pagamento sono 720@" +
                    "Funzione speciale Moltiplicatore Progressivo@" +
                    "Funzione speciale Pagamenti a cascata",
            playTips= "Per questa slot consiglio un rapporto Bet/Budget di 0,5/100. Ad esempio giocando 100€ il Bet ideale di partenza è tra 0,5€ e 1€.@" +
                    "Partire testando la slot, se non dovesse rispondere da subito con vincite anche piccole in una cinquantina di Spin, è il caso di rimandare la partita a soldi veri.@" +
                    "In caso di frequenti vincite buone (file o pagine intere) dobbiamo incrementare il Bet il più possibile in base alle nostre possibilità e tentare qualche tiro. In caso di esito negativo si potrà tornare a giocare con il Bet di partenza. Non di rado potremmo avere ottime combinazioni, con buone possibilità di vincita nei momenti caldi.@" +
                    "Come per tutte le slot della NetEnt consiglio di non eccedere troppo con il Bet rispetto al Budget destinato al gioco. Queste slot hanno bisogno di effettuare numerosi Spin, ma in casi fortunati è possibile anche ottenere una vincita molto alta da 1000 e più volte il Bet.",
            relatedWebSites = "http://bit.ly/SNAIBONUS100@http://bit.ly/BonusLOTTOMATICA@")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        makeStatusbarTranslucent()

        setContentView(R.layout.activity_detail)
        viewModel = ViewModelProviders.of(this).get(DetailActivityViewModel::class.java)

        if (viewModel.state.value==null)
            viewModel.state.postValue(DetailStatus.INITIAL)

        viewModel.state.observe(this, Observer { changedState ->
            changedState?.let {
                changedState logFrom this
                when(changedState){
                    DetailStatus.INITIAL -> bindInitialFragment()
                    DetailStatus.PLAYING_DEMO -> bindTrySlotFragment()
                }
            }
        })
    }

    fun bindInitialFragment(){
        val fragment = DescriptionDetailFragment()
        fragment.assignDataToDisplay(descrizioneEsempio)
        replaceFrag(detailActivityContainer,fragment)
    }

    fun bindTrySlotFragment(){
        val fragment = TrySlotFragment()
        replaceFrag(detailActivityContainer,fragment)
    }

    fun unbindTrySlotFragment(){
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel.state.postValue(DetailStatus.INITIAL)
    }

    override fun onBackPressed() {
        when(viewModel.state.value){
            DetailStatus.INITIAL -> super.onBackPressed()
            DetailStatus.PLAYING_DEMO -> unbindTrySlotFragment()
        }
    }
}
