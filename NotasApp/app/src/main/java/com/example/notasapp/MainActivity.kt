package com.example.notasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notasapp.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observadores (MVVM)
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) ProgressBar.VISIBLE else ProgressBar.GONE
        }

        viewModel.loginResult.observe(this) { response ->
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()

            // GUARDAR TOKEN (Para las siguientes peticiones)
            val prefs = getSharedPreferences("NotasAppPrefs", MODE_PRIVATE)
            prefs.edit().putString("TOKEN", response.token).apply()

            // Navegar al Dashboard
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("NOMBRE_USUARIO", response.nombre) // Pasamos el nombre
            startActivity(intent)
            finish()
        }

        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }
}