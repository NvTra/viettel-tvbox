package com.viettel.tvbox.services

import com.viettel.tvbox.models.FavoriteGame
import com.viettel.tvbox.models.HistoryGame
import com.viettel.tvbox.models.UserInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("auth/user/information")
    fun getUserInformation(): Call<UserInformation>


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
}