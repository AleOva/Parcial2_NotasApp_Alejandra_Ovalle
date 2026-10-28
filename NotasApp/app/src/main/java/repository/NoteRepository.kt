package com.example.notasapp.repository

import com.example.notasapp.models.NoteRequest
import com.example.notasapp.network.RetrofitClient

class NoteRepository {
    suspend fun getNotes() = RetrofitClient.apiService.getNotes()
    suspend fun createNote(title: String, content: String) =
        RetrofitClient.apiService.createNote(NoteRequest(title, content))
}