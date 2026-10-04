package com.devesh.vedahostelwarden.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "veda_warden_prefs")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val LOGGED_IN_EMAIL = stringPreferencesKey("logged_in_email")
        private val ASSIGNED_HOSTEL_ID = stringPreferencesKey("assigned_hostel_id")
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val loggedInEmailFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LOGGED_IN_EMAIL]
    }

    val assignedHostelIdFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ASSIGNED_HOSTEL_ID]
    }

    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }.first()
    }

    suspend fun getLoggedInEmail(): String? {
        return context.dataStore.data.map { it[LOGGED_IN_EMAIL] }.first()
    }

    suspend fun getAssignedHostelId(): String? {
        return context.dataStore.data.map { it[ASSIGNED_HOSTEL_ID] }.first()
    }

    suspend fun saveUserSession(email: String, hostelId: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[LOGGED_IN_EMAIL] = email
            if (hostelId != null) {
                preferences[ASSIGNED_HOSTEL_ID] = hostelId
            }
        }
    }

    suspend fun updateAssignedHostelId(hostelId: String) {
        context.dataStore.edit { preferences ->
            preferences[ASSIGNED_HOSTEL_ID] = hostelId
        }
    }

    suspend fun clearUserSession() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[LOGGED_IN_EMAIL] = ""
            preferences[ASSIGNED_HOSTEL_ID] = ""
        }
    }
}
