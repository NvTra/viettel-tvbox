package com.viettel.tvbox.services

import com.viettel.tvbox.models.AccessHistory
import com.viettel.tvbox.models.CustomAPiResponse
import com.viettel.tvbox.models.FavoriteGame
import com.viettel.tvbox.models.HistoryGame
import com.viettel.tvbox.models.PayHistory
import com.viettel.tvbox.models.UserInformation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("auth/user/information")
    fun getUserInformation(): Call<UserInformation>


    @POST("auth/change-password")
    fun changePassword(
        @Body body: Map<String, String>
    ): Call<CustomAPiResponse>

    @GET("user/game-history")
    fun getGamePlayHistory(
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("sortOrder") sortOrder: String?,
        @Query("sortProperty") sortProperty: String?,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("forAge") forAge: String
    ): Call<HistoryGame>

    @GET("interact-game/favorite_game")
    fun getFavoriteGame(): Call<FavoriteGame>

    @GET("user-history/access-history")
    fun getAccessHistory(
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("startDate") startDate: Long,
        @Query("endDate") endDate: Long,
    ): Call<AccessHistory>

    @GET("subscription-log")
    fun getPayHistory(
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("sortOrder") sortOrder: String?,
        @Query("sortProperty") sortProperty: String?,
        @Query("startTs") startDate: Long,
        @Query("endTs") endDate: Long,
    ): Call<PayHistory>

    @GET("user-history/save")
    fun savHisistorySearch(
        @Query("textSearch") textSearch: String,
    ): Call<Void>
}