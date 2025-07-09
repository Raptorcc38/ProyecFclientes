package com.example.proyecfclientes.repository

import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.utils.Preferencias
import retrofit2.Response

class RepositorioChat {
    suspend fun getMensajes(appointmentId: Int, context: android.content.Context): Response<List<Mensaje>> {
        val token = Preferencias.obtenerToken(context)
        return ApiClient.retrofit.getMensajesCita(appointmentId, "Bearer $token")
    }

    suspend fun enviarMensaje(appointmentId: Int, mensaje: String, context: android.content.Context): Response<Unit> {
        val token = Preferencias.obtenerToken(context)
        // Ajusta el receiver_id según tu lógica de usuario actual
        val body = mapOf("message" to mensaje, "receiver_id" to 1)
        return ApiClient.retrofit.enviarMensajeCita(appointmentId, "Bearer $token", body)
    }
}
