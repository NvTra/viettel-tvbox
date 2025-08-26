package com.viettel.tvbox.models

data class GameDetail(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val id: String? = "",
    val title: String? = "",
    val partnerGameId: String? = "",
    val partner: String? = "",
    val description: String? = "",
    val forAge: String? = "",
    val multiPlay: Boolean? = null,
    val development: String? = "",
    val provider: String? = "",
    val videoTrailer: String? = "",
    val star: Double? = null,
    val type: String? = "",
    val isFree: Boolean? = null,
    val publisher: String? = "",
    val types: List<String>? = emptyList(),
    val duration: String? = "",
    val imageScreen: String? = "",
    val controller: List<String>? = emptyList(),
    val device: List<String>? = emptyList(),
    val country: List<String>? = emptyList(),
    val subtitle: List<String>? = emptyList(),
    val image: String? = "",
    val relationGame: List<GameRelation>? = emptyList(),
    val type_IMAGE_BLACK_NUT: String? = "",
)

data class GameRelation(
    val id: String? = "",
    val title: String? = "",
    val imageScreen: String? = "",
    val type: String? = "",
    val star: Double? = null
)

data class GameSearchResponse(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val data: List<GameRelation>? = emptyList(),
    val totalPages: Int? = null,
    val totalElements: Int? = null,
    val hasNext: Boolean? = null
)

data class LikeGame(
    val gameId: String? = "",
    val likeGame: Boolean? = null,
)

data class GeneralGame(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val generalInteractGame: GeneralStar? = null,
    val userInfoInteracts: List<UserLike>? = null
)

data class GeneralStar(
    val avgStar: Double? = null,
    val fiveStar: Int? = null,
    val fourStar: Int? = null,
    val oneStar: Int? = null,
    val threeStar: Int? = null,
    val twoStar: Int? = null,
)

data class UserLike(
    val userId: String? = null,
    val name: String? = null,
    val createdDate: Long? = null,
)