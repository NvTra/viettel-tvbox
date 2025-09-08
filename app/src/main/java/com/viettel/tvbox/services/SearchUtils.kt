package com.viettel.tvbox.services

import android.content.Context

object SearchUtils {
    fun getHistoryList(context: Context): List<String> {
        return SearchPreferences(context).getSearchHistory()
    }

    fun addHistory(context: Context, query: String) {
        SearchPreferences(context).addSearchHistory(query)
    }

    fun clearHistory(context: Context) {
        SearchPreferences(context).clearSearchHistory()
    }
}

