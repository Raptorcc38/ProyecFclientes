package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.proyecfclientes.Data.modelo.Trabajador
import com.example.proyecfclientes.databinding.FragmentPerfilTrabajadorBinding
import com.example.proyecfclientes.repository.RepositorioCitas
import kotlinx.coroutines.launch

class PerfilTrabajadorFragment : Fragment() {
    private var _binding: FragmentPerfilTrabajadorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilTrabajadorBinding.inflate(inflater, container, false)

        val trabajador = arguments?.getSerializable("trabajador") as? Trabajador

        trabajador?.let {
            val nombre = it.user?.name ?: ""
            val apellido = it.user?.last_name ?: ""
            binding.tvNombrePerfil.text = "$nombre $apellido"
            binding.tvCalificacionPerfil.text = "Calificación: ${it.average_rating ?: "Sin calificación"}"
            binding.tvTrabajosPerfil.text = "Trabajos realizados: ${it.reviews_count ?: 0}"

            val foto = if (it.picture_url == null || it.picture_url == "null") null else it.picture_url
            Glide.with(requireContext())
                .load(foto)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .into(binding.ivFotoPerfil)

            binding.btnContactar.setOnClickListener {
                // Crear cita y navegar al chat
                val categoriaId = trabajador.user?.type ?: 0 // Usa 0 si es nulo, o ajusta según tu lógica
                lifecycleScope.launch {
                    try {
                        val response = RepositorioCitas().crearCita(trabajador.id ?: 0, categoriaId, requireContext())
                        if (response.isSuccessful && response.body() != null && response.body()!!.id != null && response.body()!!.id != 0) {
                            val cita = response.body()!!
                            val citaId = cita.id!!
                            // Navegación segura al chat
                            val bundle = Bundle()
                            bundle.putInt("appointmentId", citaId)
                            findNavController().navigate(com.example.proyecfclientes.R.id.chatFragment, bundle)
                        } else {
                            Toast.makeText(requireContext(), "No se pudo crear la cita", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
