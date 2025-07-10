package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecfclientes.Data.modelo.Categoria
import com.example.proyecfclientes.repository.RepositorioCategorias
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.launch
import retrofit2.Response

class CategoriasViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RepositorioCategorias()
    private val _categorias = MutableLiveData<Response<List<Categoria>>>()
    val categorias: LiveData<Response<List<Categoria>>> = _categorias

    fun cargarCategorias() {
        viewModelScope.launch {
            // OBTENER EL TOKEN DESDE LAS PREFERENCIAS
            val token = Preferencias.getToken(getApplication<Application>().applicationContext)
            val response = repo.getCategorias(token ?: "")

            _categorias.value = response
        }
    }
}
