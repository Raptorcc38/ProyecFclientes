package com.example.proyecfclientes.Data.modelo

import java.io.Serializable

data class User(
    val id: Int?,
    val name: String?,
    val last_name: String?,
    val type: Int?
) : Serializable
