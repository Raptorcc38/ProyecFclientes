package com.example.proyecfclientes.Data.response

data class CitaResponse(
    val id: Int?,
    val worker_id: Int?,
    val user_id: Int?,
    val appointment_date: String?,
    val appointment_time: String?,
    val category_selected_id: Int?,
    val latitude: String?,
    val longitude: String?,
    val status: Int?,
    val worker: WorkerResponse?,
    val category: CategoriaResponse?,
    val client: ClientResponse?
)

data class WorkerResponse(
    val id: Int?,
    val user_id: Int?,
    val picture_url: String?,
    val average_rating: String?,
    val reviews_count: Int?,
    val user: UserResponse?
)

data class CategoriaResponse(
    val id: Int?,
    val name: String?
)

data class ClientResponse(
    val id: Int?,
    val name: String?,
    val last_name: String?,
    val type: Int?
)

data class UserResponse(
    val id: Int?,
    val name: String?,
    val email: String?
)
