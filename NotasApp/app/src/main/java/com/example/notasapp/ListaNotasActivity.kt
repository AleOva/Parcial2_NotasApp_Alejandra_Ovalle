package com.example.notasapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.adapters.NotasAdapter
import com.example.notasapp.viewmodel.ListaNotasViewModel

class ListaNotasActivity : AppCompatActivity() {

    private val viewModel: ListaNotasViewModel by viewModels()
    private lateinit var adapter: NotasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas)

        val recyclerView = findViewById<RecyclerView>(R.id.rvNotas)

        // Configuramos el adapter
        adapter = NotasAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Pedir las notas al backend
        viewModel.cargarNotas()

        // Observar cuando lleguen los datos y pasarlos al adapter
        viewModel.notas.observe(this) { lista ->
            adapter.actualizarLista(lista)
        }

        viewModel.error.observe(this) { mensaje ->
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        }
    }
}