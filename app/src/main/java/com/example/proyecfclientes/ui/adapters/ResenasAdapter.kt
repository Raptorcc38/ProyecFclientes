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
        holder.binding.tvResenaUsuario.text = resena.user?.name ?: "Usuario"
        holder.binding.tvResenaComentario.text = resena.comment ?: ""
        holder.binding.tvResenaFecha.text = resena.created_at ?: ""
        holder.binding.tvResenaRating.text = " ${resena.rating ?: "-"}"
    }

    override fun getItemCount(): Int = resenas.size

    fun actualizarResenas(nuevasResenas: List<Resena>) {
        resenas = nuevasResenas
        notifyDataSetChanged()
    }
}
