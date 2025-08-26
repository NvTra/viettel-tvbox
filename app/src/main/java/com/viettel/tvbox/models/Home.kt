package com.viettel.tvbox.models


data class ManagerHomeConfig(
    val id: String? = "",
    val title: String? = "",
    val type: String? = "",
    val status: Boolean? = null,
    val items: List<GameItem>? = emptyList(),
    val criteria: String? = null,
)

data class GameItem(
    val id: String? = "",
    val title: String? = "",
    val image: String? = "",
    val type: String? = "",
    val numberIndex: Int? = null,
)


data class AllGameItemResponse(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val data: List<AllGameByTitle>? = emptyList(),
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val hasNext: Boolean = false
)

data class AllGameByTitle(
    val priority: Int? = null,
    val id: String? = "",
    val type: String? = "",
    val device: String? = "",
    val title: String? = "",
    val star: Int? = null,
    val development: String? = "",
    val releaseTime: String? = "",
    val imageScreen: String? = "",
    val displayGenres: String? = "",
    val multiply: Boolean? = null,
)

data class Banner(
    val image: String? = "",
    val banner: String? = "",
    val type: String? = ""
)


