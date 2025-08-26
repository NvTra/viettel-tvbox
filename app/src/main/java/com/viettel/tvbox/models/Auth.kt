package com.viettel.tvbox.models

data class LoginRequest(
    val username: String,
    val password: String
)


data class LoginResponse(
    val responseMessage: String,
    val responseCode: Long,
    val responseDate: String,
    val accessToken: String,
    val refreshToken: String
)