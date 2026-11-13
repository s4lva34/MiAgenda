package com.salva.miagenda.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

data class Credenciales(
    val usuario: String?,
    val contrasena: String?,
    val sesionIniciada: Boolean
)

class UserPreferences(private val context: Context) {

    companion object {
        private val CLAVE_USUARIO = stringPreferencesKey("usuario")
        private val CLAVE_CONTRASENA = stringPreferencesKey("contrasena")
        private val CLAVE_SESION_INICIADA = booleanPreferencesKey("sesion_iniciada")
    }

    val credenciales: Flow<Credenciales> = context.dataStore.data.map { preferencias ->
        Credenciales(
            usuario = preferencias[CLAVE_USUARIO],
            contrasena = preferencias[CLAVE_CONTRASENA],
            sesionIniciada = preferencias[CLAVE_SESION_INICIADA] ?: false
        )
    }

    suspend fun guardarCredenciales(usuario: String, contrasena: String) {
        context.dataStore.edit { preferencias ->
            preferencias[CLAVE_USUARIO] = usuario
            preferencias[CLAVE_CONTRASENA] = contrasena
        }
    }

    suspend fun iniciarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias[CLAVE_SESION_INICIADA] = true
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias[CLAVE_SESION_INICIADA] = false
        }
    }
}