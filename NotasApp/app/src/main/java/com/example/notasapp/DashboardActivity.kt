package com.example.notasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnCrear = findViewById<Button>(R.id.btnIrCrearNota)
        val btnLista = findViewById<Button>(R.id.btnIrListaNotas)

        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO") ?: "Usuario"
        tvBienvenida.text = "Bienvenido, $nombreUsuario"

        btnCrear.setOnClickListener {
            // Usamos this@DashboardActivity para evitar el error en rojo
            startActivity(Intent(this@DashboardActivity, CrearNotaActivity::class.java))
        }

        btnLista.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, ListaNotasActivity::class.java))
        }
    }
}