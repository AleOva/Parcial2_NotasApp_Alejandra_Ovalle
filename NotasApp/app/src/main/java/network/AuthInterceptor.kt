package com.example.notasapp.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    // Leemos el token que guardamos en el Login
    private val prefs = context.getSharedPreferences("NotasAppPrefs", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = prefs.getString("TOKEN", null)

        // Si hay un token, lo agregamos al Header
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}