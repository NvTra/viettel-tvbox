package com.viettel.tvbox.services.token

import UserPreferences
import android.content.Context
import com.google.gson.Gson
import com.viettel.tvbox.BuildConfig
import com.viettel.tvbox.R
import com.viettel.tvbox.utils.NetworkUtils
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
    private val onLogout: () -> Unit,
    private val context: Context
) : Interceptor {

    companion object {
        private const val AUTH_SCHEME = "Bearer "
        private const val AUTH_HEADER_NAME = "Authorization"
        private const val REFRESH_TOKEN_API = "auth/refresh-token"

        private val NON_TOKEN_APIS = listOf(
            "auth/login",
            "auth/login/no-captcha",
            "auth/login/black-nut",
            "auth/otp/confirm",
            "auth/otp/refresh",
            "auth/refresh-token",
            "game/enduser/home-page/noauth",
            "game/enduser/list-video",
            "type",
            "game/enduser/cloud-html",
            "game/enduser/no-auth/",
            "game/enduser/smart-search/noauth",
            "game/enduser/check-play/no-auth",
            "game/enduser/no-auth/top-game",
            "game/enduser/no-auth/all-free-game",
            "interact-game/no-auth/general-game",
            "interact-game/no-auth/comments",
            "end-user/promotions/no-auth",
            "no-auth/banners",
            "manage-home-config/no-auth/end-user",
            "auth/back-url",
            "game/enduser/no-auth/title",
            "game/enduser/no-auth/blacknut/access-token",
            "landing-page/no-auth",
            "rank/user/no-auth",
            "manage-home-config/popup",
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

        if (!NetworkUtils.isConnected(context)) {
            throw IOException(context.getString(R.string.error_no_network))
        }

        println(
            "Request URL: ${originalRequest.url} ||| ${
                isTokenBasedAuthEntryPoint(
                    originalRequest.url.toString()
                )
            }"
        )

        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", buildDeviceInfo())
            .build()

        if (isTokenBasedAuthEntryPoint(originalRequest.url.toString())) {
            return chain.proceed(requestWithUserAgent)
        }

        if (!isJwtTokenValid()) {
            println(context.getString(R.string.error_jwt_invalid))
            return handle401Error(requestWithUserAgent, chain)
        }

        val authenticatedRequest = addTokenToRequest(requestWithUserAgent)
        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            response.close()
            return handle401Error(requestWithUserAgent, chain)
        }

        return response
    }

    private fun buildDeviceInfo(): String {
        val okHttpVersion = okhttp3.OkHttp.VERSION
        return "OkHttp/$okHttpVersion(Linux; Android ${android.os.Build.VERSION.RELEASE}; ${android.os.Build.MODEL}) ${android.os.Build.MANUFACTURER}"
    }

    private fun isTokenBasedAuthEntryPoint(url: String): Boolean {
        val apiPath = url.replace(BuildConfig.API_BASE_URL, "")
        println("API Path: $apiPath")
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
                println(context.getString(R.string.error_no_refresh_token))
                onLogout()
            }

            val refreshResponse = performRefreshToken(refreshToken)

            if (refreshResponse != null) {
                userPreferences.saveToken(refreshResponse.accessToken)
                userPreferences.saveRefreshToken(refreshResponse.refreshToken)
                userPreferences.saveTokenExpiration(refreshResponse.expiresIn)

                println(context.getString(R.string.refresh_token_success))
                val newRequest = addTokenToRequest(originalRequest)
                return chain.proceed(newRequest)
            } else {
                println(context.getString(R.string.refresh_token_failed))
                onLogout()
                throw IOException(context.getString(R.string.error_failed_refresh_token))
            }

        } catch (e: Exception) {
            println(context.getString(R.string.error_refresh_token, e.message ?: ""))
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
                val responseBody = response.body.string()
                responseBody.let {
                    gson.fromJson(it, LoginResponse::class.java)
                }
            } else {
                println(context.getString(R.string.error_refresh_token_api, response.code))
                null
            }
        } catch (e: Exception) {
            println(context.getString(R.string.error_exception_refresh_token_api, e.message ?: ""))
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