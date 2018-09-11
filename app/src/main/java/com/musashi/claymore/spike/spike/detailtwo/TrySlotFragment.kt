package com.musashi.claymore.spike.spike.detailtwo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.inflateInContainer
import com.musashi.claymore.spike.spike.R
import kotlinx.android.synthetic.main.fragment_try_slot.view.*
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import aqua.extensions.log


class TrySlotFragment : Fragment() {
    companion object {
        private val LINK_PLAY = "PLAY_LINK"

        fun newInstance(linkPlay: String): TrySlotFragment {
            val args = Bundle()
            args.putSerializable(LINK_PLAY, linkPlay)
            val fragment = TrySlotFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vg = inflateInContainer(R.layout.fragment_try_slot, container)

        // settings per far utilizzare JavaScript (copiati ed incollati senza pudore, vanno sistemati ma funziona tutto)
        vg.myWebView.settings.javaScriptEnabled=true
        vg.myWebView.settings.domStorageEnabled = true
        vg.myWebView.settings.saveFormData = true
        vg.myWebView.settings.allowContentAccess = true
        vg.myWebView.settings.allowFileAccess = true
        vg.myWebView.settings.allowFileAccessFromFileURLs = true
        vg.myWebView.settings.allowUniversalAccessFromFileURLs = true
        vg.myWebView.settings.setSupportZoom(true)
        vg.myWebView.webViewClient = WebViewClient()
        vg.myWebView.isClickable = true
        vg.myWebView.webChromeClient = WebChromeClient()

        // dice che pagina caricare
        vg.myWebView.loadUrl(arguments?.getSerializable(LINK_PLAY).toString())

        return vg
    }

    override fun onDestroy() {
        super.onDestroy()
        // solo per accertarsi che il fragment venga distrutto
        "destroyed" log "FRAG_DESTROYED"
    }
}
