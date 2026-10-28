package com.example.notasapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notasapp.repository.NoteRepository
import kotlinx.coroutines.launch

class CrearNotaViewModel : ViewModel() {
    private val repository = NoteRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _exito = MutableLiveData<Boolean>()
    val exito: LiveData<Boolean> = _exito

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun guardarNota(titulo: String, contenido: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.createNote(titulo, contenido)
                if (response.isSuccessful) {
                    _exito.value = true
                } else {
                    _error.value = "Error al guardar la nota"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}