package com.example.petshield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petshield.databinding.FragmentMyfoodBinding
import com.example.petshield.databinding.FragmentResultBinding

class ResultFragment : Fragment()  {
    private lateinit var binding: FragmentResultBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)

        binding.resultNearMapIb.setOnClickListener {

            // Show MapFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, MapFragment())
                .addToBackStack(null) // Optional: Add to back stack to enable back navigation
                .commit()
        }

        binding.resultRetryIb.setOnClickListener {

            // Show CameraFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, CameraFragment())
                .addToBackStack(null) // Optional: Add to back stack to enable back navigation
                .commit()
        }
        return binding.root
    }
}