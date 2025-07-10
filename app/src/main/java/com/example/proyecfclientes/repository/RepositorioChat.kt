package com.example.proyecfclientes.repository

import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.utils.Preferencias
import retrofit2.Response

class RepositorioChat {
    suspend fun getMensajes(appointmentId: Int, context: android.content.Context): Response<List<Mensaje>> {
        val token = com.example.proyecfclientes.utils.Preferencias.getToken(context)
        return ApiClient.retrofit.obtenerMensajesCita(token = "Bearer $token", appointmentId = appointmentId)
    }

    suspend fun enviarMensaje(appointmentId: Int, mensaje: String, context: android.content.Context): Response<Unit> {
        val token = com.example.proyecfclientes.utils.Preferencias.getToken(context)
        // Solo envía el mensaje como String, sin receiver_id
        val body = mapOf("message" to mensaje)
        return ApiClient.retrofit.enviarMensajeCita(token = "Bearer $token", appointmentId = appointmentId, mensaje = body)
    }
}
