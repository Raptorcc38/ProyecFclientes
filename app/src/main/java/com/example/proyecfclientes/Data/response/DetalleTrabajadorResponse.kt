package com.example.proyecfclientes.Data.response

data class DetalleTrabajadorResponse(
    val id: Int?,
    val user_id: Int?,
    val picture_url: String?,
    val average_rating: String?,
    val reviews_count: Int?,
    val user: UserDetalleResponse?,
    val categories: List<CategoriaDetalleResponse>?,
    val reviews: List<ResenaResponse>?
)

data class UserDetalleResponse(
    val id: Int?,
    val name: String?,
    val last_name: String?,
    val type: Int?
)

data class CategoriaDetalleResponse(
    val id: Int?,
    val name: String?
)

data class ResenaResponse(
    val id: Int?,
    val user_id: Int?,
    val review: String?,
    val rating: Int?,
    val created_at: String?,
    val user: UserDetalleResponse?
)
