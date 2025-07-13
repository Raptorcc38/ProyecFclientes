package com.example.proyecfclientes.Data.requests

data class ReviewRequest(
    val rating: Int,
    val review: String,
    val didTheJob: Boolean
)
