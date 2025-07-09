package com.example.proyecfclientes.repository

import com.example.proyecfclientes.Data.requests.LoginRequest
import com.example.proyecfclientes.Data.requests.RegistroRequest
import com.example.proyecfclientes.Data.response.LoginResponse
import com.example.proyecfclientes.network.ApiClient
import retrofit2.Response

class RepositorioClientes {

    suspend fun loginCliente(loginRequest: LoginRequest): Response<LoginResponse> {
        return ApiClient.retrofit.loginCliente(loginRequest)
    }

    suspend fun registrarCliente(registroRequest: RegistroRequest): Response<Unit> {
        return ApiClient.retrofit.registroCliente(registroRequest)
    }
}
