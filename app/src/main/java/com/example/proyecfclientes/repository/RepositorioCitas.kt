package com.example.proyecfclientes.repository

import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.Data.requests.CrearCitaRequest
import com.example.proyecfclientes.network.ApiService
import retrofit2.Response

class RepositorioCitas(private val apiService: ApiService) {

    suspend fun crearCita(token: String, request: CrearCitaRequest): Response<Cita> {
        return apiService.crearCita(token, request)
    }

    suspend fun concretarCita(token: String, citaId: Int, request: ConcretarCitaRequest): Response<Cita> {
        return apiService.concretarCita(token, citaId, request)
    }

    suspend fun obtenerCitas(token: String): Response<List<Cita>> {
        return apiService.obtenerCitas(token)
    }
}
