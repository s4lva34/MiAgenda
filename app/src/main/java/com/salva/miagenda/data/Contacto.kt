package com.salva.miagenda.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contacto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuario: String,
    val nombre: String,
    val telefono: String,
    val email: String?
)


