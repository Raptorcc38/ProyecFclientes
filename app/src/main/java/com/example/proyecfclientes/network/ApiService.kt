package com.example.proyecfclientes.network

import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.Data.requests.CrearCitaRequest
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.modelo.Categoria
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // ...otros métodos...

    // Obtener mensajes del chat de una cita
    @GET("appointments/{id}/chats")
    suspend fun obtenerMensajesCita(
        @Header("Authorization") token: String,
        @Path("id") appointmentId: Int
    ): Response<List<Mensaje>>

    // Enviar mensaje al chat de una cita
    @POST("appointments/{id}/chats")
    suspend fun enviarMensajeCita(
        @Header("Authorization") token: String,
        @Path("id") appointmentId: Int,
        @Body mensaje: Map<String, String>
    ): Response<Unit>

    // Crear cita
    @POST("appointments")
    suspend fun crearCita(
        @Header("Authorization") token: String,
        @Body request: CrearCitaRequest
    ): Response<Cita>

    // Concretar cita
    @POST("appointments/{id}/make")
    suspend fun concretarCita(
        @Header("Authorization") token: String,
        @Path("id") citaId: Int,
        @Body request: ConcretarCitaRequest
    ): Response<Cita>

    // Obtener categorías
    @GET("categories")
    suspend fun obtenerCategorias(): Response<List<Categoria>>

    // Login de cliente
    @POST("client/login")
    suspend fun loginCliente(@Body request: com.example.proyecfclientes.Data.requests.LoginRequest): retrofit2.Response<com.example.proyecfclientes.Data.response.LoginResponse>

    // Registro de cliente
    @POST("api/client/register")
    suspend fun registroCliente(@Body request: com.example.proyecfclientes.Data.requests.RegistroRequest): retrofit2.Response<Unit>

    // Obtener trabajadores por categoría
    @GET("categories/{id}/workers")
    suspend fun obtenerTrabajadoresPorCategoria(@Path("id") categoriaId: Int): Response<List<com.example.proyecfclientes.Data.modelo.Trabajador>>

    // Obtener detalle de un trabajador (categorías y reseñas)
    @GET("workers/{id}")
    suspend fun obtenerDetalleTrabajador(
        @Path("id") trabajadorId: Int
    ): Response<com.example.proyecfclientes.Data.modelo.TrabajadorDetalle>

    // Calificar cita
    @POST("appointments/{id}/review")
    suspend fun calificarCita(
        @Header("Authorization") token: String,
        @Path("id") appointmentId: Int,
        @Body body: Map<String, Any>
    ): Response<Unit>

    // Obtener citas
    @GET("appointments")
    suspend fun obtenerCitas(
        @Header("Authorization") token: String
    ): Response<List<com.example.proyecfclientes.Data.modelo.Cita>>

    // Obtener cita por ID
    @GET("appointments/{id}")
    suspend fun obtenerCitaPorId(
        @Header("Authorization") token: String,
        @Path("id") citaId: Int
    ): Response<Cita>
}
