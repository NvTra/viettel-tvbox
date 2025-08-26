package com.viettel.tvbox.services

import com.viettel.tvbox.models.AllGameItemResponse
import com.viettel.tvbox.models.Banner
import com.viettel.tvbox.models.ManagerHomeConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {

    @GET("manage-home-config/no-auth/end-user")
    fun getManagerHomeConfig(): Call<List<ManagerHomeConfig>>

    @GET("game/enduser/no-auth/title")
    fun getAllGameByTitle(
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("type") type: String? = "ALL",
        @Query("titleId") titleId: String? = ""
    ): Call<AllGameItemResponse>

    @GET("no-auth/banners?sortOrder=desc")
    fun getAllBanner(): Call<List<Banner>>
}