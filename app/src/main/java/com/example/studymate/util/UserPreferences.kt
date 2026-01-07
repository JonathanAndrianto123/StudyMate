package com.example.studymate.util

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "study_mate_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_USER_ID = "current_user_id"
        private const val KEY_USER_EMAIL = "current_user_email"
    }

    fun saveCurrentUser(userId: Int, email: String) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            apply()
        }
    }

    fun getCurrentUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun getCurrentUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun isUserLoggedIn(): Boolean {
        return getCurrentUserId() != -1
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
