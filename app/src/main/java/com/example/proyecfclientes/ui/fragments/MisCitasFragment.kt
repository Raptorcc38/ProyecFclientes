package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecfclientes.databinding.FragmentMisCitasBinding
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.repository.RepositorioCitas
import com.example.proyecfclientes.ui.adapters.CitasAdapter
import com.example.proyecfclientes.viewmodel.MisCitasViewModel
import androidx.navigation.fragment.findNavController

class MisCitasFragment : Fragment() {
    private var _binding: FragmentMisCitasBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MisCitasViewModel
    private lateinit var adapter: CitasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisCitasBinding.inflate(inflater, container, false)
        val repo = RepositorioCitas(ApiClient.retrofit)
        val factory = com.example.proyecfclientes.viewmodel.MisCitasViewModelFactory(requireActivity().application, repo)
        viewModel = ViewModelProvider(this, factory)[MisCitasViewModel::class.java]

        adapter = CitasAdapter(emptyList()) { cita ->
            cita.id?.let {
                val action = MisCitasFragmentDirections.actionMisCitasFragmentToChatFragment(it)
                findNavController().navigate(action)
            }
        }

        binding.recyclerViewCitas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCitas.adapter = adapter

        viewModel.citas.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista ?: emptyList())
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.cargarCitas()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
