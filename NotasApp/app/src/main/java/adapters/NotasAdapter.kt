package com.example.notasapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.R
import com.example.notasapp.models.Note

class NotasAdapter(private var listaNotas: List<Note>) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvItemTitulo)
        val tvContenido: TextView = itemView.findViewById(R.id.tvItemContenido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = listaNotas[position]
        holder.tvTitulo.text = nota.title
        holder.tvContenido.text = nota.content
    }

    override fun getItemCount(): Int = listaNotas.size

    fun actualizarLista(nuevasNotas: List<Note>) {
        listaNotas = nuevasNotas
        notifyDataSetChanged()
    }
}