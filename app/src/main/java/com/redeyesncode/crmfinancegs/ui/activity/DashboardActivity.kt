package com.redeyesncode.crmfinancegs.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityDashboardBinding
import com.redeyesncode.gsfinancenbfc.base.BaseActivity

class DashboardActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding:ActivityDashboardBinding

    private lateinit var navController: NavController
    private val backgrounds = listOf(
        R.drawable.background_bottom_blue_rounded,
        R.drawable.background_bottom_yellow_rounded,
        // Add more drawables as needed
    )
    private val handler = Handler(Looper.getMainLooper())

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.navPrivacyPolicy){
            val intentApplyLoan = Intent(this@DashboardActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","PRIVACY")
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId==R.id.navTerms){
            val intentApplyLoan = Intent(this@DashboardActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","http://gsfinance.in/term%20&%20Conditions.html")
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId==R.id.navLendingPartner){
            val intentApplyLoan = Intent(this@DashboardActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","https://gsfinanceservice.com/#partners")
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId==R.id.navFaq){
            val intentApplyLoan = Intent(this@DashboardActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","http://gsfinance.in/frequently%20asked.html")
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId==R.id.navAbout){
            val intentApplyLoan = Intent(this@DashboardActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","http://gsfinance.in/about.html")
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId==R.id.navHelp){
            val intentApplyLoan = Intent(this@DashboardActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","http://gsfinance.in/contact.html")
            startActivity(intentApplyLoan)
            return true
        } else{
            return false
        }
    }

    private var currentBackgroundIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setupNavController()
        startBackgroundChangeLoop()

        binding.navView.setNavigationItemSelectedListener(this)

        setupDashBanner()

        binding.ivOpenNav.setOnClickListener {

            binding.mainDrawer.openDrawer(GravityCompat.START)

        }

        binding.ivSearch.setOnClickListener {
            val intentSearch = Intent(this@DashboardActivity,LeadSearchActivity::class.java)
            startActivity(intentSearch)

        }



        setContentView(binding.root)

    }

    private fun setupDashBanner(){
        val sliderModels = arrayListOf<SlideModel>()
        val bankPartners = listOf(
           "https://redeyesncodemaster.s3.ap-south-1.amazonaws.com/banner1.jpeg",
           "https://redeyesncodemaster.s3.ap-south-1.amazonaws.com/banner2.jpeg",
           "https://redeyesncodemaster.s3.ap-south-1.amazonaws.com/banner3.jpeg",

        )
        for (images in bankPartners){

            sliderModels.add(SlideModel(images, ScaleTypes.FIT))

        }
        binding.imageSlider.setImageList(sliderModels, ScaleTypes.FIT)


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