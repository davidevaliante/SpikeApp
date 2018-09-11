package com.musashi.claymore.spike.spike


import java.io.Serializable
import kotlin.collections.HashMap

data class Bonus(var bonus : String?=null, var image : String?=null, var link : String?=null, var name:String?=null,var producer:Producer?=null, var rating:String?=null,var review:String?=null):Serializable
data class Producer(var id:String?=null, var name:String?=null,var image:String?=null,var link:String?=null):Serializable


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

data class SlotCard(
        var id:String?=null,
        var description:String?=null,
        var name: String?=null,
        var producer: String?=null,
        var rating: String?=null,
        var time: Double?=null,
        var type: String?=null
)

data class SlotMenu(
        var description:String?=null,
        var name: String?=null
)


