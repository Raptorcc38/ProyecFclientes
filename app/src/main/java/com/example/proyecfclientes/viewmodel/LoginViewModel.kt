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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = repo.loginCliente(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.access_token?.let { token ->
                    // Guarda el token en Preferencias (persistente)
                    Preferencias.guardarToken(getApplication(), token)
                    // También actualiza el TokenManager (en memoria)
                    TokenManager.token = token
                }
            }
            _loginResponse.value = response
        }
    }
}
