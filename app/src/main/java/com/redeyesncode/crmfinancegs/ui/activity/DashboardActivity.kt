package com.redeyesncode.crmfinancegs.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
import com.google.android.material.textfield.TextInputLayout
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityDashboardBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class DashboardActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding:ActivityDashboardBinding

    private lateinit var navController: NavController

    @Inject
    lateinit var mainViewModel: MainViewModel



    private val backgrounds = listOf(
        R.drawable.background_bottom_blue_rounded,
        R.drawable.background_bottom_yellow_rounded,
        // Add more drawables as needed
    )
    private val handler = Handler(Looper.getMainLooper())
    fun createUpdatePasswordTextInputLayout(context: Context): TextInputLayout {
        // Create TextInputLayout
        val textInputLayout = TextInputLayout(context)

        // Set margin for TextInputLayout
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val marginInPixels = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._15sdp)
        layoutParams.setMargins(marginInPixels, 0, marginInPixels, 0)
        textInputLayout.layoutParams = layoutParams

        // Create EditText for password
        val editTextPassword = EditText(context)
        editTextPassword.hint = "New mPass (5 Digit)"
        editTextPassword.inputType = InputType.TYPE_CLASS_NUMBER
        editTextPassword.maxEms = 5 // Set maximum length to 5 characters

        // You can customize the appearance or add more configurations as needed
        editTextPassword.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        editTextPassword.setHintTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))

        // Add EditText to TextInputLayout
        textInputLayout.addView(editTextPassword)

        // Set up other configurations for TextInputLayout
        textInputLayout.isPasswordVisibilityToggleEnabled = true

        return textInputLayout
    }    fun showUpdatePasswordDialog(context: Context) {
        // Create TextInputLayout
        val textInputLayout = createUpdatePasswordTextInputLayout(context)

        // Create AlertDialog
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Update MPass")
        alertDialogBuilder.setView(textInputLayout)

        // Add positive and negative buttons as needed
        alertDialogBuilder.setPositiveButton("Update") { dialog, which ->
            // Handle the password update logic here
            val newPassword = textInputLayout.editText?.text.toString()
            // Perform necessary actions with the new password
            if(newPassword.isEmpty()){
                showToast("Please enter password")
            }else if(newPassword.length<5){
                showToast("Min Length of password is 5.")
            }else{
                val user = AppSession(this@DashboardActivity).getObject(
                    Constant.USER_LOGIN,
                    LoginUserResponse::class.java) as LoginUserResponse
                val updateMpassMap = hashMapOf<String,String>()
                updateMpassMap.put("updatedMPass",newPassword)
                updateMpassMap.put("userId",user.data?.userId.toString())
                mainViewModel.updateMpass(updateMpassMap)

            }

        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // Handle cancellation or any other action
        }

        // Show the dialog
        alertDialogBuilder.show()
    }
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
        } else if(item.itemId==R.id.updateMPass){
            showUpdatePasswordDialog(this@DashboardActivity)
            return true
        }else if(item.itemId==R.id.eMandateRegister){
            val intentApplyLoan = Intent(this@DashboardActivity, EMandateCRMActivity::class.java)
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId==R.id.createCollection){
            val intentApplyLoan = Intent(this@DashboardActivity, CollectionActivity::class.java)
            startActivity(intentApplyLoan)
            return true
        }

        else{
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
        (application as AndroidApp).getDaggerComponent().injectDashboardActivity(this@DashboardActivity)

        attachObservers()
        binding.ivOpenNav.setOnClickListener {

            binding.mainDrawer.openDrawer(GravityCompat.START)

        }

        binding.ivSearch.setOnClickListener {
            // apply checks for leads and visit filter.
            if(navController.currentDestination?.id==R.id.leadsFragment || navController.currentDestination?.id==R.id.approveFragment){
                val intentSearch = Intent(this@DashboardActivity,LeadSearchActivity::class.java)
                startActivity(intentSearch)

            }else if(navController.currentDestination?.id==R.id.visitFragment){

                val intentSearch = Intent(this@DashboardActivity,VisitSearchActivity::class.java)
                startActivity(intentSearch)
            }

        }
        logEmpBase("DASHBOARD_ACTIVITY")



        setContentView(binding.root)

    }

    private fun attachObservers() {
        mainViewModel.responseUpdateMpass.observe(this,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                showToast(it.message.toString())
                val loginIntent = Intent(this@DashboardActivity,LoginActivity::class.java)
                loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(loginIntent)

            },
            onError = {
                hideLoadingDialog()
            }

        ))


    }

    private fun setupDashBanner(){
        val sliderModels = arrayListOf<SlideModel>()
        val bankPartners = listOf(
           "https://androidbucket3577.s3.ap-south-1.amazonaws.com/banner1.jpg",
           "https://androidbucket3577.s3.ap-south-1.amazonaws.com/banner2.jpg",
           "https://androidbucket3577.s3.ap-south-1.amazonaws.com/banner3.jpg",
           "https://androidbucket3577.s3.ap-south-1.amazonaws.com/banner4.jpg",
           "https://androidbucket3577.s3.ap-south-1.amazonaws.com/banner5.jpeg",

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
         navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

    }
}