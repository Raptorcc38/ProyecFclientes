package com.example.proyecfclientes.utils

import android.content.Context

object Preferencias {

    private const val PREFS_NAME = "proyecfclientes_prefs"
    private const val TOKEN_KEY = "token"
    private const val USER_ID_KEY = "user_id"

    fun guardarToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN_KEY, null)
    }

    fun limpiarToken(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    fun guardarUserId(context: Context, userId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(USER_ID_KEY, userId).apply()
    }

    fun getUserId(context: Context): Int? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val id = prefs.getInt(USER_ID_KEY, -1)
        return if (id != -1) id else null
    }

    fun guardarTokenYUserId(context: Context, token: String, userId: Int) {
        guardarToken(context, token)
        guardarUserId(context, userId)
    }
}
