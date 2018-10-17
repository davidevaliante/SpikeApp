package com.musashi.claymore.spike.spike

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import aqua.extensions.showError
import aqua.extensions.showMessage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fromHtml
import kotlinx.android.synthetic.main.activity_bonus_guide.*
import setClickableHtmlWithImages

class BonusGuideActivity : AppCompatActivity() {

    private val bg = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/Mix%2Fbonus_article_bg.jpg?alt=media&token=a46e2bf8-4f76-4c10-a48f-cfad0f93f1fb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bonus_guide)

        val isArticle = intent.extras.getBoolean("IS_ARTICLE", false)
        if(isArticle){
            val article = intent.extras.getSerializable("ARTICLE") as Article
            article.let {
                guideLoaderIndicator.smoothToHide()
                loadFailtext.visibility=View.GONE

                guideCard.visibility = View.VISIBLE
                bonusguideContent.setClickableHtmlWithImages("${article.title} \n${article.content}", this)
                bonusGuideBackBtn.setOnClickListener { finish() }
                bonusGuideExternalLinkBtn.visibility=View.GONE
            }
        }else{
            val bonusData = intent.extras.getSerializable("BONUS_DATA") as Bonus
            if(bonusData.guideId != null){
                FirebaseDatabase.getInstance().reference.child("BonusGuides").child("it").child(bonusData.guideId as String)
                        .addListenerForSingleValueEvent(object :ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val guide = snapshot.getValue(BonusGuide::class.java)
                                if(guide?.content == null || guide?.content?.length == 0){
                                    guideLoaderIndicator.smoothToHide()
                                    loadFailtext.visibility=View.VISIBLE

                                }else{
                                    guideLoaderIndicator.smoothToHide()
                                    guideCard.visibility = View.VISIBLE
                                    bonusguideContent.setClickableHtmlWithImages(guide?.content, this@BonusGuideActivity)
                                    bonusGuideBackBtn.setOnClickListener { finish() }
                                    bonusGuideExternalLinkBtn.setOnClickListener {
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.data = Uri.parse(guide?.bonus?.link)
                                        startActivity(intent)
                                    }
                                }
                            }
                            override fun onCancelled(p0: DatabaseError) {
                            }
                        })
            }
        }
        Glide.with(this).load(bg).transition(withCrossFade()).into(bonusGuideBg)
    }
}
