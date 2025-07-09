package com.example.proyecfclientes.network

import com.example.proyecfclientes.utils.Preferencias
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://trabajos.jmacboy.com/api/"

    // TokenManager: una función que obtiene el token de SharedPreferences
    private fun getToken(): String? {
        // Usa el contexto global de la app
        return TokenManager.token
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(AutenticacionInterceptor { getToken() })
        .build()

    val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
