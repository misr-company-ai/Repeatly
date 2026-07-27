package com.repeatly.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "repeatly_reminders"
        private const val NOTIFICATION_ID = 2001

        private val promptMessages = listOf(
            "احكيلي حصل معاك إيه النهارده؟",
            "خد بالك من نفسك النهارده"
        )
    }

    override fun onReceive(context: Context, intent: Intent?) {
        createChannelIfNeeded(context)

        val entries = JournalStorage.loadEntries(context)
        val insights = PatternAnalyzer.buildInsights(entries)

        val message = if (insights.isNotEmpty() && entries.size % 3 == 0) {
            insights.random()
        } else {
            promptMessages.random()
        }

        val openIntent = Intent(context, MainActivity::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(context, 3001, openIntent, flags)

        // 🔥 صوت الإشعار
        val soundUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notifications)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_repeatly)
            .setContentTitle("Repeatly")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(soundUri) // 🔥 إضافة الصوت هنا
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun createChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 🔥 صوت الإشعار
            val soundUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notifications)

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Repeatly Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.setSound(soundUri, audioAttributes) // 🔥 مهم جدًا

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
