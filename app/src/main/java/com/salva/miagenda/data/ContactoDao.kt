package com.salva.miagenda.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactoDao {

    @Insert
    suspend fun insertar(contacto: Contacto)

    @Update
    suspend fun actualizar(contacto: Contacto)

    @Delete
    suspend fun eliminar(contacto: Contacto)

    @Query("SELECT * FROM Contacto WHERE usuario = :usuario")
    fun obtenerContactosDeUsuario(usuario: String): Flow<List<Contacto>>
}