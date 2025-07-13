package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.Data.requests.MensajeRequest
import com.example.proyecfclientes.repository.RepositorioCitas
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel(
    application: Application,
    private val repositorioCitas: RepositorioCitas
) : AndroidViewModel(application) {

    private val _mensajes = MutableLiveData<List<Mensaje>>()
    val mensajes: LiveData<List<Mensaje>> = _mensajes

    private val _enviando = MutableLiveData<Boolean>()
    val enviando: LiveData<Boolean> = _enviando

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var autoUpdateJob: Job? = null

    fun cargarMensajes(appointmentId: Int) {
        viewModelScope.launch {
            try {
                val token = Preferencias.getToken(getApplication<Application>().applicationContext) ?: ""
                val response = repositorioCitas.obtenerMensajesChat(token, appointmentId)
                if (response.isSuccessful) {
                    _mensajes.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar mensajes"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }

    fun enviarMensaje(appointmentId: Int, mensaje: String, receiverId: Int) {
        _enviando.value = true
        viewModelScope.launch {
            try {
                val token = Preferencias.getToken(getApplication<Application>().applicationContext) ?: ""
                val request = MensajeRequest(message = mensaje, receiver_id = receiverId)
                val response = repositorioCitas.enviarMensajeChat(token, appointmentId, request)
                if (response.isSuccessful) {
                    cargarMensajes(appointmentId) // refresca
                    _enviando.value = false
                } else {
                    _error.value = "No se pudo enviar el mensaje"
                    _enviando.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
                _enviando.value = false
            }
        }
    }

    fun startAutoUpdate(appointmentId: Int) {
        autoUpdateJob?.cancel()
        autoUpdateJob = viewModelScope.launch {
            while (true) {
                cargarMensajes(appointmentId)
                delay(30_000)
            }
        }
    }

    fun stopAutoUpdate() {
        autoUpdateJob?.cancel()
    }

    fun clearError() {
        _error.value = null
    }
}
