package com.example.notasapp.network

import com.example.notasapp.models.LoginRequest
import com.example.notasapp.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.notasapp.models.Note
import com.example.notasapp.models.NoteRequest
import retrofit2.http.GET // Agrega este import

interface ApiService {
    // CAMBIA "api/login" por la ruta real de tu backend
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @GET("api/notes")
    suspend fun getNotes(): Response<List<Note>>

    @POST("api/notes")
    suspend fun createNote(@Body request: NoteRequest): Response<Note>
}