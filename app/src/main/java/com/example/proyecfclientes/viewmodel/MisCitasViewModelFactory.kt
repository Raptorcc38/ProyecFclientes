package com.example.proyecfclientes.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecfclientes.repository.RepositorioCitas

class MisCitasViewModelFactory(
    private val application: Application,
    private val repositorioCitas: RepositorioCitas
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisCitasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MisCitasViewModel(application, repositorioCitas) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
