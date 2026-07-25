package com.repeatly.app

object TagEngine {

    private val dictionary: Map<String, String> = mapOf(
        "اتخانقت" to "مشاكل",
        "خناقة" to "مشاكل",
        "اتخاصمت" to "مشاكل",
        "زعلت" to "مشاكل",
        "زعلان" to "مشاكل",
        "عصبت" to "مشاكل",
        "اتضايقت" to "مشاكل",

        "اتأخرت" to "تأخير",
        "تأخرت" to "تأخير",
        "مستني" to "تأخير",

        "كسلت" to "كسل",
        "كسلان" to "كسل",
        "مذاكرتش" to "كسل",
        "اجلت" to "كسل",

        "الشغل" to "شغل",
        "شغلي" to "شغل",
        "المكتب" to "شغل",
        "الشركة" to "شغل",

        "صاحبي" to "علاقات",
        "صاحبتي" to "علاقات",
        "أهلي" to "علاقات",
        "اهلي" to "علاقات",
        "خطيبتي" to "علاقات",
        "خطيبي" to "علاقات",

        "تعبان" to "تعب",
        "تعب" to "تعب",
        "مرهق" to "تعب",
        "مش قادر" to "تعب",

        "مبسوط" to "إيجابي",
        "فرحان" to "إيجابي",
        "انجزت" to "إيجابي",
        "خلصت" to "إيجابي",

        "قلق" to "قلق",
        "خايف" to "قلق",
        "متوتر" to "قلق"
    )

    fun extractTags(text: String): List<String> {
        val normalized = text.trim()
        val foundTags = LinkedHashSet<String>()

        val sortedKeywords = dictionary.keys.sortedByDescending { it.length }
        for (keyword in sortedKeywords) {
            if (normalized.contains(keyword)) {
                foundTags.add(dictionary.getValue(keyword))
            }
        }

        return foundTags.toList()
    }
}
