package com.example.proyecfclientes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecfclientes.Data.modelo.Resena
import com.example.proyecfclientes.databinding.ItemResenaBinding

class ResenasAdapter(private var resenas: List<Resena>) :
    RecyclerView.Adapter<ResenasAdapter.ResenaViewHolder>() {

    inner class ResenaViewHolder(val binding: ItemResenaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResenaViewHolder {
        val binding = ItemResenaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResenaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResenaViewHolder, position: Int) {
        val resena = resenas[position]
        val nombre = resena.user?.name ?: "Usuario"
        holder.binding.tvNombreUsuario.text = nombre
        holder.binding.tvComentario.text = resena.comment ?: ""
        holder.binding.tvPuntaje.text = "Puntaje: ${resena.rating ?: "-"}"
        holder.binding.tvFecha.text = resena.created_at ?: ""
    }

    override fun getItemCount(): Int = resenas.size

    fun actualizarResenas(nuevasResenas: List<Resena>) {
        resenas = nuevasResenas
        notifyDataSetChanged()
    }
}

