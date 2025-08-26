package com.viettel.tvbox.services

import com.viettel.tvbox.models.Categories
import com.viettel.tvbox.models.DetailCategoryResponse
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoriesService {
    @GET("type")
    fun getCategories(
        @Query("type") forAge: String?, @Query("titleId") titleId: String?
    ): Call<Categories>

    @GET("type/{id}")
    fun getDetailCategory(
        @Path("id") id: String?,
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("type") type: String?
    ): Call<DetailCategoryResponse>

}