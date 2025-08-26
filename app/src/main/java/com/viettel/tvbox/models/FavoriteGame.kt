package com.viettel.tvbox.models

data class FavoriteGame(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val status: Boolean? = null,
    val listGame: List<GameRelation>? = emptyList()
)
