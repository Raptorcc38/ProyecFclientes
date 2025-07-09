package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecfclientes.databinding.FragmentChatBinding
import com.example.proyecfclientes.ui.adapters.MensajesAdapter
import com.example.proyecfclientes.viewmodel.ChatViewModel
import com.example.proyecfclientes.utils.Preferencias

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: MensajesAdapter
    private var appointmentId: Int = 0
    private var miUsuarioId: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            viewModel.cargarMensajes(appointmentId)
            handler.postDelayed(this, 30000) // 30 segundos
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[ChatViewModel::class.java]

        appointmentId = arguments?.getInt("appointmentId") ?: 0
        miUsuarioId = Preferencias.obtenerToken(requireContext())?.let { /* decodifica tu id si lo tienes */ 0 } ?: 0

        adapter = MensajesAdapter(emptyList(), miUsuarioId)
        binding.recyclerViewMensajes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMensajes.adapter = adapter

        binding.btnEnviar.setOnClickListener {
            val mensaje = binding.etMensaje.text.toString().trim()
            if (mensaje.isNotEmpty()) {
                viewModel.enviarMensaje(appointmentId, mensaje)
                binding.etMensaje.setText("")
            }
        }

        binding.btnConcretarCita.setOnClickListener {
            // Aquí va la lógica para concretar la cita (abrir mapa, fecha, hora, etc.)
            Toast.makeText(requireContext(), "Funcionalidad de concretar cita pendiente", Toast.LENGTH_SHORT).show()
        }

        observarViewModel()
        viewModel.cargarMensajes(appointmentId)
        handler.post(refreshRunnable)

        return binding.root
    }

    private fun observarViewModel() {
        viewModel.mensajes.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful && response.body() != null) {
                adapter.actualizarLista(response.body()!!)
                binding.recyclerViewMensajes.scrollToPosition(adapter.itemCount - 1)
            } else {
                Toast.makeText(requireContext(), "Error al obtener mensajes", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.envioExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito == true) {
                viewModel.cargarMensajes(appointmentId)
            } else if (exito == false) {
                Toast.makeText(requireContext(), "No se pudo enviar el mensaje", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(refreshRunnable)
        _binding = null
    }
}

