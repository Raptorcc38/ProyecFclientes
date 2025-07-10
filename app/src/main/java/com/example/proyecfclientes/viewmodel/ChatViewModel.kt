package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.Data.requests.CrearCitaRequest
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.launch
import retrofit2.Response

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val _mensajes = MutableLiveData<List<Mensaje>>()
    val mensajes: LiveData<List<Mensaje>> = _mensajes

    private val _mensajeEnviado = MutableLiveData<Boolean>()
    val mensajeEnviado: LiveData<Boolean> = _mensajeEnviado

    private val _citaFinalizadaSinCalificar = MutableLiveData<Boolean>()
    val citaFinalizadaSinCalificar: LiveData<Boolean> = _citaFinalizadaSinCalificar
    private val _calificacionExitosa = MutableLiveData<Boolean>()
    val calificacionExitosa: LiveData<Boolean> = _calificacionExitosa

    private var citaActual: Cita? = null

    fun cargarMensajesDeCita(appointmentId: Int) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = "Bearer ${Preferencias.getToken(context)}"
                val response = ApiClient.retrofit.obtenerMensajesCita(token, appointmentId)
                if (response.isSuccessful && response.body() != null) {
                    _mensajes.value = response.body()!!
                }
            } catch (e: Exception) {
                _mensajes.value = emptyList()
            }
        }
    }

    fun enviarMensaje(appointmentId: Int, mensaje: String, receiverId: Int) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = "Bearer ${Preferencias.getToken(context)}"
                val response = ApiClient.retrofit.enviarMensajeCita(
                    token,
                    appointmentId,
                    mapOf("message" to mensaje, "receiver_id" to receiverId.toString())
                )
                _mensajeEnviado.value = response.isSuccessful
                if (response.isSuccessful) {
                    cargarMensajesDeCita(appointmentId)
                }
            } catch (e: Exception) {
                _mensajeEnviado.value = false
            }
        }
    }

    fun verificarCitaFinalizadaSinCalificar(cita: Cita?) {
        // status == 1: concretada, status == 2: calificada
        _citaFinalizadaSinCalificar.value = (cita?.status == 1)
        citaActual = cita
    }

    fun calificarCita(appointmentId: Int, rating: Float, comentario: String) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = "Bearer ${Preferencias.getToken(context)}"
                val body = mapOf(
                    "rating" to rating,
                    "comment" to comentario
                )
                val response = ApiClient.retrofit.calificarCita(token, appointmentId, body)
                _calificacionExitosa.value = response.isSuccessful
            } catch (e: Exception) {
                _calificacionExitosa.value = false
            }
        }
    }
}
