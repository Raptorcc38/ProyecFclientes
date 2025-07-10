package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecfclientes.databinding.FragmentPerfilTrabajadorBinding
import com.example.proyecfclientes.ui.adapters.ResenasAdapter
import com.example.proyecfclientes.viewmodel.PerfilTrabajadorViewModel
class PerfilTrabajadorFragment : Fragment() {

    private lateinit var binding: FragmentPerfilTrabajadorBinding
    private lateinit var viewModel: PerfilTrabajadorViewModel

    // Asume que tienes argumentos trabajador y categoriaId por SafeArgs
    private val args by navArgs<PerfilTrabajadorFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPerfilTrabajadorBinding.inflate(inflater, container, false)
        // Crear el repositorio y el factory para el ViewModel
        val repositorioCitas = com.example.proyecfclientes.repository.RepositorioCitas(com.example.proyecfclientes.network.ApiClient.retrofit)
        val factory = com.example.proyecfclientes.viewmodel.PerfilTrabajadorViewModelFactory(requireActivity().application, repositorioCitas)
        viewModel = ViewModelProvider(this, factory)[PerfilTrabajadorViewModel::class.java]

        // Botón de "Contactar"
        binding.btnContactar.setOnClickListener {
            // Soluciona el error de tipo: id nunca debe ser null
            val trabajadorId = args.trabajador.id ?: 0
            val categoriaId = args.categoriaId
            viewModel.crearCita(trabajadorId, categoriaId)
        }

        viewModel.citaCreada.observe(viewLifecycleOwner) { cita ->
            if (cita != null) {
                // Navega al chat pasando el id de la cita creada
                val action = PerfilTrabajadorFragmentDirections
                    .actionPerfilTrabajadorFragmentToChatFragment(cita.id ?: 0)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "No se pudo crear la cita", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar RecyclerView de reseñas
        val resenasAdapter = ResenasAdapter(emptyList())
        binding.recyclerViewResenas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewResenas.adapter = resenasAdapter

        // Cargar detalle del trabajador
        val trabajadorId = args.trabajador.id ?: 0
        viewModel.cargarDetalleTrabajador(trabajadorId)

        viewModel.detalleTrabajador.observe(viewLifecycleOwner) { detalle ->
            if (detalle != null) {
                val nombre = detalle.user?.name ?: ""
                val apellido = detalle.user?.last_name ?: ""
                binding.tvNombrePerfil.text = "$nombre $apellido"
                binding.tvCalificacionPerfil.text = "Calificación: ${detalle.average_rating ?: "Sin calificación"}"
                binding.tvTrabajosPerfil.text = "Trabajos realizados: ${detalle.reviews_count ?: 0}"
                val profesiones = detalle.categories?.joinToString(", ") { it.name } ?: "Sin profesiones"
                binding.tvProfesionesPerfil.text = "Profesiones: $profesiones"
                val foto = if (detalle.picture_url.isNullOrEmpty() || detalle.picture_url == "null") null else detalle.picture_url
                com.bumptech.glide.Glide.with(this)
                    .load(foto)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(binding.ivFotoPerfil)
                resenasAdapter.actualizarResenas(detalle.reviews ?: emptyList())
            }
        }

        return binding.root
    }
}
