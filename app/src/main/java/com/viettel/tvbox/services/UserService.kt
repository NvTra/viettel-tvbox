package com.viettel.tvbox.services

import com.viettel.tvbox.models.auth.UserInformation
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("auth/user/information")
    fun getUserInformation(): Call<UserInformation>
}