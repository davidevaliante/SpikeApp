package com.musashi.claymore.spike.spike

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import aqua.extensions.showError
import aqua.extensions.showMessage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.constants.StorageFolders
import com.squareup.picasso.Picasso
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
                articleTitle.visibility = View.VISIBLE
                articleTitle.text = article.title
                guideCard.visibility = View.VISIBLE
                bonusguideContent.setClickableHtmlWithImages("${article.title} \n${article.content}", this)
                bonusGuideBackBtn.setOnClickListener { finish() }
                bonusGuideExternalLinkBtn.visibility=View.GONE
                Log.d("DEBUG_FOTO", article.getImageLinkFromName())

                Glide.with(this).load(article.getImageLinkFromName()).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        articleImage.visibility = View.GONE
                        imageGuideCard.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                }).into(articleImage)


            }
        }else{
            val bonusData = intent.extras.getSerializable("BONUS_DATA") as Bonus
            var image : Drawable? = null

            Log.d("DEBUG_FOTO", bonusData.getInternalImageLinkFromName())

            Glide.with(this).load(bonusData.getInternalImageLinkFromName()).listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Log.d("DEBUG_FOTO", "failed")
                    articleImage.visibility = View.GONE
                    imageGuideCard.visibility = View.GONE

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    image = resource

                    return false
                }

            }).into(articleImage)


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
