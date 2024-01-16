package com.chiksmedina.medweather.weather.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Data store for saving and retrieving data on device.
class DataStoreRepository (
    private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("auth_prefs")
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
        val CITY = stringPreferencesKey("city")
    }

    suspend fun saveLocation(lat: Double, lon: Double) {
        context.dataStore.edit { preferences ->
            preferences[LATITUDE] = lat
            preferences[LONGITUDE] = lon
        }
    }

    suspend fun saveCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY] = city
        }
    }

    val latitude: Flow<Double> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[LATITUDE] ?: -12.0432 // Default Lima latitude
            }

    val longitude: Flow<Double> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[LONGITUDE] ?: -77.0282 // Default Lima longitude
            }

    val city: Flow<String> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[CITY] ?: "Lima"
            }

}
