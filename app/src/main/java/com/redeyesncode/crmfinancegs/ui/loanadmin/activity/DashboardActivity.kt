package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView
import com.redeyesncode.crmfinancegs.data.DashboardResponse
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityDashboardLoanAdminBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.crmfinancegs.ui.loanadmin.adapter.BankPartnerPager
import com.redeyesncode.crmfinancegs.ui.loanadmin.adapter.LoanPackageAdapter
import com.redeyesncode.crmfinancegs.ui.loanadmin.adapter.XmlPagerAdapter
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.navApplyLoan){
            val intentApplyLoan = Intent(this@DashboardActivity, ApplyLoanActivity::class.java)
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId==R.id.navPrivacyPolicy){
            val intentApplyLoan = Intent(this@DashboardActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","PRIVACY")
            startActivity(intentApplyLoan)
            return true
        }else if(item.itemId== R.id.navTerms){
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
            val intentApplyLoan = Intent(this@DashboardActivity, AboutActivity::class.java)
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
    private fun setupViewPagerServices() {

        val xmlFiles = listOf(R.layout.layout_intro_card, R.layout.layout_intro_card,R.layout.layout_intro_card,R.layout.layout_intro_card) // Add your XML files to the list
        val xmlPagerAdapter = XmlPagerAdapter(xmlFiles)

        val bankPartners = listOf("https://gsfinanceservice.com/img/logo/1.png"
            ,"https://gsfinanceservice.com/img/logo/2.png",
            "https://gsfinanceservice.com/img/logo/3.png",
            "https://gsfinanceservice.com/img/logo/4.png",
            "https://gsfinanceservice.com/img/logo/5.png",
            "https://gsfinanceservice.com/img/logo/6.png",
            "https://gsfinanceservice.com/img/logo/7.png",
            "https://gsfinanceservice.com/img/logo/8.png",
            "https://gsfinanceservice.com/img/logo/9.png",
            "https://gsfinanceservice.com/img/logo/10.png",
            "https://gsfinanceservice.com/img/logo/11.png",
            "https://gsfinanceservice.com/img/logo/12.png",
            "https://gsfinanceservice.com/img/logo/13.png",
            "https://gsfinanceservice.com/img/logo/14.png",
            "https://gsfinanceservice.com/img/logo/15.png",
            )
        val bankPartnerAdapter = BankPartnerPager(bankPartners)
        binding.viewPagerBanks.adapter = bankPartnerAdapter

        binding.dotsIndicatorBanks.attachTo(binding.viewPagerBanks)


        binding.viewPager.adapter = xmlPagerAdapter


        binding.dotsIndicator.attachTo(binding.viewPager)

    }

    lateinit var binding:ActivityDashboardLoanAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardLoanAdminBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectDashboardActivityLOANADMIN(this@DashboardActivity)


        attachObservers()
        initialApiCall()
        initClicks()
        setupViewPagerServices()
        binding.navView.setNavigationItemSelectedListener(this)
        startAutomaticScrolling()
        setContentView(binding.root)
    }

    private fun initialApiCall() {
        val user = AppSession(this@DashboardActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse
        showSnackbar("USER_ID ${user.user?.id.toString()}")
        val dashboardMap = hashMapOf<String,String>()
        dashboardMap.put("id",user.user?.id.toString())
        mainViewModel.getDashboardResponse(dashboardMap)

    }

    private fun attachObservers() {
        mainViewModel.dashboardResponse.observe(this,Event.EventObserver(

            onLoading = {
                        showLoadingDialog()

            },
            onSuccess = {
                        hideLoadingDialog()
                if(it.status==true){
                    //setup recycler view
                    binding.recvPackage.adapter = LoanPackageAdapter(this@DashboardActivity,it.packages,false)
                    binding.recvPackage.layoutManager = LinearLayoutManager(this@DashboardActivity,
                        LinearLayoutManager.VERTICAL,false)
                    //setup image slider
                    val sliderModels = arrayListOf<SlideModel>()
                    for (images in it.banner){

                        sliderModels.add(SlideModel(images.image, ScaleTypes.FIT))

                    }
                    binding.imageSlider.setImageList(sliderModels, ScaleTypes.FIT)
                    AppSession(this@DashboardActivity).putObject(Constant.USER_DASHBOARD,it)

                    dashboardBusinessLogic(it)
                    AppSession(this@DashboardActivity).putString(Constant.USER_LOAN_ID,it.loanInfo?.loanId.toString())





                }

            },
            onError = {
                hideLoadingDialog()
                showToast(it)

            }


        ))

        mainViewModel.assignRegularPackageResponse.observe(this, Event.EventObserver(
            onLoading = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()

                try {
                    if(it.userPackage?.package_data?.isNotEmpty() == true){
                        AppSession(this@DashboardActivity).putString(Constant.REGULAR_PACKAGE_ID,it.userPackage?.package_data?.get(0)?.id.toString())
                        val pendingAppSession = Intent(this@DashboardActivity,
                            PackageAssignActivity::class.java)
                        pendingAppSession.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(pendingAppSession)
                    }


                }catch (e:Exception){
                    e.printStackTrace()
                }

            },
            onError = {
                hideLoadingDialog()

            }


        ))
        mainViewModel.assignCustomPackageResponse.observe(this,Event.EventObserver(
            onLoading = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()



                try {
                    if(it.userPackage?.packagesData?.isNotEmpty() == true){
                        AppSession(this@DashboardActivity).putString(Constant.REGULAR_PACKAGE_ID,it.userPackage?.packagesData?.get(0)?.id.toString())
                        val pendingAppSession = Intent(this@DashboardActivity,
                            PackageAssignActivity::class.java)
                        pendingAppSession.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(pendingAppSession)
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }
            },
            onError = {
                hideLoadingDialog()
            }


        ))



    }

    private fun dashboardBusinessLogic(it: DashboardResponse) {


        // SAVING ENTIRE DASHBOARD RESPONSE IN SESSION





                            if(it.kyc.equals("Approved")){
                        binding.layoutDashboardKyc.mainLayout.visibility = View.GONE


                        if(it.loanInfo?.loanId!=0 && it.loanInfo?.currentStatus.equals("Closed")){
                            showCustomDialog("Info","Your Loan Status is closed !")

                            // hit assign package api.
                            checkNewPackageAfterLoanClosed()



                        }else if(it.loanInfo?.loanId!=0 && it.loanInfo?.currentStatus.equals("Underprocess")){

                            AppSession(this@DashboardActivity).putString(Constant.USER_LOAN_ID,it.loanInfo?.loanId.toString())
                            val intentLoanEmi = Intent(this@DashboardActivity, LoanEmiActivity::class.java)
                            intentLoanEmi.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intentLoanEmi)
                            AppSession(this@DashboardActivity).putString(Constant.CUSTOM_PACKAGE_ID,it.assignedCustom.toString())
                            AppSession(this@DashboardActivity).putString(Constant.REGULAR_PACKAGE_ID,it.assignedPackage.toString())


                        }else if(it.loanInfo?.loanId!=0 && it.loanInfo?.currentStatus.equals("Pending") && it.loanInfo?.enach.equals("success")){
                            val intentApproved = Intent(this@DashboardActivity, ApproveActivity::class.java)
                            intentApproved.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            intentApproved.putExtra("STATUS","Your Loan has been approved ! It will be disbursed shortly")
                            startActivity(intentApproved)

                        } else{
                            if(it.assignedPackage!=0 || it.assignedCustom!=0){
                                if(it.assignedCustom!=0){
                                    AppSession(this@DashboardActivity).putString(Constant.CUSTOM_PACKAGE_ID,it.assignedCustom.toString())
                                    val pendingAppSession = Intent(this@DashboardActivity,
                                        PackageAssignActivity::class.java)
                                    pendingAppSession.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(pendingAppSession)
                                }else if(it.assignedPackage!=0){
                                    AppSession(this@DashboardActivity).putString(Constant.REGULAR_PACKAGE_ID,it.assignedPackage.toString())
                                    val pendingAppSession = Intent(this@DashboardActivity,
                                        PackageAssignActivity::class.java)
                                    pendingAppSession.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(pendingAppSession)
                                }
                            }else{

                                val pendingAppSession = Intent(this@DashboardActivity,
                                    PendingActivity::class.java)
                                pendingAppSession.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(pendingAppSession)

                            }
                        }
                        //Check for loanId
                        try {
                            AppSession(this@DashboardActivity).putString(Constant.USER_LOAN_ID,it.loanInfo?.loanId.toString())

                        }catch (e:Exception){
                            AppSession(this@DashboardActivity).putString(Constant.USER_LOAN_ID,"")

                            e.printStackTrace()
                        }
                    }else if(it.kyc.equals("Rejected")) {
                        binding.layoutDashboardKyc.tvKycStatus.setText("Your KYC Request was Rejected")
                        binding.layoutDashboardKyc.mainLayout.visibility = View.VISIBLE
                        // Rejected Activity

                        val rejectActivity = Intent(this@DashboardActivity, RejectActivity::class.java)
                        rejectActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(rejectActivity)

                    }else if(it.kyc.equals("Pending")){
                        binding.layoutDashboardKyc.tvKycStatus.setText("Your KYC Request is Pending")
                        binding.layoutDashboardKyc.mainLayout.visibility = View.VISIBLE
                        val pendingAppSession = Intent(this@DashboardActivity, PendingActivity::class.java)
                        pendingAppSession.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(pendingAppSession)
                    }


    }

    private fun checkNewPackageAfterLoanClosed() {
        val user = AppSession(this@DashboardActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        val regularPackageMap = hashMapOf<String,String>()
        regularPackageMap.put("user_id",user.user?.id.toString())

        mainViewModel.assignRegularPackage(regularPackageMap)
        mainViewModel.assignCustomPackage(regularPackageMap)





    }

    private fun initClicks() {
        binding.ivNavDrawer.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)

        }
        binding.layoutDashboardKyc.mainLayout.setOnClickListener {
            val intentApplyLoan = Intent(this@DashboardActivity, ApplyLoanActivity::class.java)
            startActivity(intentApplyLoan)
        }
        binding.layoutDashboardKyc.btnApplyKyc.setOnClickListener {
            val intentApplyLoan = Intent(this@DashboardActivity, ApplyLoanActivity::class.java)
            startActivity(intentApplyLoan)
        }
        binding.ivNotification.setOnClickListener {

            showCustomDialog("Info","Under-Development")
        }



    }
    private fun startAutomaticScrolling() {
        coroutineScope.launch {
            while (true) {
                delay(3000) // adjust the delay as needed

                withContext(Dispatchers.Main) {
                    // Ensure the code runs on the main thread
                    try {
                        binding.viewPagerBanks.currentItem = (binding.viewPagerBanks.currentItem + 1) % binding.viewPagerBanks.adapter!!.itemCount
                        binding.viewPager.currentItem = (binding.viewPager.currentItem + 1) % binding.viewPager.adapter!!.itemCount

                    }catch (e:Exception){
                        binding.viewPagerBanks.currentItem = 0
                        binding.viewPager.currentItem = 0
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancel the coroutine when the activity is destroyed
    }
}