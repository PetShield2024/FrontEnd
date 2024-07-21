package com.example.petshield

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petshield.databinding.ActivityWalkBinding

class WalkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}