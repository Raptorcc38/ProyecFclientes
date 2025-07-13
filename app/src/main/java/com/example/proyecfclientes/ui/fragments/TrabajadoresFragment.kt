package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecfclientes.Data.modelo.Trabajador
import com.example.proyecfclientes.databinding.FragmentTrabajadoresBinding
import com.example.proyecfclientes.ui.adapters.TrabajadoresAdapter
import com.example.proyecfclientes.viewmodel.TrabajadoresViewModel

class TrabajadoresFragment : Fragment() {

    private lateinit var binding: FragmentTrabajadoresBinding
    private lateinit var viewModel: TrabajadoresViewModel
    private lateinit var adapter: TrabajadoresAdapter
    private var listaOriginal: List<Trabajador> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTrabajadoresBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[TrabajadoresViewModel::class.java]

        val categoriaId = arguments?.getInt("categoriaId") ?: 0

        adapter = TrabajadoresAdapter(emptyList()) { trabajador ->

            val action = TrabajadoresFragmentDirections.actionTrabajadoresFragmentToPerfilTrabajadorFragment(trabajador, categoriaId)
            findNavController().navigate(action)
        }

        binding.recyclerViewTrabajadores.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTrabajadores.adapter = adapter


        binding.etBuscadorTrabajador.addTextChangedListener { texto ->
            val filtro = texto.toString().lowercase()
            val filtradas = listaOriginal.filter {
                val nombre = it.user?.name?.lowercase() ?: ""
                val apellido = it.user?.last_name?.lowercase() ?: ""
                nombre.contains(filtro) || apellido.contains(filtro)
            }
            adapter.actualizarLista(filtradas)
        }

        observarViewModel()
        viewModel.cargarTrabajadores(categoriaId)

        return binding.root
    }

    private fun observarViewModel() {
        viewModel.trabajadores.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful && response.body() != null) {
                listaOriginal = response.body() ?: emptyList()
                adapter.actualizarLista(listaOriginal)
            } else {
                Toast.makeText(requireContext(), "Error al obtener trabajadores", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
