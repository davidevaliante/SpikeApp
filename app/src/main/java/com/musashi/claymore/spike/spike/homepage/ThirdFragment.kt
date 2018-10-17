package com.musashi.claymore.spike.spike.homepage


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aqua.extensions.goTo
import aqua.extensions.showMessage
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.musashi.claymore.spike.spike.*
import fromHtml
import kotlinx.android.synthetic.main.article_card.view.*

import kotlinx.android.synthetic.main.bonus_card.view.*
import kotlinx.android.synthetic.main.fragment_third.*
import java.text.SimpleDateFormat
import java.util.*


class ThirdFragment : Fragment() {

    var articleList:MutableList<Article> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleRecyclerView.layoutManager = LinearLayoutManager(activity)
        articleRecyclerView.adapter = ArticleAdapter(articleList, activity as Activity)

        FirebaseDatabase.getInstance().reference.child("Extra").child("it").addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val article = it.getValue(Article::class.java)
                    article?.id = it.key
                    articleList.add(article!!)
                }
                (articleRecyclerView.adapter as ArticleAdapter).updateList(articleList)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })

    }

    class ArticleAdapter(var articleList: MutableList<Article>, val activity: Activity) : RecyclerView.Adapter<ThirdFragment.ArticleAdapter.ArticleViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
            return ArticleViewHolder(view)
        }

        override fun getItemCount(): Int = articleList.size

        override fun onBindViewHolder(holder: ThirdFragment.ArticleAdapter.ArticleViewHolder, position: Int) {
            holder.bindView(articleList[position])
        }

        fun updateList(newList: MutableList<Article>) {
            this.articleList = newList
            notifyDataSetChanged()
        }

        inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(data: Article) {
                val time = Date((data.time as Double).toLong())
                val format = SimpleDateFormat("MMM/YYYY")
                val dateString = format.format(time)

                itemView.apply {
                    articleCardTitle.text = data.title?.fromHtml()?.take(50)
                    articleCardDescription.text = "${data.content?.fromHtml().toString().replace("\n", "")?.take(180)}..."
                    articleCardUpdated.text = "Aggiornato $dateString"
                    setOnClickListener {
                        val bundle = Bundle()
                        bundle.putBoolean("IS_ARTICLE", true)
                        bundle.putSerializable("ARTICLE", data)
                        activity.goTo<BonusGuideActivity>(bundle)
                    }
                }
            }
        }
    }
}
