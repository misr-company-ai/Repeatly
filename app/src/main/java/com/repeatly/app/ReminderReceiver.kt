private fun createChannelIfNeeded(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val soundUri = android.net.Uri.parse(
            "android.resource://" + context.packageName + "/" + R.raw.notifications
        )

        val audioAttributes = android.media.AudioAttributes.Builder()
            .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Repeatly Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        // 👇 أهم سطر
        channel.setSound(soundUri, audioAttributes)

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
