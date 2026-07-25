package com.repeatly.app

import android.content.Context
import org.json.JSONArray

object JournalStorage {

    private const val PREFS_NAME = "repeatly_prefs"
    private const val KEY_ENTRIES = "journal_entries"

    fun saveEntry(context: Context, entry: JournalEntry) {
        val entries = loadEntries(context).toMutableList()
        entries.add(entry)
        val array = JSONArray()
        entries.forEach { array.put(it.toJson()) }

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ENTRIES, array.toString())
            .apply()
    }

    fun loadEntries(context: Context): List<JournalEntry> {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ENTRIES, null) ?: return emptyList()

        val array = JSONArray(raw)
        val result = mutableListOf<JournalEntry>()
        for (i in 0 until array.length()) {
            result.add(JournalEntry.fromJson(array.getJSONObject(i)))
        }
        return result
    }
}
