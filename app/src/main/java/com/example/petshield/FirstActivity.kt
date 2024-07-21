package com.example.petshield

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petshield.databinding.ActivityFirstBinding
import com.example.petshield.databinding.ActivityMainBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}