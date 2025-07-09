package com.example.proyecfclientes.Data.modelo

import java.io.Serializable

// Modelo para los mensajes del chat
// Puedes ajustar los campos según la respuesta real de la API

data class Mensaje(
    val id: Int,
    val message: String,
    val sender_id: Int,
    val receiver_id: Int,
    val created_at: String
) : Serializable

