package com.viettel.tvbox.services

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "search_prefs"
        private const val KEY_HISTORY = "search_history"
        private const val MAX_HISTORY = 9
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getSearchHistory(): List<String> {
        val json = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addSearchHistory(query: String) {
        val history = getSearchHistory().toMutableList()
        history.remove(query)
        history.add(0, query)
        if (history.size > MAX_HISTORY) {
            history.removeAt(history.size - 1)
        }
        saveHistory(history)
    }

    fun clearSearchHistory() {
        prefs.edit { remove(KEY_HISTORY) }
    }

    private fun saveHistory(history: List<String>) {
        val json = gson.toJson(history)
        prefs.edit { putString(KEY_HISTORY, json) }
    }
}
