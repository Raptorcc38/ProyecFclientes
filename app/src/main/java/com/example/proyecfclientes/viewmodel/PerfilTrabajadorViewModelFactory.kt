package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecfclientes.repository.RepositorioCitas

class PerfilTrabajadorViewModelFactory(
    private val application: Application,
    private val repositorioCitas: RepositorioCitas
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilTrabajadorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilTrabajadorViewModel(application, repositorioCitas) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

