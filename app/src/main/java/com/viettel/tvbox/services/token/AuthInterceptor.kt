package com.viettel.tvbox.services.token

import UserPreferences
import com.google.gson.Gson
import com.viettel.tvbox.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: String
)

class AuthInterceptor(
    private val userPreferences: UserPreferences,
    private val onLogout: () -> Unit
) : Interceptor {

    companion object {
        private const val AUTH_SCHEME = "Bearer "
        private const val AUTH_HEADER_NAME = "Authorization"
        private const val REFRESH_TOKEN_API = "auth/refresh-token"

        private val NON_TOKEN_APIS = listOf(
            "/api/auth/login",
            "/api/auth/login/no-captcha",
            "/api/auth/login/black-nut",
            "/api/auth/otp/confirm",
            "/api/auth/otp/refresh",
            "/api/auth/refresh-token",
            "/api/game/enduser/home-page/noauth",
            "/api/game/enduser/list-video",
            "/api/type",
            "/api/game/enduser/cloud-html",
            "/api/game/enduser/no-auth/",
            "/api/game/enduser/smart-search/noauth",
            "/api/game/enduser/check-play/no-auth",
            "/api/game/enduser/no-auth/top-game",
            "/api/game/enduser/no-auth/all-free-game",
            "/api/interact-game/no-auth/general-game",
            "/api/interact-game/no-auth/comments",
            "/api/end-user/promotions/no-auth",
            "/api/no-auth/banners",
            "/api/manage-home-config/no-auth/end-user",
            "/api/auth/back-url",
            "/api/game/enduser/no-auth/title",
            "/api/game/enduser/no-auth/blacknut/access-token",
            "/api/landing-page/no-auth",
            "/api/rank/user/no-auth",
            "/api/manage-home-config/popup",
        )
    }

    private val isRefreshing = AtomicBoolean(false)
    private val gson = Gson()

    private val refreshClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        println("Request URL: ${originalRequest.url}")

        if (!isTokenBasedAuthEntryPoint(originalRequest.url.toString())) {
            return chain.proceed(originalRequest)
        }

        if (!isJwtTokenValid()) {
            println("JWT Token không hợp lệ, thực hiện refresh token")
            return handle401Error(originalRequest, chain)
        }

        val authenticatedRequest = addTokenToRequest(originalRequest)
        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            response.close()
            return handle401Error(originalRequest, chain)
        }

        return response
    }

    private fun isTokenBasedAuthEntryPoint(url: String): Boolean {
        val apiPath = url.replace(BuildConfig.API_BASE_URL, "")
        return NON_TOKEN_APIS.find { e -> apiPath.startsWith(e) } != null
    }

    private fun addTokenToRequest(request: Request): Request {
        val token = userPreferences.getToken()
        return if (token.isNotEmpty()) {
            request.newBuilder()
                .header(AUTH_HEADER_NAME, AUTH_SCHEME + token)
                .build()
        } else {
            request
        }
    }

    @Synchronized
    private fun handle401Error(originalRequest: Request, chain: Interceptor.Chain): Response {
        if (isRefreshing.get()) {
            waitForRefreshCompletion()
            val newRequest = addTokenToRequest(originalRequest)
            return chain.proceed(newRequest)
        }

        isRefreshing.set(true)

        try {
            val refreshToken = userPreferences.getRefreshToken()
            if (refreshToken.isEmpty()) {
                println("Không có refresh token, thực hiện logout")
                onLogout()
            }

            val refreshResponse = performRefreshToken(refreshToken)

            if (refreshResponse != null) {
                // Lưu token mới
                userPreferences.saveToken(refreshResponse.accessToken)
                userPreferences.saveRefreshToken(refreshResponse.refreshToken)
                userPreferences.saveTokenExpiration(refreshResponse.expiresIn)

                println("Refresh token thành công")

                // Retry request gốc với token mới
                val newRequest = addTokenToRequest(originalRequest)
                return chain.proceed(newRequest)
            } else {
                println("Refresh token thất bại, thực hiện logout")
                onLogout()
                throw IOException("Failed to refresh token")
            }

        } catch (e: Exception) {
            println("Lỗi khi refresh token: ${e.message}")
            onLogout()
            throw e
        } finally {
            isRefreshing.set(false)
        }
    }

    private fun performRefreshToken(refreshToken: String): LoginResponse? {
        return try {
            val request = Request.Builder()
                .url(BuildConfig.API_BASE_URL + REFRESH_TOKEN_API)
                .header(AUTH_HEADER_NAME, AUTH_SCHEME + refreshToken)
                .get()
                .build()

            val response = refreshClient.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                responseBody?.let {
                    gson.fromJson(it, LoginResponse::class.java)
                }
            } else {
                println("Refresh token API trả về lỗi: ${response.code}")
                null
            }
        } catch (e: Exception) {
            println("Exception khi gọi refresh token API: ${e.message}")
            null
        }
    }

    private fun isJwtTokenValid(): Boolean {
        val token = userPreferences.getToken()
        val expirationTime = userPreferences.getTokenExpiration()

        if (token.isEmpty() || expirationTime <= 0) {
            return false
        }

        val currentTime = System.currentTimeMillis()
        return expirationTime > (currentTime + 2000)
    }

    private fun waitForRefreshCompletion() {
        var waitTime = 0
        while (isRefreshing.get() && waitTime < 30000) {
            try {
                Thread.sleep(100)
                waitTime += 100
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                break
            }
        }
    }
}