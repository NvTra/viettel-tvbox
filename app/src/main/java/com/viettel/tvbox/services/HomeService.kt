package com.viettel.tvbox.services

import com.viettel.tvbox.models.AllGameItemResponse
import com.viettel.tvbox.models.Banner
import com.viettel.tvbox.models.LogUserHistory
import com.viettel.tvbox.models.ManagerHomeConfig
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeService {

    @GET("manage-home-config/no-auth/end-user")
    fun getManagerHomeConfig(): Call<List<ManagerHomeConfig>>

    @GET("manage-home-config/auth/end-user")
    fun getManagerHomeConfigAuth(): Call<List<ManagerHomeConfig>>

    @GET("game/enduser/no-auth/title")
    fun getAllGameByTitle(
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("type") type: String? = "ALL",
        @Query("titleId") titleId: String? = ""
    ): Call<AllGameItemResponse>

    @GET("no-auth/banners?sortOrder=desc")
    fun getAllBanner(): Call<List<Banner>>

    @GET("user-history/save")
    fun saveHistorySearch(@Body body: LogUserHistory): Call<Void>

    @POST("user-history/auth/save-play-game")
    fun savePlayGame(@Body body: LogUserHistory): Call<Void>
}