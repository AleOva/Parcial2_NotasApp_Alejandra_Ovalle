package com.example.notasapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notasapp.models.Note
import com.example.notasapp.repository.NoteRepository
import kotlinx.coroutines.launch

class ListaNotasViewModel : ViewModel() {
    private val repository = NoteRepository()

    private val _notas = MutableLiveData<List<Note>>()
    val notas: LiveData<List<Note>> = _notas

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun cargarNotas() {
        viewModelScope.launch {
            try {
                val response = repository.getNotes()
                if (response.isSuccessful && response.body() != null) {
                    _notas.value = response.body()
                } else {
                    _error.value = "Error al cargar las notas"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
        }
    }
}