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
import com.example.proyecfclientes.R

class CategoriasFragment : Fragment() {

    private lateinit var binding: FragmentCategoriasBinding
    private lateinit var viewModel: CategoriasViewModel
    private lateinit var adapter: CategoriasAdapter
    private var categoriasOriginal: List<Categoria> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoriasBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[CategoriasViewModel::class.java]

        // Configurar RecyclerView
        adapter = CategoriasAdapter(emptyList()) { categoria ->
            val bundle = Bundle()
            bundle.putInt("categoriaId", categoria.id)
            findNavController().navigate(R.id.action_categoriasFragment_to_trabajadoresFragment, bundle)
        }

        binding.recyclerViewCategorias.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCategorias.adapter = adapter

        // Botón "Mis Citas"
        binding.btnMisCitas.setOnClickListener {
            findNavController().navigate(R.id.action_categoriasFragment_to_misCitasFragment)
        }

        // Buscador
        binding.etBuscador.addTextChangedListener { texto ->
            val filtro = texto.toString().lowercase()
            val filtradas = categoriasOriginal.filter { it.name.lowercase().contains(filtro) }
            adapter.actualizarLista(filtradas)
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
