package com.musashi.claymore.spike.spike.detail


import android.content.pm.ActivityInfo
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
        vg.myWebView.loadUrl("https://netent-static.casinomodule.com/games/pyramid_mobile_html/game/pyramid_mobile_html.xhtml?staticServer=https%3A%2F%2Fnetent-static.casinomodule.com%2F&gameName=pyramid.desktop&targetElement=game&flashParams.bgcolor=000000&mobileParams.lobbyURL=https%253A%252F%252Fwww.netent.com%252Fen%252Fsection%252Fentertain%252F&gameId=pyramid_not_mobile&server=https%3A%2F%2Fnetent-game.casinomodule.com%2F&lang=it&sessId=DEMO-1430359953083&operatorId=default")

        return vg
    }



    override fun onDestroy() {
        super.onDestroy()
        // solo per accertarsi che il fragment venga distrutto
        "destroyed" log "FRAG_DESTROYED"
    }
}
