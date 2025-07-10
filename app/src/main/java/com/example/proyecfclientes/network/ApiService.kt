package com.example.proyecfclientes.network

import com.example.proyecfclientes.Data.modelo.*
import com.example.proyecfclientes.Data.requests.*
import com.example.proyecfclientes.Data.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // AUTENTICACIÓN
    @POST("client/login")
    suspend fun loginCliente(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("client/register")
    suspend fun registrarCliente(
        @Body request: RegistroRequest
    ): Response<LoginResponse>

    // CATEGORÍAS
    @GET("categories")
    suspend fun obtenerCategorias(
        @Header("Authorization") token: String
    ): Response<List<Categoria>>

    @GET("categories/{categoriaId}/workers")
    suspend fun obtenerTrabajadoresPorCategoria(
        @Header("Authorization") token: String,
        @Path("categoriaId") categoriaId: Int
    ): Response<List<Trabajador>>

    // CITAS
    @POST("appointments")
    suspend fun crearCita(
        @Header("Authorization") token: String,
        @Body request: CrearCitaRequest
    ): Response<Cita>

    @POST("appointments/{appointmentId}/make")
    suspend fun concretarCita(
        @Header("Authorization") token: String,
        @Path("appointmentId") appointmentId: Int,
        @Body request: ConcretarCitaRequest
    ): Response<Cita>

    @GET("appointments")
    suspend fun obtenerCitas(
        @Header("Authorization") token: String
    ): Response<List<Cita>>

    @GET("appointments/{appointmentId}")
    suspend fun obtenerCitaPorId(
        @Header("Authorization") token: String,
        @Path("appointmentId") appointmentId: Int
    ): Response<Cita>

    // CHAT (usa appointmentId para consistencia)
    @GET("appointments/{appointmentId}/chats")
    suspend fun obtenerMensajesChat(
        @Header("Authorization") token: String,
        @Path("appointmentId") appointmentId: Int
    ): Response<List<Mensaje>>

    @POST("appointments/{appointmentId}/chats")
    suspend fun enviarMensajeChat(
        @Header("Authorization") token: String,
        @Path("appointmentId") appointmentId: Int,
        @Body request: MensajeRequest
    ): Response<Unit>

    // TRABAJADOR
    @GET("workers/{trabajadorId}")
    suspend fun obtenerDetalleTrabajador(
        @Header("Authorization") token: String,
        @Path("trabajadorId") trabajadorId: Int
    ): Response<TrabajadorDetalle>

    // REVIEW / RESEÑAS
    @POST("appointments/{appointmentId}/review")
    suspend fun enviarReview(
        @Header("Authorization") token: String,
        @Path("appointmentId") appointmentId: Int,
        @Body reviewRequest: ReviewRequest
    ): Response<Unit>

    @GET("workers/{trabajadorId}/reviews")
    suspend fun obtenerResenas(
        @Header("Authorization") token: String,
        @Path("trabajadorId") trabajadorId: Int
    ): Response<List<Resena>>
}
