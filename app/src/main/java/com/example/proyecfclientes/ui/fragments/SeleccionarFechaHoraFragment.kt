package com.example.proyecfclientes.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.proyecfclientes.databinding.FragmentSeleccionarFechaHoraBinding
import java.util.*

class SeleccionarFechaHoraFragment : Fragment() {

    private var _binding: FragmentSeleccionarFechaHoraBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<SeleccionarFechaHoraFragmentArgs>()

    private var fechaSeleccionada: String? = null
    private var horaSeleccionada: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeleccionarFechaHoraBinding.inflate(inflater, container, false)

        binding.btnFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    fechaSeleccionada = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                    binding.tvFecha.text = fechaSeleccionada
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(),
                { _, hour, minute ->
                    horaSeleccionada = "%02d:%02d".format(hour, minute)
                    binding.tvHora.text = horaSeleccionada
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.btnConcretar.setOnClickListener {
            if (fechaSeleccionada.isNullOrEmpty() || horaSeleccionada.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Selecciona fecha y hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            findNavController().navigate(
                SeleccionarFechaHoraFragmentDirections.actionSeleccionarFechaHoraFragmentToMisCitasFragment()
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
