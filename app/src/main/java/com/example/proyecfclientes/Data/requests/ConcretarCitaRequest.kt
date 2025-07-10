package com.example.proyecfclientes.Data.requests

data class ConcretarCitaRequest(
    val appointment_date: String,
    val appointment_time: String,
    val latitude: String,
    val longitude: String
)
