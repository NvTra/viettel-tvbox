package com.viettel.tvbox.services.token

import UserPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userPreferences: UserPreferences,
    private val onLogout: () -> Unit
) : Interceptor {
    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Add Authorization header if token exists
        val token = userPreferences.getToken()
        if (token.isNotEmpty()) {
            request = request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        }

        val response = chain.proceed(request)

        // Token refresh logic can be added here if needed
        // if (response.code == 401) { ... }

        return response
    }
}
