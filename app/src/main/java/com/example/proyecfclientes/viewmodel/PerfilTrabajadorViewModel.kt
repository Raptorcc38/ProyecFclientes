package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.Data.modelo.TrabajadorDetalle
import com.example.proyecfclientes.Data.requests.CrearCitaRequest
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.repository.RepositorioCitas
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.launch
import retrofit2.Response

class PerfilTrabajadorViewModel(
    application: Application,
    private val repositorioCitas: RepositorioCitas
) : AndroidViewModel(application) {

    val citaCreada = MutableLiveData<Cita?>()
    val detalleTrabajador = MutableLiveData<TrabajadorDetalle?>()
    val cargandoDetalle = MutableLiveData<Boolean>()

    fun crearCita(workerId: Int?, categoriaId: Int) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${Preferencias.getToken(getApplication())}"
                val request = CrearCitaRequest(workerId ?: 0, categoriaId)
                val response = repositorioCitas.crearCita(token, request)
                if (response.isSuccessful && response.body() != null) {
                    citaCreada.postValue(response.body())
                } else {
                    citaCreada.postValue(null)
                }
            } catch (e: Exception) {
                citaCreada.postValue(null)
            }
        }
    }

    fun cargarDetalleTrabajador(trabajadorId: Int) {
        viewModelScope.launch {
            cargandoDetalle.postValue(true)
            try {
                val response: Response<TrabajadorDetalle> = ApiClient.retrofit.obtenerDetalleTrabajador(trabajadorId)
                if (response.isSuccessful && response.body() != null) {
                    detalleTrabajador.postValue(response.body())
                } else {
                    detalleTrabajador.postValue(null)
                }
            } catch (e: Exception) {
                detalleTrabajador.postValue(null)
            } finally {
                cargandoDetalle.postValue(false)
            }
        }
    }
}
