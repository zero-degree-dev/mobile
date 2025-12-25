package com.example.zero_degree.core.api

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    
    private const val PREFS_NAME = "zero_degree_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_USER_ID = "user_id"
    
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveToken(context: Context, token: String) {
        getSharedPreferences(context).edit()
            .putString(KEY_ACCESS_TOKEN, token)
            .apply()
    }
    
    fun getToken(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_ACCESS_TOKEN, null)
    }
    
    fun clearToken(context: Context) {
        getSharedPreferences(context).edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_USER_ID)
            .apply()
    }
    
    fun saveUserId(context: Context, userId: String) {
        getSharedPreferences(context).edit()
            .putString(KEY_USER_ID, userId)
            .apply()
    }
    
    fun getUserId(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_ID, null)
    }
    
    fun isLoggedIn(context: Context): Boolean {
        return getToken(context) != null
    }
}

