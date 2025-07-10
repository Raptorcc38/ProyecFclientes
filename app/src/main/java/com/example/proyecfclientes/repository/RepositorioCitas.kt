package com.example.proyecfclientes.repository

import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.Data.modelo.TrabajadorDetalle
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.Data.requests.CrearCitaRequest
import com.example.proyecfclientes.Data.requests.MensajeRequest
import com.example.proyecfclientes.network.ApiClient
import retrofit2.Response

class RepositorioCitas(
    private val api: com.example.proyecfclientes.network.ApiService
) {
    suspend fun crearCita(token: String, request: CrearCitaRequest): Response<Cita> {
        return api.crearCita(token = "Bearer $token", request = request)
    }

    suspend fun concretarCita(token: String, appointmentId: Int, request: ConcretarCitaRequest): Response<Cita> {
        return api.concretarCita(token = "Bearer $token", appointmentId = appointmentId, request = request)
    }

    suspend fun obtenerCitas(token: String): Response<List<Cita>> {
        return api.obtenerCitas(token = "Bearer $token")
    }

    suspend fun obtenerCitaPorId(token: String, appointmentId: Int): Response<Cita> {
        return api.obtenerCitaPorId(token = "Bearer $token", appointmentId = appointmentId)
    }

    suspend fun obtenerMensajesChat(token: String, appointmentId: Int): Response<List<Mensaje>> {
        return api.obtenerMensajesChat(token = "Bearer $token", citaId = appointmentId)
    }

    suspend fun enviarMensajeChat(token: String, appointmentId: Int, mensajeRequest: MensajeRequest): Response<Unit> {
        return api.enviarMensajeChat(token = "Bearer $token", citaId = appointmentId, request = mensajeRequest)
    }
    suspend fun obtenerDetalleTrabajador(token: String, trabajadorId: Int): Response<TrabajadorDetalle> {
        return api.obtenerDetalleTrabajador(token = "Bearer $token", trabajadorId = trabajadorId)
    }

}
