package com.salva.miagenda

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.salva.miagenda.data.UserPreferences
import com.salva.miagenda.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPreferences = UserPreferences(this)
        comprobarSesionIniciada()

        binding.btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }
    }

    private fun comprobarSesionIniciada() {
        lifecycleScope.launch {
            val credenciales = userPreferences.credenciales.first()
            if (credenciales.sesionIniciada) abrirListaDeContactos()
        }
    }

    private fun iniciarSesion() {
        val usuario = binding.etUsuario.text?.toString().orEmpty().trim()
        val contrasena = binding.etContrasena.text?.toString().orEmpty()

        binding.tilUsuario.error =
            if (usuario.isEmpty()) getString(R.string.error_campo_obligatorio) else null
        binding.tilContrasena.error =
            if (contrasena.isEmpty()) getString(R.string.error_campo_obligatorio) else null
        if (usuario.isEmpty() || contrasena.isEmpty()) return

        lifecycleScope.launch {
            try {
                val credenciales = userPreferences.credenciales.first()
                when {
                    credenciales.usuario == null -> {
                        userPreferences.guardarCredenciales(usuario, contrasena)
                        entrar()
                    }

                    credenciales.usuario == usuario && credenciales.contrasena == contrasena -> entrar()
                    else -> Snackbar.make(
                        binding.root, R.string.error_credenciales_incorrectas, Snackbar.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.root, R.string.error_iniciar_sesion, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private suspend fun entrar() {
        userPreferences.iniciarSesion()
        abrirListaDeContactos()
    }

    private fun abrirListaDeContactos() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}