package com.example.notasapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasapp.viewmodel.CrearNotaViewModel

class CrearNotaActivity : AppCompatActivity() {
    private val viewModel: CrearNotaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_nota)

        val etTitulo = findViewById<EditText>(R.id.etTituloNota)
        val etContenido = findViewById<EditText>(R.id.etContenidoNota)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarNota)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarCrear)

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val contenido = etContenido.text.toString()

            if (titulo.isNotEmpty() && contenido.isNotEmpty()) {
                viewModel.guardarNota(titulo, contenido)
            } else {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) ProgressBar.VISIBLE else ProgressBar.GONE
        }

        viewModel.exito.observe(this) {
            Toast.makeText(this, "¡Nota guardada con éxito!", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }
}