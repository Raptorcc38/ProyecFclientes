package com.example.proyecfclientes.repository

import android.content.Context
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.requests.CrearCitaRequest
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.utils.Preferencias
import retrofit2.Response

class RepositorioCitas {
    suspend fun crearCita(workerId: Int, categorySelectedId: Int, context: Context): Response<Cita> {
        val token = Preferencias.obtenerToken(context)
        val body = CrearCitaRequest(workerId, categorySelectedId)
        return ApiClient.retrofit.crearCita("Bearer $token", body)
    }
}
