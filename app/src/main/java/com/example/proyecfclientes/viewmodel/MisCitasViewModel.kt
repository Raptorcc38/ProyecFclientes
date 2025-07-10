package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.repository.RepositorioCitas
import kotlinx.coroutines.launch
import retrofit2.Response

class MisCitasViewModel(application: Application, private val repo: RepositorioCitas) : AndroidViewModel(application) {

    val resultadoConcretar = MutableLiveData<Response<Cita>>()

    // Agregado para el fragmento
    private val _citas = MutableLiveData<List<Cita>>()
    val citas: LiveData<List<Cita>> = _citas

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarCitas() {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = com.example.proyecfclientes.utils.Preferencias.getToken(context)
                val response = repo.obtenerCitas("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    _citas.value = response.body()
                } else {
                    _error.value = "Error al obtener citas"
                }
            } catch (e: Exception) {
                _error.value = "Error de red"
            }
        }
    }

    fun concretarCita(token: String, citaId: Int, request: ConcretarCitaRequest) {
        viewModelScope.launch {
            val response = repo.concretarCita(token, citaId, request)
            resultadoConcretar.postValue(response)
        }
    }
}

class MisCitasViewModelFactory(
    private val application: Application,
    private val repo: RepositorioCitas
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisCitasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MisCitasViewModel(application, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
