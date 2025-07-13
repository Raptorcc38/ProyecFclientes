package com.example.proyecfclientes.Data.modelo

import java.io.Serializable

data class TrabajadorDetalle(
    val id: Int?,
    val user_id: Int?,
    val picture_url: String?,
    val average_rating: Double?,
    val reviews_count: Int?,
    val user: User?,
    val categories: List<Categoria>?,
    val reviews: List<Resena>?

) : Serializable

