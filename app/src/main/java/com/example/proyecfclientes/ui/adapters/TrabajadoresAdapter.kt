package com.example.proyecfclientes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecfclientes.Data.modelo.Trabajador
import com.example.proyecfclientes.databinding.ItemTrabajadorBinding

class TrabajadoresAdapter(
    private var trabajadores: List<Trabajador>,
    private val onClick: (Trabajador) -> Unit
) : RecyclerView.Adapter<TrabajadoresAdapter.TrabajadorViewHolder>() {

    inner class TrabajadorViewHolder(private val binding: ItemTrabajadorBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(trabajador: Trabajador) {
            val nombre = trabajador.user?.name ?: ""
            val apellido = trabajador.user?.last_name ?: ""
            binding.tvNombre.text = "$nombre $apellido"
            binding.tvCalificacion.text = "Calificación: ${trabajador.average_rating ?: "Sin calificación"}"
            binding.tvTrabajos.text = "Trabajos realizados: ${trabajador.reviews_count ?: 0}"

            val foto = if (trabajador.picture_url == null || trabajador.picture_url == "null") null else trabajador.picture_url
            Glide.with(binding.root)
                .load(foto)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .into(binding.ivFoto)

            binding.root.setOnClickListener { onClick(trabajador) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrabajadorViewHolder {
        val binding = ItemTrabajadorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrabajadorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrabajadorViewHolder, position: Int) {
        holder.bind(trabajadores[position])
    }

    override fun getItemCount(): Int = trabajadores.size

    fun actualizarLista(nuevaLista: List<Trabajador>) {
        trabajadores = nuevaLista
        notifyDataSetChanged()
    }
}
