package com.example.petshield

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.petshield.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private val imageResIds = intArrayOf(R.drawable.img_graph01, R.drawable.img_graph02, R.drawable.img_graph01)

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeVPAdapter: HomeVPAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up the ViewPager2 adapter
        binding.homeViewPager.adapter = HomeVPAdapter(imageResIds)

        // Set up TabLayout with ViewPager2
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "몸무게 기록"
                1 -> "걸음 수 기록"
                else -> "이동거리 기록"
            }
        }.attach()

        binding.homeWalkIb.setOnClickListener {
            // 화면 전환
            val intent = Intent(requireContext(), WalkActivity::class.java)

            startActivity(intent)
        }

        binding.homeHowBtn.setOnClickListener {
            // Create an AlertDialog
            AlertDialog.Builder(requireContext())
                .setTitle("심박수 측정 방법")
                .setMessage("아두이노 심박수 모듈을 이용해 반려견의 심박수를 측정합니다. \n 평균치는 80-120입니다.")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss() // Close the dialog when the "OK" button is clicked
                }
                .create()
                .show() // Show the dialog
        }



        return binding.root
    }
}
