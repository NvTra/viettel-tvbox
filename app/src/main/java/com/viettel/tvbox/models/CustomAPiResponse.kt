package com.viettel.tvbox.models

data class CustomAPiResponse(
    val responseMessage: String? = null,
    val responseCode: Int? = null,
    val responseDate: String? = null,
)

data class RestException(
    val status: Int? = null,
    val code: Int? = null,
    val error: String? = null,
    val path: String? = "",
    val date: String? = ""
)