package com.salva.miagenda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.salva.miagenda.data.Contacto
import com.salva.miagenda.databinding.ItemContactoBinding

class ContactoAdapter : ListAdapter<Contacto, ContactoAdapter.ContactoViewHolder>(ContactoDiffCallback) {
    class ContactoViewHolder(private val binding: ItemContactoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun mostrar(contacto: Contacto) {
            binding.tvNombre.text = contacto.nombre
            binding.tvTelefono.text = contacto.telefono
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val binding = ItemContactoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        holder.mostrar(getItem(position))
    }
}

object ContactoDiffCallback : DiffUtil.ItemCallback<Contacto>() {
    override fun areItemsTheSame(oldItem: Contacto, newItem: Contacto) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Contacto, newItem: Contacto) = oldItem == newItem
}