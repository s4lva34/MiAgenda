package com.salva.miagenda.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contacto::class], version = 1, exportSchema = false)
abstract class AgendaDataBase : RoomDatabase() {

    abstract fun contactoDao(): ContactoDao

    companion object {
        @Volatile
        private var instancia: AgendaDataBase? = null

        fun obtenerBaseDeDatos(context: Context): AgendaDataBase {
            return instancia ?: synchronized(this) {
                val nueva = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaDataBase::class.java,
                    "agenda.db"
                ).build()
                instancia = nueva
                nueva
            }
        }
    }
}