package com.example.proyecfclientes.repository

import com.example.proyecfclientes.Data.modelo.Trabajador
import com.example.proyecfclientes.network.ApiClient
import retrofit2.Response

class RepositorioTrabajadores {
    suspend fun getTrabajadoresPorCategoria(token: String, categoriaId: Int): Response<List<Trabajador>> {
        return ApiClient.retrofit.obtenerTrabajadoresPorCategoria("Bearer $token", categoriaId)
    }
}
