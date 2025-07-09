package com.example.proyecfclientes.network
import com.example.proyecfclientes.Data.modelo.Categoria
import com.example.proyecfclientes.Data.modelo.Trabajador
import com.example.proyecfclientes.Data.requests.LoginRequest
import com.example.proyecfclientes.Data.requests.RegistroRequest
import com.example.proyecfclientes.Data.response.LoginResponse
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.requests.CrearCitaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Header

interface ApiService {
    // Endpoint para login (POST /client/login)
    @POST("client/login")
    suspend fun loginCliente(@Body request: LoginRequest): Response<LoginResponse>

    // Endpoint para registro (POST /client/register)
    @POST("client/register")
    suspend fun registroCliente(@Body request: RegistroRequest): Response<Unit>

    // network/ApiService.kt
    @GET("categories")
    suspend fun obtenerCategorias(): Response<List<Categoria>>

    @GET("categories/{id}/workers")
    suspend fun obtenerTrabajadoresPorCategoria(@Path("id") categoriaId: Int): Response<List<Trabajador>>

    // Obtener mensajes del chat de una cita
    @GET("appointments/{id}/chats")
    suspend fun getMensajesCita(
        @Path("id") appointmentId: Int,
        @Header("Authorization") token: String
    ): Response<List<Mensaje>>

    // Enviar mensaje al chat de una cita
    @POST("appointments/{id}/chats")
    suspend fun enviarMensajeCita(
        @Path("id") appointmentId: Int,
        @Header("Authorization") token: String,
        @Body body: Map<String, Any>
    ): Response<Unit>

    // Crear una cita (POST /appointments)
    @POST("appointments")
    suspend fun crearCita(
        @Header("Authorization") token: String,
        @Body body: CrearCitaRequest
    ): Response<Cita>

}
