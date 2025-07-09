package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecfclientes.Data.modelo.Trabajador
import com.example.proyecfclientes.repository.RepositorioTrabajadores
import kotlinx.coroutines.launch
import retrofit2.Response

class TrabajadoresViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RepositorioTrabajadores()
    private val _trabajadores = MutableLiveData<Response<List<Trabajador>>>()
    val trabajadores: LiveData<Response<List<Trabajador>>> = _trabajadores

    fun cargarTrabajadores(categoriaId: Int) {
        viewModelScope.launch {
            val response = repo.getTrabajadoresPorCategoria(categoriaId)
            _trabajadores.value = response
        }
    }
}
