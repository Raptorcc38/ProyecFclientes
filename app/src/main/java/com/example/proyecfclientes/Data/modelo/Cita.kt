package com.example.proyecfclientes.Data.modelo

data class Cita(
    val id: Int?,
    val worker_id: Int?,
    val user_id: Int?,
    val appointment_date: String?,
    val appointment_time: String?,
    val category_selected_id: Int?,
    val latitude: String?,
    val longitude: String?,
    val status: Int?,
    val worker: Trabajador?,
    val category: Categoria?,
    val client: User?
)
