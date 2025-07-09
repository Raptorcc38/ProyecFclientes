package com.example.proyecfclientes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.databinding.ItemMensajeBinding

class MensajesAdapter(
    private var mensajes: List<Mensaje>,
    private val miUsuarioId: Int // Para diferenciar mensajes enviados/recibidos
) : RecyclerView.Adapter<MensajesAdapter.MensajeViewHolder>() {

    inner class MensajeViewHolder(val binding: ItemMensajeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mensaje: Mensaje) {
            binding.tvMensaje.text = mensaje.message
            // Puedes personalizar el diseño según si el mensaje es enviado o recibido
            if (mensaje.sender_id == miUsuarioId) {
                binding.tvMensaje.textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_END
            } else {
                binding.tvMensaje.textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val binding = ItemMensajeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MensajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        holder.bind(mensajes[position])
    }

    override fun getItemCount(): Int = mensajes.size

    fun actualizarLista(nuevaLista: List<Mensaje>) {
        mensajes = nuevaLista
        notifyDataSetChanged()
    }
}

