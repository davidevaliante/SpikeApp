package com.musashi.claymore.spike.spike

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import java.util.*

data class Bonus(var bonus : String?=null, var image : String?=null, var link : String?=null, var name:String?=null,var producer:Producer?=null, var rating:String?=null,var review:String?=null)
data class Producer(var id:String?=null, var name:String?=null,var image:String?=null,var link:String?=null)


data class Slot(
        var bonus:List<Bonus>? = null,
        var isFake:Boolean?=null,
        var description:String?=null,
        var linkPlay:String?=null,
        var linkYoutube: String?=null,
        var name: String?=null,
        var producer: Producer?=null,
        var rating: String?=null,
        var tecnicals: String?=null,
        var time: Long?=null,
        var tips: String?=null,
        var type: String?=null
)

data class SlotCard(
        var description:String?=null,
        var name: String?=null,
        var producer: String?=null,
        var rating: String?=null,
        var time: Long?=null,
        var type: String?=null
)

data class SlotMenu(
        var description:String?=null,
        var name: String?=null
)


