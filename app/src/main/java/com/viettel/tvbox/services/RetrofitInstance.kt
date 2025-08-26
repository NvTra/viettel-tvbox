package com.viettel.tvbox.services

import UserPreferences
import android.content.Context
import com.viettel.tvbox.BuildConfig
import com.viettel.tvbox.services.token.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private lateinit var userPreferences: UserPreferences
    private const val BASE_URL = BuildConfig.API_BASE_URL

    fun init(context: Context) {
        userPreferences = UserPreferences.getInstance(context)
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(userPreferences) {
                userPreferences.clearAuth()
            })
            .build()
    }
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val promotionService: PromotionService by lazy {
        retrofit.create(PromotionService::class.java)
    }

    val categoryService: CategoriesService by lazy {
        retrofit.create(CategoriesService::class.java)
    }

    val homeService: HomeService by lazy {
        retrofit.create(HomeService::class.java)
    }
    val gameService: GameService by lazy {
        retrofit.create(GameService::class.java)
    }

  
}