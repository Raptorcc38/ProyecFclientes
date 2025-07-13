package com.example.proyecfclientes.Data.response

data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)
