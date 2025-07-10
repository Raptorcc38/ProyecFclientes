package com.example.proyecfclientes.Data.modelo

import java.io.Serializable

data class Resena(
    val id: Int?,
    val rating: Double?,
    val comment: String?,
    val created_at: String?,
    val user: User?
) : Serializable

