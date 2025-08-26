package com.viettel.tvbox.services

import com.viettel.tvbox.models.GameDetail
import com.viettel.tvbox.models.GameRelation
import com.viettel.tvbox.models.GameSearchResponse
import com.viettel.tvbox.models.GeneralGame
import com.viettel.tvbox.models.LikeGame
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {
    @GET("game/enduser/no-auth/{id}")
    fun getGameDetail(@Path("id") id: String): Call<GameDetail>

    @GET("game/enduser/searched")
    fun searchGames(
        @Query("type") type: String
    ): Call<List<GameRelation>>


    @GET("game/enduser/smart-search")
    fun smartSearchGames(
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("textSearch") textSearch: String,
        @Query("allGame") allGame: String?,
        @Query("type") type: String
    ): Call<GameSearchResponse>

    @POST("interact-game/like-game")
    fun saveFavoriteGame(
        @Body LikeGame: LikeGame
    ): Call<LikeGame>


    @GET("interact-game/no-auth/general-game")
    fun generalGame(
        @Query("gameId") gameId: String?,
    ): Call<GeneralGame>


    @POST("interact-game/like-game")
    fun likeGame(@Body likeGame: LikeGame): Call<LikeGame>
}