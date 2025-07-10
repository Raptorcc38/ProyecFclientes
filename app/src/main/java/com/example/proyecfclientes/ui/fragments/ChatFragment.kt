package com.example.proyecfclientes.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.proyecfclientes.databinding.FragmentChatBinding
import com.example.proyecfclientes.viewmodel.ChatViewModel
import com.example.proyecfclientes.ui.adapters.MensajesAdapter
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.network.ApiClient
import com.example.proyecfclientes.utils.Preferencias
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ChatFragmentArgs>()

    private lateinit var viewModel: ChatViewModel

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ChatViewModel::class.java]
        return binding.root
    }

    private fun mostrarDialogoCalificacion(appointmentId: Int) {
        val context = requireContext()
        val dialogView = LayoutInflater.from(context).inflate(com.example.proyecfclientes.R.layout.dialog_calificar_cita, null)
        val ratingBar = dialogView.findViewById<RatingBar>(com.example.proyecfclientes.R.id.ratingBar)
        val etComentario = dialogView.findViewById<EditText>(com.example.proyecfclientes.R.id.etComentario)

        AlertDialog.Builder(context)
            .setTitle("Calificar trabajo")
            .setView(dialogView)
            .setPositiveButton("Enviar") { _, _ ->
                val puntaje = ratingBar.rating
                val comentario = etComentario.text.toString()
                viewModel.calificarCita(appointmentId, puntaje, comentario)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appointmentId = args.appointmentId
        var receiverId: Int? = null
        var myUserId: Int? = null
        var citaActual: Cita? = null

        binding.btnEnviarMensaje.isEnabled = false
        // Obtener la cita para saber quién es el cliente y el trabajador
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val context = requireContext()
                val token = "Bearer ${Preferencias.getToken(context)}"
                val response = ApiClient.retrofit.obtenerCitaPorId(token, appointmentId)
                if (response.isSuccessful && response.body() != null) {
                    citaActual = response.body()
                    myUserId = Preferencias.getUserId(context)
                    // Mostrar foto y nombre del trabajador
                    val trabajador = citaActual?.worker
                    val nombre = trabajador?.user?.let { "${it.name ?: ""} ${it.last_name ?: ""}" } ?: ""
                    binding.tvNombreTrabajador.text = nombre
                    val foto = trabajador?.picture_url
                    Glide.with(this@ChatFragment)
                        .load(if (foto.isNullOrEmpty() || foto == "null") null else foto)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(binding.ivFotoTrabajador)
                    // Habilita el botón solo si ambos datos están listos
                    if (citaActual != null && myUserId != null) {
                        binding.btnEnviarMensaje.isEnabled = true
                    }
                }
            } catch (_: Exception) {}
        }

        // Adapter básico
        val adapter = MensajesAdapter(emptyList())
        binding.recyclerViewMensajes.adapter = adapter

        // Observar mensajes
        viewModel.mensajes.observe(viewLifecycleOwner) { mensajes ->
            adapter.actualizarMensajes(mensajes)
        }

        viewModel.cargarMensajesDeCita(appointmentId)

        // Actualización automática cada 30 segundos
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                viewModel.cargarMensajesDeCita(appointmentId)
                handler?.postDelayed(this, 30000)
            }
        }
        handler?.postDelayed(runnable!!, 30000)

        // Botón para enviar mensaje
        binding.btnEnviarMensaje.setOnClickListener {
            val texto = binding.etMensaje.text.toString().trim()
            if (texto.isNotEmpty() && citaActual != null && myUserId != null) {
                receiverId = if (myUserId == citaActual!!.user_id) citaActual!!.worker_id else citaActual!!.user_id
                if (receiverId != null) {
                    viewModel.enviarMensaje(appointmentId, texto, receiverId!!)
                    binding.etMensaje.setText("")
                } else {
                    Toast.makeText(requireContext(), "No se pudo determinar el destinatario", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observa envío de mensajes
        viewModel.mensajeEnviado.observe(viewLifecycleOwner) { enviado ->
            if (enviado) {
                viewModel.cargarMensajesDeCita(appointmentId)
            } else {
                Toast.makeText(requireContext(), "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para concretar cita (solo si la cita no está concretada)
        binding.btnConcretarCita?.setOnClickListener {
            val citaId = args.appointmentId
            val action = ChatFragmentDirections.actionChatFragmentToConcretarCitaFragment(citaId)
            findNavController().navigate(action)
        }

        // Mostrar popup de calificación si la cita está finalizada y no calificada
        viewModel.citaFinalizadaSinCalificar.observe(viewLifecycleOwner) { mostrar ->
            if (mostrar) {
                mostrarDialogoCalificacion(args.appointmentId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler?.removeCallbacksAndMessages(null)
        handler = null
        runnable = null
        _binding = null
    }
}
