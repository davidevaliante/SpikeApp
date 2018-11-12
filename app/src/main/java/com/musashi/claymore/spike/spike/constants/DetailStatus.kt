package com.musashi.claymore.spike.spike.constants

enum class DetailStatus{
    INITIAL,
    PLAYING_DEMO
}
enum class AppTypes{
    SLOT,
    PRODUCER,
    BONUS
}

enum class ImgSize{
    SMALL,
    MEDIUM,
    BIG
}

object SlotTypes{
    val Bar = "GRATIS"
    val Gratis = "GRATIS"
}

object StorageFolders{
    const val Slot = "SlotImages%2F"
    const val Bonus = "BonusImages%2F"
    const val Producer = "ProducerImages%2F"
    const val ArticleImages = "ArticleImages%2f"
}

