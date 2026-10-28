package com.example.notasapp.repository

import com.example.notasapp.models.LoginRequest
import com.example.notasapp.network.RetrofitClient

class AuthRepository {
    suspend fun login(email: String, password: String) =
        RetrofitClient.apiService.login(LoginRequest(email, password))
}