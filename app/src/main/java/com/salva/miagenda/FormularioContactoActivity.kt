package com.salva.miagenda

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.salva.miagenda.data.AgendaDataBase
import com.salva.miagenda.data.Contacto
import com.salva.miagenda.databinding.ActivityFormularioContactoBinding
import kotlinx.coroutines.launch

class FormularioContactoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormularioContactoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormularioContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnGuardar.setOnClickListener {
            guardarContacto()
        }
    }

    private fun guardarContacto() {
        val nombre = binding.etNombre.text?.toString().orEmpty().trim()
        val telefono = binding.etTelefono.text?.toString().orEmpty().trim()
        val email = binding.etEmail.text?.toString().orEmpty().trim().ifEmpty { null }

        binding.tilNombre.error =
            if (nombre.isEmpty()) getString(R.string.error_campo_obligatorio) else null
        binding.tilTelefono.error =
            if (telefono.isEmpty()) getString(R.string.error_campo_obligatorio) else null
        if (nombre.isEmpty() || telefono.isEmpty()) return

        // Temporal: en la fase 5 el usuario saldrá de DataStore (el que ha iniciado sesión)
        val usuario = "salva"
        val contacto =
            Contacto(usuario = usuario, nombre = nombre, telefono = telefono, email = email)
        val dao = AgendaDataBase.obtenerBaseDeDatos(this).contactoDao()

        lifecycleScope.launch {
            try {
                dao.insertar(contacto)
                finish()
            } catch (e: Exception) {
                Snackbar.make(binding.root, R.string.error_guardar_contacto, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NOMBRE = "extra_nombre"
        const val EXTRA_TELEFONO = "extra_telefono"
        const val EXTRA_EMAIL = "extra_email"
    }
}