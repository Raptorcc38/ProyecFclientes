package com.example.proyecfclientes.Data.modelo

data class Mensaje(
    val id: Int,
    val message: String,
    val sender_id: Int,
    val receiver_id: Int,
    val created_at: String
)
