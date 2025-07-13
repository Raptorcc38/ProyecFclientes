package com.example.proyecfclientes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecfclientes.Data.modelo.Mensaje
import com.example.proyecfclientes.databinding.ItemMensajeBinding

class MensajesAdapter(
    private var mensajes: List<Mensaje> = emptyList(),
    private val userIdActual: Int? = null
) : RecyclerView.Adapter<MensajesAdapter.MensajeViewHolder>() {

    inner class MensajeViewHolder(val binding: ItemMensajeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val binding = ItemMensajeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MensajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.binding.tvMensaje.text = mensaje.message

        // Determinar si el mensaje es del cliente o del trabajador
        val esCliente = mensaje.sender_id == userIdActual
        val params = holder.binding.tvMensaje.layoutParams as FrameLayout.LayoutParams
        if (esCliente) {
            // Mensaje del cliente: izquierda, fondo verde claro
            params.gravity = android.view.Gravity.START
            holder.binding.tvMensaje.setBackgroundResource(com.example.proyecfclientes.R.color.mensaje_cliente)
        } else {
            // Mensaje del trabajador: derecha, fondo amarillo claro
            params.gravity = android.view.Gravity.END
            holder.binding.tvMensaje.setBackgroundResource(com.example.proyecfclientes.R.color.mensaje_trabajador)
        }
        holder.binding.tvMensaje.layoutParams = params
    }

    override fun getItemCount(): Int = mensajes.size

    fun actualizarMensajes(nuevosMensajes: List<Mensaje>) {
        mensajes = nuevosMensajes
        notifyDataSetChanged()
    }
}
