package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecfclientes.Data.requests.RegistroRequest
import com.example.proyecfclientes.repository.RepositorioClientes
import kotlinx.coroutines.launch
import retrofit2.Response

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RepositorioClientes()
    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    fun registrar(nombre: String, apellido: String, email: String, password: String) {
        viewModelScope.launch {
            val response = repo.registrarCliente(RegistroRequest(nombre, apellido, email, password))
            _registroExitoso.value = response.isSuccessful
        }
    }
}

