package com.repeatly.app

import org.json.JSONArray
import org.json.JSONObject

data class JournalEntry(
    val text: String,
    val tags: List<String>,
    val dateIso: String,
    val dayOfWeek: String,
    val hour: Int
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("text", text)
        obj.put("tags", JSONArray(tags))
        obj.put("date", dateIso)
        obj.put("day", dayOfWeek)
        obj.put("hour", hour)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): JournalEntry {
            val tagsArray = obj.getJSONArray("tags")
            val tags = mutableListOf<String>()
            for (i in 0 until tagsArray.length()) {
                tags.add(tagsArray.getString(i))
            }
            return JournalEntry(
                text = obj.getString("text"),
                tags = tags,
                dateIso = obj.getString("date"),
                dayOfWeek = obj.getString("day"),
                hour = obj.getInt("hour")
            )
        }
    }
}
