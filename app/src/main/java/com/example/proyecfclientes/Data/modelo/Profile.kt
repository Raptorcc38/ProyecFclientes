package com.example.proyecfclientes.Data.modelo

import java.io.Serializable

// Modelo para el perfil anidado en User

data class Profile(
    val id: Int?,
    val name: String?,
    val last_name: String?,
    val type: Int?
) : Serializable

