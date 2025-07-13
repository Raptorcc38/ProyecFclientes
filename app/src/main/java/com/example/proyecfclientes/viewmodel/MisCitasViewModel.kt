package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.repository.RepositorioCitas
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.launch
import retrofit2.Response

class MisCitasViewModel(
    application: Application,
    private val repo: RepositorioCitas
) : AndroidViewModel(application) {

    val citas = MutableLiveData<List<Cita>?>()
    val error = MutableLiveData<String?>()
    val resultadoConcretar = MutableLiveData<Response<Cita>?>()


    fun cargarCitas() {
        viewModelScope.launch {
            try {
                val token = Preferencias.getToken(getApplication<Application>().applicationContext) ?: ""
                val response = repo.obtenerCitas(token)
                if (response.isSuccessful) {
                    citas.postValue(response.body())
                } else {
                    error.postValue("No se pudieron cargar las citas")
                    citas.postValue(null)
                }
            } catch (e: Exception) {
                error.postValue("Error de conexión al cargar citas")
                citas.postValue(null)
            }
        }
    }


    fun concretarCita(token: String, citaId: Int, request: ConcretarCitaRequest) {
        viewModelScope.launch {
            try {
                val response = repo.concretarCita(token, citaId, request)
                resultadoConcretar.postValue(response)
            } catch (e: Exception) {
                resultadoConcretar.postValue(null)
            }
        }
    }
}
