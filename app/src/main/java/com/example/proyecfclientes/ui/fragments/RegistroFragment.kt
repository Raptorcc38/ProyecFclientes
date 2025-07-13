package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.proyecfclientes.databinding.FragmentRegistroBinding
import com.example.proyecfclientes.viewmodel.RegistroViewModel
import com.example.proyecfclientes.R

class RegistroFragment : Fragment() {

    private lateinit var binding: FragmentRegistroBinding
    private lateinit var viewModel: RegistroViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegistroBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[RegistroViewModel::class.java]

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.registrar(nombre, apellido, email, password)
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvIrLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
        }

        observarViewModel()
        return binding.root
    }

    private fun observarViewModel() {
        viewModel.registroExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito == true) {
                Toast.makeText(requireContext(), "Registro exitoso. Inicia sesión.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
            } else if (exito == false) {
                Toast.makeText(requireContext(), "El registro falló. Verifica tus datos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
