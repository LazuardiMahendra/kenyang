package com.example.kenyangproject.Model

data class ContentMain(
    var uid: String? = null,
    var userId: String? = null,
    var resepImage: String? = null,
    var resepNama: String? = null,
    var resepBahan: String? = null,
    var resepAlat: String? = null,
    var resepLangkah: String? = null,
    var timestamp: Long? = null,
    var favoriteCount: Int = 0,
    var favorites: MutableMap<String, Boolean> = HashMap()
) {
    data class Comment(
        var uid: String? = null,
        var userId: String? = null,
        var comment: String? = null,
        var timestamp: Long? = null
    )

}
