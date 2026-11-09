package com.salva.miagenda

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.salva.miagenda.data.Contacto
import com.salva.miagenda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.fabAnadir.setOnClickListener {
            val intent = Intent(this, FormularioContactoActivity::class.java)
            startActivity(intent)
        }
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_cerrar_sesion -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        val adapter = ContactoAdapter()
        binding.rvContactos.layoutManager = LinearLayoutManager(this)
        binding.rvContactos.adapter = adapter

        // Datos de prueba
        val contactosPrueba = listOf(
            Contacto(1, "salva", "Ana García", "600123456", "ana@correo.com"),
            Contacto(2, "salva", "Luis Pérez", "611222333", null),
            Contacto(3, "salva", "Marta Ruiz", "622444555", "marta@correo.com")
        )
        adapter.submitList(contactosPrueba)
    }
}