package com.musashi.claymore.spike.spike


import com.musashi.claymore.spike.spike.constants.ImgSize
import com.musashi.claymore.spike.spike.constants.StorageFolders
import toSnakeCase

fun Slot.getImageLinkFromName(size:ImgSize=ImgSize.MEDIUM):String{
    val baseUrl = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/"
    val urlEnd = "?alt=media"
    val small="64"
    val medium="250"
    return  when(size){
        ImgSize.SMALL -> "$baseUrl${StorageFolders.Slot}thumb_${small}_slot_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.MEDIUM -> "$baseUrl${StorageFolders.Slot}thumb_${medium}_slot_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.BIG -> "$baseUrl${StorageFolders.Slot}slot_${this.name?.toSnakeCase()}$urlEnd"
    }
}

fun Article.getImageLinkFromName(size:ImgSize=ImgSize.MEDIUM):String{
    val baseUrl = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/"
    val urlEnd = "?alt=media"
    val small="64"
    val medium="250"
    return  when(size){
        ImgSize.SMALL -> "$baseUrl${StorageFolders.ArticleImages}${this.title?.toSnakeCase()}_article_image$urlEnd"
        ImgSize.MEDIUM -> "$baseUrl${StorageFolders.ArticleImages}${this.title?.toSnakeCase()}_article_image$urlEnd"
        ImgSize.BIG -> "$baseUrl${StorageFolders.ArticleImages}${this.title?.toSnakeCase()}_article_image$urlEnd"
    }
}

fun SlotCard.getImageLinkFromName(size:ImgSize=ImgSize.MEDIUM):String{
    val baseUrl = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/"
    val urlEnd = "?alt=media"
    val small="64"
    val medium="250"
    return  when(size){
        ImgSize.SMALL -> "$baseUrl${StorageFolders.Slot}thumb_${small}_slot_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.MEDIUM -> "$baseUrl${StorageFolders.Slot}thumb_${medium}_slot_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.BIG -> "$baseUrl${StorageFolders.Slot}slot_${this.name?.toSnakeCase()}$urlEnd"
    }
}

fun Bonus.getImageLinkFromName(size:ImgSize=ImgSize.BIG):String{
    val baseUrl = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/"
    val urlEnd = "?alt=media"
    val small="64"
    val medium="250"
    return when(size){
        ImgSize.SMALL -> "$baseUrl${StorageFolders.Bonus}thumb_${small}_bonus_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.MEDIUM -> "$baseUrl${StorageFolders.Bonus}thumb_${medium}_bonus_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.BIG -> "$baseUrl${StorageFolders.Bonus}bonus_${this.name?.toSnakeCase()}$urlEnd"
    }
}



fun Bonus.getInternalImageLinkFromName(size:ImgSize=ImgSize.BIG):String{
    val baseUrl = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/"
    val urlEnd = "?alt=media"


    return "$baseUrl${StorageFolders.BonusInternalImage}bonus_internal_${this.name?.toSnakeCase()}$urlEnd"

}

fun Producer.getImageLinkFromName(size:ImgSize=ImgSize.MEDIUM):String{
    val baseUrl = "https://firebasestorage.googleapis.com/v0/b/spike-2481d.appspot.com/o/"
    val urlEnd = "?alt=media"
    val small="64"
    val medium="250"
    return when(size){
        ImgSize.SMALL -> "$baseUrl${StorageFolders.Producer}thumb_${small}_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.MEDIUM -> "$baseUrl${StorageFolders.Producer}thumb_${medium}_${this.name?.toSnakeCase()}$urlEnd"
        ImgSize.BIG -> "$baseUrl${StorageFolders.Producer}${this.name?.toSnakeCase()}$urlEnd"
    }
}