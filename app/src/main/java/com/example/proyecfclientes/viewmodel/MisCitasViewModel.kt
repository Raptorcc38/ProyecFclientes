package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.repository.RepositorioCitas
import kotlinx.coroutines.launch
import retrofit2.Response

class MisCitasViewModel(
    application: Application,
    private val repo: RepositorioCitas
) : AndroidViewModel(application) {

    val citas = MutableLiveData<List<Cita>?>()
    val resultadoConcretar = MutableLiveData<Response<Cita>?>()

    // Obtener todas las citas del cliente
    fun obtenerCitas(token: String) {
        viewModelScope.launch {
            try {
                val response = repo.obtenerCitas(token)
                if (response.isSuccessful) {
                    citas.postValue(response.body())
                } else {
                    citas.postValue(null)
                }
            } catch (e: Exception) {
                citas.postValue(null)
            }
        }
    }

    // Concretar cita
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
