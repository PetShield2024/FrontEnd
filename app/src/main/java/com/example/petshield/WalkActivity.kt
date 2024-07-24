package com.example.petshield

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.petshield.databinding.ActivityWalkBinding
import com.google.android.material.progressindicator.CircularProgressIndicator

class WalkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkBinding
    private lateinit var circularProgressIndicator: CircularProgressIndicator
    private lateinit var timerText: TextView
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button

    private var isPaused = false
    private var elapsedTime = 0L // Time in milliseconds

    private val totalDuration = 3600000L // 1 hour in milliseconds

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (!isPaused) {
                elapsedTime += 1000 // Increase time by 1 second
                updateTimer()
                handler.postDelayed(this, 1000) // Schedule the next update in 1 second
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        circularProgressIndicator = binding.circularProgressIndicator
        timerText = binding.timerText
        pauseButton = binding.pauseButton
        stopButton = binding.stopButton

        pauseButton.setOnClickListener {
            isPaused = !isPaused
            if (isPaused) {
                pauseButton.text = "Continue"
            } else {
                pauseButton.text = "Pause"
                handler.postDelayed(runnable, 1000) // Start the timer
            }
        }

        stopButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            // Handle stop logic
            finish()
        }

        // Start the stopwatch
        handler.postDelayed(runnable, 1000)
    }

    private fun updateTimer() {
        val minutes = (elapsedTime / 1000) / 60
        val seconds = (elapsedTime / 1000) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)

        // Update progress indicator
        val progress = ((elapsedTime.toFloat() / totalDuration) * 100).toInt()
        circularProgressIndicator.progress = progress
    }
}
