package com.example.proyecfclientes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.databinding.ItemMensajeBinding

class MensajesAdapter(
    private var mensajes: List<Mensaje> = emptyList(),
    private val userIdActual: Int? = null // ID del usuario logueado para distinguir emisor/receptor
) : RecyclerView.Adapter<MensajesAdapter.MensajeViewHolder>() {

    inner class MensajeViewHolder(val binding: ItemMensajeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val binding = ItemMensajeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MensajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.binding.tvMensaje.text = mensaje.message
        // Si quieres cambiar color/fondo/alineación según remitente, puedes hacerlo aquí
        // Por ejemplo:
        // if (mensaje.sender_id == userIdActual) { ... }
    }

    override fun getItemCount(): Int = mensajes.size

    fun actualizarMensajes(nuevosMensajes: List<Mensaje>) {
        mensajes = nuevosMensajes
        notifyDataSetChanged()
    }
}
