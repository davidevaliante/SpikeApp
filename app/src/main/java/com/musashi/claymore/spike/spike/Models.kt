package com.musashi.claymore.spike.spike

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import java.util.*


@Entity(tableName = "SlotMachines",
        indices = arrayOf(Index(value = "shortName")))
data class DetailedDescription(
        @PrimaryKey(autoGenerate = true)
        var id:Int?=null,
        var shortName:String?=null,
        var fullName:String?=null,
        var description: String?=null,
        var imageLink: String?=null,
        var embedLink: String?=null,
        var technicals: String?=null,
        var playTips: String?=null,
        var relatedWebSites: String?=null)

