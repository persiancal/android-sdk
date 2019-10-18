package com.github.persiancal.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        localEventsButton.setOnClickListener {
            startActivity(Intent(this, LocalActivity::class.java))
        }
        remoteEventsButton.setOnClickListener {
            startActivity(Intent(this, RemoteActivity::class.java))
        }
    }
}
