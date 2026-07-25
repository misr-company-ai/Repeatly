package com.repeatly.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etEntry: EditText
    private lateinit var tvEncouragement: TextView
    private lateinit var insightsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEntry = findViewById(R.id.etEntry)
        tvEncouragement = findViewById(R.id.tvEncouragement)
        insightsContainer = findViewById(R.id.insightsContainer)

        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveEntry()
        }

        requestNotificationPermissionIfNeeded()
        NotificationScheduler.scheduleHourlyReminder(this)

        refreshInsights()
    }

    override fun onResume() {
        super.onResume()
        refreshInsights()
    }

    private fun saveEntry() {
        val text = etEntry.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(this, "اكتب حاجة الأول", Toast.LENGTH_SHORT).show()
            return
        }

        val tags = TagEngine.extractTags(text)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dayFormat = SimpleDateFormat("EEEE", Locale.US)

        val entry = JournalEntry(
            text = text,
            tags = tags,
            dateIso = dateFormat.format(Date()),
            dayOfWeek = dayFormat.format(Date()),
            hour = calendar.get(Calendar.HOUR_OF_DAY)
        )

        JournalStorage.saveEntry(this, entry)
        etEntry.setText("")
        Toast.makeText(this, "تم الحفظ", Toast.LENGTH_SHORT).show()
        refreshInsights()
    }

    private fun refreshInsights() {
        val entries = JournalStorage.loadEntries(this)
        tvEncouragement.text = PatternAnalyzer.encouragementLine(entries)

        val insights = PatternAnalyzer.buildInsights(entries)
        insightsContainer.removeAllViews()

        if (insights.isEmpty()) {
            val emptyView = TextView(this)
            emptyView.text = "لسه مفيش أنماط كافية، كمل تكتب يوميًا"
            emptyView.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
            insightsContainer.addView(emptyView)
            return
        }

        for (insight in insights) {
            val itemView = TextView(this)
            itemView.text = insight
            itemView.setTextColor(ContextCompat.getColor(this, R.color.black))
            itemView.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg))
            itemView.setPadding(24, 24, 24, 24)
            itemView.gravity = Gravity.START

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 12
            itemView.layoutParams = params

            insightsContainer.addView(itemView)
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1010
                )
            }
        }
    }
}
