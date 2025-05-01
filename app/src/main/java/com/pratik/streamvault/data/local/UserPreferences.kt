package com.pratik.streamvault.data.local

import android.content.Context
import androidx.core.content.edit

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("streamvault_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "auth_token"
    }

    fun saveToken(token: String) {
        prefs.edit() { putString(TOKEN_KEY, token) }
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        prefs.edit() { clear() }
    }
}