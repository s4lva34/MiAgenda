package com.salva.miagenda

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.salva.miagenda.data.AgendaDataBase
import com.salva.miagenda.data.Contacto
import com.salva.miagenda.data.ContactoDao
import com.salva.miagenda.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: ContactoDao
    private val adapter = ContactoAdapter { contacto -> abrirFormularioParaEditar(contacto) }
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

        binding.rvContactos.layoutManager = LinearLayoutManager(this)
        binding.rvContactos.adapter = adapter

        // Temporal: en la fase 5 el usuario saldrá de DataStore (el que ha iniciado sesión)
        val usuario = "salva"
        dao = AgendaDataBase.obtenerBaseDeDatos(this).contactoDao()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    dao.obtenerContactosDeUsuario(usuario).collect { contactos ->
                        adapter.submitList(contactos)
                    }
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.root,
                        R.string.error_cargar_contactos,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
        configurarSwipeParaBorrar()
    }

    private fun configurarSwipeParaBorrar() {
        val swipe = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.bindingAdapterPosition
                val contacto = adapter.currentList[posicion]
                confirmarBorrado(contacto, posicion)
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(binding.rvContactos)
    }

    private fun confirmarBorrado(contacto: Contacto, posicion: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialogo_borrar_titulo)
            .setMessage(getString(R.string.dialogo_borrar_mensaje, contacto.nombre))
            .setNegativeButton(R.string.dialogo_cancelar) { _, _ ->
                adapter.notifyItemChanged(
                    posicion
                )
            }
            .setPositiveButton(R.string.dialogo_eliminar) { _, _ -> borrarContacto(contacto) }
            .setOnCancelListener { adapter.notifyItemChanged(posicion) }
            .show()
    }

    private fun borrarContacto(contacto: Contacto) {
        lifecycleScope.launch {
            try {
                dao.eliminar(contacto)
                Snackbar.make(binding.root, R.string.contacto_eliminado, Snackbar.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Snackbar.make(binding.root, R.string.error_eliminar_contacto, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun abrirFormularioParaEditar(contacto: Contacto) {
        val intent = Intent(this, FormularioContactoActivity::class.java)
        intent.putExtra(FormularioContactoActivity.EXTRA_ID, contacto.id)
        intent.putExtra(FormularioContactoActivity.EXTRA_NOMBRE, contacto.nombre)
        intent.putExtra(FormularioContactoActivity.EXTRA_TELEFONO, contacto.telefono)
        intent.putExtra(FormularioContactoActivity.EXTRA_EMAIL, contacto.email)
        startActivity(intent)
    }
}