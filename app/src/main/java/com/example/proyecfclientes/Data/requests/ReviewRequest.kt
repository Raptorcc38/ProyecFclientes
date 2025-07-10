package com.example.proyecfclientes.Data.requests

data class ReviewRequest(
    val rating: Int,           // Puntuación, normalmente 1-10
    val review: String,        // Comentario de la reseña
    val didTheJob: Boolean     // Si el trabajador hizo el trabajo o no
)
