package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecfclientes.databinding.FragmentCategoriasBinding
import com.example.proyecfclientes.viewmodel.CategoriasViewModel
import com.example.proyecfclientes.ui.adapters.CategoriasAdapter
import com.example.proyecfclientes.Data.modelo.Categoria

class CategoriasFragment : Fragment() {

    private lateinit var binding: FragmentCategoriasBinding
    private lateinit var viewModel: CategoriasViewModel
    private lateinit var adapter: CategoriasAdapter
    private var categoriasOriginal: List<Categoria> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoriasBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[CategoriasViewModel::class.java]

        adapter = CategoriasAdapter(emptyList()) { categoria ->
            val action = CategoriasFragmentDirections.actionCategoriasFragmentToTrabajadoresFragment(categoria.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewCategorias.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCategorias.adapter = adapter


        binding.etBuscador.addTextChangedListener { texto ->
            val filtro = texto.toString().lowercase()
            val filtradas = categoriasOriginal.filter { it.name.lowercase().contains(filtro) }
            adapter.actualizarLista(filtradas)
        }


        binding.btnMisCitas.setOnClickListener {
            findNavController().navigate(CategoriasFragmentDirections.actionCategoriasFragmentToMisCitasFragment())
        }

        observarViewModel()
        viewModel.cargarCategorias()

        return binding.root
    }

    private fun observarViewModel() {
        viewModel.categorias.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful && response.body() != null) {
                categoriasOriginal = response.body() ?: emptyList()
                adapter.actualizarLista(categoriasOriginal)
            } else {
                Toast.makeText(requireContext(), "Error al obtener categorías", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
