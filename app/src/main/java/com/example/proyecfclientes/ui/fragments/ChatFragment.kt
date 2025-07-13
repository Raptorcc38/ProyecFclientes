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
            val receiverId = args.trabajadorId // Usar el id del trabajador pasado por argumentos
            if (mensaje.isNotEmpty() && receiverId != 0) {
                lifecycleScope.launch {
                    try {
                        val token = Preferencias.getToken(requireContext())
                        val request = com.example.proyecfclientes.Data.requests.MensajeRequest(
                            message = mensaje,
                            receiver_id = receiverId
                        )
                        val response = withContext(Dispatchers.IO) {
                            apiService.enviarMensajeChat("Bearer $token", args.appointmentId, request)
                        }
                        if (response.isSuccessful) {
                            binding.etMensaje.setText("")
                            cargarMensajes()
                        } else {
                            Toast.makeText(requireContext(), "No se pudo enviar el mensaje", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "No se pudo determinar el destinatario", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnConcretarCita.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Concretar cita")
                .setMessage("¿Está seguro que desea concretar una cita?")
                .setPositiveButton("Sí") { _, _ ->
                    val action = ChatFragmentDirections
                        .actionChatFragmentToSeleccionarUbicacionFragment(args.appointmentId)
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
                    var nombre = cita.worker?.user?.profile?.name ?: cita.worker?.user?.name ?: ""
                    var apellido = cita.worker?.user?.profile?.last_name ?: cita.worker?.user?.last_name ?: ""
                    // Si no hay nombre ni apellido en la API, usar el argumento de navegación
                    if (nombre.isBlank() && apellido.isBlank()) {
                        val nombreArgs = args.trabajadorNombre ?: ""
                        val partes = nombreArgs.trim().split(" ")
                        // Si hay más de una palabra, la primera es el nombre y el resto es el apellido
                        nombre = partes.firstOrNull() ?: ""
                        apellido = if (partes.size > 1) partes.subList(1, partes.size).joinToString(" ") else ""
                    }
                    // Mostrar ambos juntos
                    trabajadorNombre = listOf(nombre, apellido).filter { it.isNotBlank() }.joinToString(" ")
                    binding.tvNombreTrabajador.text = trabajadorNombre
                    trabajadorFoto = cita.worker?.picture_url ?: args.trabajadorFotoUrl
                    Glide.with(requireContext())
                        .load(trabajadorFoto)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .into(binding.ivFotoTrabajador)
                } else {
                    android.util.Log.d("ChatDebug", "No se recibió cita o worker")
                    binding.tvNombreTrabajador.text = "Trabajador"
                }
            } catch (e: Exception) {
                android.util.Log.e("ChatDebug", "Error al obtener cita: ", e)
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
