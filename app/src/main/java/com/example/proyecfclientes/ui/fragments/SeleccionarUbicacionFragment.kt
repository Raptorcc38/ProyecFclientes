package com.example.proyecfclientes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.proyecfclientes.databinding.FragmentSeleccionarUbicacionBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class SeleccionarUbicacionFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSeleccionarUbicacionBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    private val args by navArgs<SeleccionarUbicacionFragmentArgs>()

    private var latitudSeleccionada: Double? = null
    private var longitudSeleccionada: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeleccionarUbicacionBinding.inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.btnConfirmarUbicacion.setOnClickListener {
            if (latitudSeleccionada != null && longitudSeleccionada != null) {

                val action = SeleccionarUbicacionFragmentDirections
                    .actionSeleccionarUbicacionFragmentToSeleccionarFechaHoraFragment(
                        appointmentId = args.appointmentId,
                        latitud = latitudSeleccionada!!.toString(),
                        longitud = longitudSeleccionada!!.toString()
                    )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val santaCruz = LatLng(-17.783331, -63.182131)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruz, 13f))
        // Ya no agregamos ningún marcador dinámico aquí, solo usamos el pin fijo del layout
        // El centro del mapa será la ubicación seleccionada
        googleMap?.setOnCameraIdleListener {
            val center = googleMap?.cameraPosition?.target
            if (center != null) {
                latitudSeleccionada = center.latitude
                longitudSeleccionada = center.longitude
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}
