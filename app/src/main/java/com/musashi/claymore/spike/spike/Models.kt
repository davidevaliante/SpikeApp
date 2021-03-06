package com.musashi.claymore.spike.spike


import android.support.annotation.Keep
import java.io.Serializable
import kotlin.collections.HashMap

@Keep
data class Bonus(var bonus : String?=null,
                 var image : String?=null,
                 var link : String?=null,
                 var name:String?=null,
                 var producer:Producer?=null,
                 var rating:String?=null,
                 var review:String?=null,
                 var guideId:String?=null):Serializable

@Keep
data class BonusGuide(
        var bonus:Bonus?=null,
        var content:String?=null,
        var time:Double?=null
)

@Keep
data class Article(
        var id:String?=null,
        var content:String?=null,
        var time:Double?=null,
        var title:String?=null
):Serializable

@Keep
data class Producer(var id:String?=null, var name:String?=null,var image:String?=null,var link:String?=null):Serializable

@Keep
data class Slot(
        var id:String?=null,
        var bonus:HashMap<String,Bonus>? = null,
        var isFake:Boolean?=null,
        var description:String?=null,
        var linkPlay:String?=null,
        var linkYoutube: String?=null,
        var name: String?=null,
        var producer: Producer?=null,
        var rating: String?=null,
        var tecnicals: String?=null,
        var time: Double?=null,
        var tips: String?=null,
        var type: String?=null
)
@Keep
data class SlotCard(
        var id:String?=null,
        var description:String?=null,
        var name: String?=null,
        var producer: String?=null,
        var rating: String?=null,
        var time: Double?=null,
        var type: String?=null
)
@Keep
data class SlotMenu(
        var description:String?=null,
        var name: String?=null
)


