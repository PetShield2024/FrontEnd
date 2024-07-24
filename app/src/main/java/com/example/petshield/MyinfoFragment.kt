package com.example.petshield

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petshield.databinding.FragmentMyinfoBinding
import com.example.petshield.databinding.FragmentResultBinding

class MyinfoFragment : Fragment()  {

    private lateinit var binding: FragmentMyinfoBinding

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyinfoBinding.inflate(inflater, container, false)

        binding.myinfoEditIb.setOnClickListener {
            val intent = Intent(requireContext(), ModifyActivity::class.java)
            startActivity(intent)
        }
        return binding.root
}
}