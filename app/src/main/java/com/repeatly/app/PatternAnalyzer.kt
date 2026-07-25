package com.repeatly.app

object PatternAnalyzer {

    fun buildInsights(entries: List<JournalEntry>): List<String> {
        if (entries.isEmpty()) return emptyList()

        val insights = mutableListOf<String>()

        val tagCounts = mutableMapOf<String, Int>()
        val tagDayCounts = mutableMapOf<String, MutableMap<String, Int>>()
        val coOccurrence = mutableMapOf<Pair<String, String>, Int>()

        for (entry in entries) {
            for (tag in entry.tags) {
                tagCounts[tag] = (tagCounts[tag] ?: 0) + 1

                val dayMap = tagDayCounts.getOrPut(tag) { mutableMapOf() }
                dayMap[entry.dayOfWeek] = (dayMap[entry.dayOfWeek] ?: 0) + 1
            }

            for (tagA in entry.tags) {
                for (tagB in entry.tags) {
                    if (tagA != tagB) {
                        val key = Pair(tagA, tagB)
                        coOccurrence[key] = (coOccurrence[key] ?: 0) + 1
                    }
                }
            }
        }

        for ((tag, count) in tagCounts) {
            if (count >= 3) {
                insights.add("ده حصل قبل كده $count مرات: $tag")
            }
        }

        for ((tag, dayMap) in tagDayCounts) {
            val total = tagCounts[tag] ?: 0
            if (total < 3) continue

            val topDay = dayMap.maxByOrNull { it.value }
            if (topDay != null && total > 0) {
                val ratio = topDay.value.toDouble() / total.toDouble()
                if (ratio >= 0.5 && topDay.value >= 2) {
                    insights.add("واضح إن ده بيتكرر كتير يوم ${arabicDay(topDay.key)}: $tag")
                }
            }
        }

        for ((pair, count) in coOccurrence) {
            val (tagA, tagB) = pair
            val totalA = tagCounts[tagA] ?: 0
            if (totalA < 3) continue

            val ratio = count.toDouble() / totalA.toDouble()
            if (ratio >= 0.6 && count >= 3) {
                insights.add("غالبًا $tagA بيحصل مع $tagB")
            }
        }

        return insights.distinct()
    }

    fun encouragementLine(entries: List<JournalEntry>): String {
        val recentNegative = entries.takeLast(5).count {
            it.tags.any { tag -> tag == "مشاكل" || tag == "تعب" || tag == "قلق" || tag == "كسل" }
        }

        return when {
            recentNegative >= 3 -> "مش كل مرة لازم يتكرر نفس الموضوع"
            recentNegative == 0 && entries.isNotEmpty() -> "إنت بتتحسن، كمل"
            else -> "خد بالك من نفسك النهارده"
        }
    }

    private fun arabicDay(englishDay: String): String {
        return when (englishDay) {
            "Sunday" -> "الأحد"
            "Monday" -> "الاثنين"
            "Tuesday" -> "الثلاثاء"
            "Wednesday" -> "الأربعاء"
            "Thursday" -> "الخميس"
            "Friday" -> "الجمعة"
            "Saturday" -> "السبت"
            else -> englishDay
        }
    }
}
