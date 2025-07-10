package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecfclientes.Data.requests.LoginRequest
import com.example.proyecfclientes.Data.response.LoginResponse
import com.example.proyecfclientes.network.TokenManager
import com.example.proyecfclientes.repository.RepositorioClientes
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RepositorioClientes()
    private val _loginResponse = MutableLiveData<Response<LoginResponse>>()
    val loginResponse: LiveData<Response<LoginResponse>> = _loginResponse

    private fun extraerUserIdDeJWT(token: String): Int? {
        return try {
            val partes = token.split(".")
            if (partes.size != 3) return null
            val payload = android.util.Base64.decode(partes[1], android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP)
            val json = String(payload, Charsets.UTF_8)
            val regex = Regex("\\\"(user_id|id)\\\"\\s*:\\s*(\\d+)")
            val match = regex.find(json)
            match?.groups?.get(2)?.value?.toIntOrNull()
        } catch (e: Exception) {
            null
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = repo.loginCliente(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.access_token?.let { token ->
                    // Guarda el token en Preferencias (persistente)
                    val userId = extraerUserIdDeJWT(token)
                    if (userId != null) {
                        Preferencias.guardarTokenYUserId(getApplication(), token, userId)
                    } else {
                        Preferencias.guardarToken(getApplication(), token)
                    }
                    // También actualiza el TokenManager (en memoria)
                    TokenManager.token = token
                }
            }
            _loginResponse.value = response
        }
    }
}
