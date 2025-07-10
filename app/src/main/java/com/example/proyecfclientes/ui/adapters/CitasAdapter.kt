package com.example.proyecfclientes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecfclientes.Data.modelo.Cita
import com.example.proyecfclientes.databinding.ItemCitaBinding

class CitasAdapter(
    private var citas: List<Cita>,
    private val onClick: (Cita) -> Unit
) : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    inner class CitaViewHolder(val binding: ItemCitaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = ItemCitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]
        val trabajador = cita.worker?.user?.let { "${it.name ?: ""} ${it.last_name ?: ""}" } ?: "Sin trabajador"
        val categoria = cita.category?.name ?: "Sin categoría"
        val estado = when {
            cita.status == 2 -> "Calificada"
            cita.status == 1 -> "Concretada"
            else -> "No concretada"
        }
        val fechaHora = if (cita.appointment_date != null && cita.appointment_time != null) {
            "${cita.appointment_date} ${cita.appointment_time}"
        } else {
            "Sin fecha"
        }
        holder.binding.tvTrabajador.text = trabajador
        holder.binding.tvCategoria.text = categoria
        holder.binding.tvEstado.text = estado
        holder.binding.tvFechaHora.text = fechaHora
        holder.binding.root.setOnClickListener { onClick(cita) }
    }

    override fun getItemCount(): Int = citas.size

    fun actualizarLista(nuevaLista: List<Cita>) {
        citas = nuevaLista
        notifyDataSetChanged()
    }
}

