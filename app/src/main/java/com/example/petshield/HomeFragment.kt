package com.example.petshield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private val imageResIds = intArrayOf(R.drawable.img_graph01, R.drawable.img_graph02, R.drawable.img_graph01)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val viewPager = view.findViewById<ViewPager2>(R.id.home_view_pager)
        viewPager.adapter = HomeVPAdapter(imageResIds)

        val tabLayout = view.findViewById<TabLayout>(R.id.home_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "몸무게 기록"
                1 -> "걸음 수 기록"
                else -> "이동거리 기록"
            }
        }.attach()

        return view
    }
}
