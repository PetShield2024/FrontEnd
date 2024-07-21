package com.example.petshield

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.petshield.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar
        // setSupportActionBar(binding.toolbar)
        // supportActionBar?.setDisplayShowTitleEnabled(false)

        // Find back and notification buttons
        val btnBack = binding.toolbar.findViewById<ImageButton>(R.id.btn_back)
        val btnNotifications = binding.toolbar.findViewById<ImageButton>(R.id.btn_alram)

        // Set up NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_frm) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomnavi.setupWithNavController(navController)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commit()
        }

        binding.bottomnavi.itemIconTintList = null
        binding.bottomnavi.background = null
        binding.bottomnavi.menu.getItem(2).isEnabled = false

        // 네비게이션 메뉴 아이템을 반복하여 선택 해제합니다.
        for (i in 0 until 5) {
            binding.bottomnavi.menu.getItem(i).isChecked = false
        }

        // 아무것도 선택되지 않은 상태로 설정합니다.
        binding.bottomnavi.selectedItemId = R.id.blank



        binding.bottomnavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mapFragment -> createFragmentWithBundle(MapFragment())
                R.id.foodFragment -> createFragmentWithBundle(FoodFragment())
                R.id.cameraFragment -> createFragmentWithBundle(CameraFragment())
                R.id.myinfoFragment -> createFragmentWithBundle(MyinfoFragment())
            }
            true
        }

        // Handle Toolbar buttons
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnNotifications.setOnClickListener {
            // Handle notifications button click
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
        }

        // NavController listener to manage back button visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isStartDestination = destination.id == navController.graph.startDestinationId
            btnBack.visibility = if (isStartDestination) View.GONE else View.VISIBLE
        }
        // Handle Floating Action Button click to navigate to HomeFragment
        binding.mainFloatingAddBtn.setOnClickListener {
            // 네비게이션 메뉴 아이템을 반복하여 선택 해제합니다.
            for (i in 0 until 5) {
                binding.bottomnavi.menu.getItem(i).isChecked = false
            }

            // 아무것도 선택되지 않은 상태로 설정합니다.
            binding.bottomnavi.selectedItemId = R.id.blank


            createFragmentWithBundle(HomeFragment())
        }
    }

    // Function to replace fragment
    private fun createFragmentWithBundle(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment)
            .commit()
    }
}
