package com.redeyesncode.crmfinancegs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityDashboardBinding
import com.redeyesncode.gsfinancenbfc.base.BaseActivity

class DashboardActivity : BaseActivity() {

    lateinit var binding:ActivityDashboardBinding

    private lateinit var navController: NavController
    private val backgrounds = listOf(
        R.drawable.background_bottom_blue_rounded,
        R.drawable.background_bottom_yellow_rounded,
        // Add more drawables as needed
    )
    private val handler = Handler(Looper.getMainLooper())

    private var currentBackgroundIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setupNavController()
        startBackgroundChangeLoop()
        setContentView(binding.root)

    }

    private fun startBackgroundChangeLoop() {
        val backgroundChangeRunnable = object : Runnable {
            override fun run() {
                // Change the background every 3 seconds
                binding.toolbar.setBackgroundResource(backgrounds[currentBackgroundIndex])
                currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.size

                // Repeat the process after 3 seconds
                handler.postDelayed(this, 600)
            }
        }

        // Start the initial delay
        handler.postDelayed(backgroundChangeRunnable, 3000)
    }

    override fun onDestroy() {
        // Remove any callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_dashboard) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

    }
}