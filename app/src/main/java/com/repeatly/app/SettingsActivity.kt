package com.repeatly.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private val shareLink = "https://nearme.vercel.app/sharing"
    private val developerLink = "https://omarabdelazizbe.vercel.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<Button>(R.id.btnShareApp).setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
            startActivity(Intent.createChooser(sendIntent, null))
        }

        findViewById<Button>(R.id.btnDeveloper).setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(developerLink))
            startActivity(browserIntent)
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
