package com.example.notasapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notasapp.models.LoginResponse
import com.example.notasapp.repository.AuthRepository
import kotlinx.coroutines.launch
import com.example.notasapp.network.RetrofitClient
class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = response.body()
                    RetrofitClient.authToken = response.body()!!.token
// Guardar el token en el cliente de Retrofit
                    RetrofitClient.authToken = response.body()!!.token
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}