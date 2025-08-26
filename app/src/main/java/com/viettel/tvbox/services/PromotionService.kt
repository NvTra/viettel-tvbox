package com.viettel.tvbox.services

import com.viettel.tvbox.models.Promotions
import com.viettel.tvbox.models.PromotionsDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PromotionService {

    @GET("/api/end-user/promotions/no-auth")
    fun getData(
        @Query("page") page: Int, @Query("pageSize") pageSize: Int, @Query("type") type: String
    ): Call<Promotions>


    // GET /api/end-user/promotions/no-auth/{id}
    @GET("/api/end-user/promotions/no-auth/{id}")
    fun getDetailNoAuth(
        @Path("id") id: String
    ): Call<PromotionsDetail>

    // GET /api/end-user/promotions/{id}
    fun getDetailAuth(
        @Path("id") id: String
    ): Call<PromotionsDetail>
}