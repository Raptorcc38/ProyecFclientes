package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.repository.RepositorioChat
import kotlinx.coroutines.launch
import retrofit2.Response

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RepositorioChat()
    private val _mensajes = MutableLiveData<Response<List<Mensaje>>>()
    val mensajes: LiveData<Response<List<Mensaje>>> = _mensajes
    private val _envioExitoso = MutableLiveData<Boolean>()
    val envioExitoso: LiveData<Boolean> = _envioExitoso

    fun cargarMensajes(appointmentId: Int) {
        viewModelScope.launch {
            val response = repo.getMensajes(appointmentId, getApplication<Application>().applicationContext)
            _mensajes.value = response
        }
    }

    fun enviarMensaje(appointmentId: Int, mensaje: String) {
        viewModelScope.launch {
            val response = repo.enviarMensaje(appointmentId, mensaje, getApplication<Application>().applicationContext)
            _envioExitoso.value = response.isSuccessful
        }
    }
}
