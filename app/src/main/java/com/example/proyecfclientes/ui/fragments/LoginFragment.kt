package com.example.proyecfclientes.ui.fragments
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.proyecfclientes.databinding.FragmentLoginBinding
import com.example.proyecfclientes.viewmodel.LoginViewModel
import com.example.proyecfclientes.R
import kotlin.apply

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[LoginViewModel::class.java]

        binding.btnIniciarSesion.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvIrRegistro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }

        observarViewModel()
        return binding.root
    }

    private fun observarViewModel() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful && response.body() != null) {
                val token = response.body()?.access_token
                if (token != null) {

                    val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    prefs.edit().putString("access_token", token).apply()
                }

                findNavController().navigate(R.id.action_loginFragment_to_categoriasFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Email o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
