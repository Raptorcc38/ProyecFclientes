package com.example.proyecfclientes.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.proyecfclientes.Data.requests.ConcretarCitaRequest
import com.example.proyecfclientes.databinding.FragmentConcretarCitaBinding
import com.example.proyecfclientes.utils.Preferencias
import com.example.proyecfclientes.viewmodel.MisCitasViewModel
import com.example.proyecfclientes.viewmodel.MisCitasViewModelFactory
import java.util.*


class ConcretarCitaFragment : Fragment() {

    private var _binding: FragmentConcretarCitaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MisCitasViewModel

    // Variables para fecha y hora seleccionadas
    private var fechaSeleccionada: String? = null
    private var horaSeleccionada: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConcretarCitaBinding.inflate(inflater, container, false)

        val repo = com.example.proyecfclientes.repository.RepositorioCitas(com.example.proyecfclientes.network.ApiClient.retrofit)
        val factory = MisCitasViewModelFactory(requireActivity().application, repo)
        viewModel = ViewModelProvider(this, factory)[MisCitasViewModel::class.java]

        // Selección de fecha
        binding.btnFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    fechaSeleccionada = "%04d-%02d-%02d".format(year, month+1, dayOfMonth)
                    binding.tvFecha.text = fechaSeleccionada
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Selección de hora
        binding.btnHora.setOnClickListener {
            val calendario = Calendar.getInstance()
            TimePickerDialog(requireContext(),
                { _, hour, minute ->
                    horaSeleccionada = "%02d:%02d".format(hour, minute)
                    binding.tvHora.text = horaSeleccionada
                },
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Botón para concretar cita
        binding.btnConcretarCita.setOnClickListener {
            // Mostrar diálogo de confirmación
            AlertDialog.Builder(requireContext())
                .setTitle("Concretar cita")
                .setMessage("¿Está seguro que desea concretar una cita?")
                .setPositiveButton("Sí") { _, _ ->
                    // Navega al fragmento de seleccionar ubicación
                    val action = ChatFragmentDirections.actionChatFragmentToSeleccionarUbicacionFragment(args.appointmentId)
                    findNavController().navigate(action)
                }
                .setNegativeButton("No", null)
                .show()
        }


        // Observa el resultado de la operación
        viewModel.resultadoConcretar.observe(viewLifecycleOwner) { response ->
            if (response != null && response.isSuccessful) {
                Toast.makeText(requireContext(), "Cita concretada correctamente", Toast.LENGTH_SHORT).show()
                try {
                    findNavController().navigate(com.example.proyecfclientes.R.id.misCitasFragment)
                } catch (e: Exception) {
                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(requireContext(), "No se pudo concretar la cita", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
