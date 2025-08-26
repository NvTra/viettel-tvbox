package com.viettel.tvbox.models

data class HistoryGame(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val data: List<HistoryGameDetail>? = emptyList(),
    val totalPages: Int? = null,
    val totalElements: Int? = null,
    val hasNext: Boolean? = null
)

data class HistoryGameDetail(
    val name: String? = "",
    val type: String? = "",
    val time: String? = "",
    val types: String? = "",
    val gid: String? = "",
    val imageHTML: String? = "",
    val imageCLOUD: String? = "",
    val uid: String? = "",
)