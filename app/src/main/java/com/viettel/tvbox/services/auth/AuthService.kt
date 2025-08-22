package com.viettel.tvbox.services.auth


import com.viettel.tvbox.models.auth.LoginRequest
import com.viettel.tvbox.models.auth.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login/no-captcha")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/logout")
    fun logout(): Call<Void>
}