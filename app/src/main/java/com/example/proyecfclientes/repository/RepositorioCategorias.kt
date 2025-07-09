// repository/RepositorioCategorias.kt
package com.example.proyecfclientes.repository

import com.example.proyecfclientes.Data.modelo.Categoria
import com.example.proyecfclientes.network.ApiClient
import retrofit2.Response

class RepositorioCategorias {
    suspend fun getCategorias(): Response<List<Categoria>> {
        return ApiClient.retrofit.obtenerCategorias()
    }
}
