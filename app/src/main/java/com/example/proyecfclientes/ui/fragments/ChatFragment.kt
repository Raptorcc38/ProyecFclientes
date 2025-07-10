package com.example.proyecfclientes.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.proyecfclientes.databinding.FragmentChatBinding
import com.example.proyecfclientes.ui.adapters.MensajesAdapter
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val args by navArgs<ChatFragmentArgs>()
    private val apiService = ApiClient.retrofit
    private lateinit var adapter: MensajesAdapter
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    private var trabajadorNombre: String = ""
    private var trabajadorFoto: String? = null
    private var receiverId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        adapter = MensajesAdapter()
        binding.recyclerViewMensajes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMensajes.adapter = adapter

        cargarInfoCita()
        cargarMensajes()

        binding.btnEnviarMensaje.setOnClickListener {
            val mensaje = binding.etMensaje.text.toString().trim()
            if (mensaje.isNotEmpty() && receiverId != null) {
                // enviarMensaje(mensaje, receiverId!!) // (Pendiente solucionar chat)
                binding.etMensaje.setText("")
            } else {
                Toast.makeText(requireContext(), "No se pudo determinar el destinatario", Toast.LENGTH_SHORT).show()
            }
        }

        // DIÁLOGO DE CONFIRMACIÓN DE CITA
        binding.btnConcretarCita.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Concretar cita")
                .setMessage("¿Está seguro que desea concretar una cita?")
                .setPositiveButton("Sí") { _, _ ->
                    // Navega a SeleccionarUbicacionFragment (debes tener la acción creada)
                    val action = ChatFragmentDirections.actionChatFragmentToSeleccionarUbicacionFragment(args.appointmentId)
                    findNavController().navigate(action)
                }
                .setNegativeButton("No", null)
                .show()
        }

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                cargarMensajes()
                handler?.postDelayed(this, 30000)
            }
        }
        handler?.post(runnable!!)

        return binding.root
    }

    private fun cargarInfoCita() {
        lifecycleScope.launch {
            try {
                val token = Preferencias.getToken(requireContext())
                val response = withContext(Dispatchers.IO) {
                    apiService.obtenerCitaPorId("Bearer $token", args.appointmentId)
                }
                if (response.isSuccessful && response.body() != null) {
                    val cita = response.body()!!
                    trabajadorNombre = cita.worker?.user?.name ?: ""
                    trabajadorFoto = cita.worker?.picture_url
                    receiverId = cita.worker?.user_id

                    binding.tvNombreTrabajador.text = trabajadorNombre
                    Glide.with(requireContext())
                        .load(trabajadorFoto)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(binding.ivFotoTrabajador)
                } else {
                    binding.tvNombreTrabajador.text = "Trabajador"
                }
            } catch (e: Exception) {
                binding.tvNombreTrabajador.text = "Trabajador"
            }
        }
    }

    private fun cargarMensajes() {
        lifecycleScope.launch {
            try {
                val token = Preferencias.getToken(requireContext())
                val response = withContext(Dispatchers.IO) {
                    apiService.obtenerMensajesChat("Bearer $token", args.appointmentId)
                }
                if (response.isSuccessful && response.body() != null) {
                    val mensajes = response.body()!!
                    adapter.actualizarMensajes(mensajes)
                    binding.recyclerViewMensajes.scrollToPosition(adapter.itemCount - 1)
                }
            } catch (_: Exception) { }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler?.removeCallbacks(runnable!!)
    }
}
