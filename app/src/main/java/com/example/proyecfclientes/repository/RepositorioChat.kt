package com.example.proyecfclientes.repository

import android.content.Context
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.Data.requests.MensajeRequest
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.utils.Preferencias
import retrofit2.Response

class RepositorioChat {

    suspend fun getMensajes(appointmentId: Int, context: Context): Response<List<Mensaje>> {
        val token = Preferencias.getToken(context)
        return ApiClient.retrofit.obtenerMensajesChat(
            token = "Bearer $token",
            citaId = appointmentId
        )
    }

    suspend fun enviarMensaje(appointmentId: Int, mensaje: String, receiverId: Int, context: Context): Response<Unit> {
        val token = Preferencias.getToken(context)
        val body = MensajeRequest(message = mensaje, receiver_id = receiverId)
        return ApiClient.retrofit.enviarMensajeChat(
            token = "Bearer $token",
            citaId = appointmentId,
            request = body
        )
    }
}
