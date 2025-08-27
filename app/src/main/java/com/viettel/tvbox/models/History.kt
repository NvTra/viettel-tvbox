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

data class AccessHistory(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val data: List<AccessHistoryDetail>? = emptyList(),
    val totalPages: Int? = null,
    val totalElements: Int? = null,
    val hasNext: Boolean? = null
)

data class AccessHistoryDetail(
    val os: String = "",
    val browser: String = "",
    val createdDate: Long = 0,
    val action: String = "",
)


data class PayHistory(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val data: List<PayHistoryDetail>? = emptyList(),
    val totalPages: Int? = null,
    val totalElements: Int? = null,
    val hasNext: Boolean? = null
)

data class PayHistoryDetail(
    val id: String? = "",
    val createdDate: Long? = 0,
    val serviceCode: String? = "",
    val price: Long? = null,
    val channel: String? = "",
    val action: String? = "",
    val subName: String? = "",
    val subCode: String? = "",
    val expiredTime: Long? = null,
    val status: Int? = null,
)
