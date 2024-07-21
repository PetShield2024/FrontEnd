package com.example.petshield

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petshield.databinding.ActivityModifyBinding

class ModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}