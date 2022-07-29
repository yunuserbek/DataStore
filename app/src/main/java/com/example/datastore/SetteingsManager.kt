package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UiMode{
    LIGHT,DARK
}
class SettingsManager(context: Context) {
    private val Context.datastore:DataStore<Preferences> by preferencesDataStore("settings")
    private val  mDataStore:DataStore<Preferences> =context.datastore

    val uiModelFlow: Flow<UiMode> =mDataStore.data
        .catch {
            if (it is IOException){
                it.printStackTrace()
                emit(emptyPreferences())
            }else{
                throw it
            }
        }
        .map { preferences ->
            when(preferences[IS_DARK_MODE]?: false){
                true-> UiMode.DARK
                false -> UiMode.LIGHT

            }
        }
    suspend fun  setUİMode(uiMode: UiMode){
        mDataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = when(uiMode){
                UiMode.LIGHT->false
                UiMode.DARK->true

            }
        }
    }
    companion object{
        val IS_DARK_MODE = booleanPreferencesKey("dark_mode")
    }
}