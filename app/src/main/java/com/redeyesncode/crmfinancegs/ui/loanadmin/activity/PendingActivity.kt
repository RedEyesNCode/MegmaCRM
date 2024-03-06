package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityPendingBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class PendingActivity : BaseActivity() {
    lateinit var binding: ActivityPendingBinding

    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPendingBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectPendingActivity(this@PendingActivity)


        initClicks()
        attachObservers()

        setContentView(binding.root)
    }



    private fun initClicks() {
        binding.ivClose.setOnClickListener {
            finishAffinity()
        }

//        binding.btnOtpContinue.setOnClickListener {
//            showTwoOptionsDialog(this@PendingActivity)
//
//
//        }
        val user = AppSession(this@PendingActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse


        Glide.with(binding.root).load(R.drawable.loading).placeholder(R.drawable.ic_kyc_pending).into(binding.ivLoader)


        binding.btnOtpContinue.setOnClickListener {
            val map = hashMapOf<String,String>()
            map.put("user_id",user.user?.id.toString())
            mainViewModel.checkKycResponse(map)

        }





    }
    private fun attachObservers() {
        mainViewModel.checkKycResponse.observe(this, Event.EventObserver(
            onLoading = {
                showLoadingDialog()

            },
            onError = {
                hideLoadingDialog()
                showToast(it)
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status.equals("success")){
                    if(it.data?.kycApproval.equals("Approved")){
                        val user = AppSession(this@PendingActivity).getObject(Constant.USER_LOGIN,
                            LoginResponse::class.java) as LoginResponse

                        val dashboardMap = hashMapOf<String,String>()
                        dashboardMap.put("id",user.user?.id.toString())
                        mainViewModel.getDashboardResponse(dashboardMap)
                    }else {

                        if(it.data?.kycApproval.equals("Rejected")){
                            var intentPackage = Intent(this@PendingActivity, RejectActivity::class.java)
                            intentPackage.putExtra("STATUS","YOUR KYC REQUEST HAS BEEN REJECTED")
                            intentPackage.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intentPackage)
                        }

                        showCustomDialog("Info","Your KYC Status : "+it.data?.kycApproval.toString())
                    }

                }

            }


        ))
        mainViewModel.dashboardResponse.observe(this,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onError = {
                showToast(it)
                hideLoadingDialog()

            },
            onSuccess = {
                hideLoadingDialog()

                if(it.status==true){

                    if(it.customPackages.isNotEmpty() && it.assignedCustom!=0){
                        AppSession(this@PendingActivity).putString(Constant.CUSTOM_PACKAGE_ID,it.assignedCustom.toString())
                    }
                    //Check for regular package id for user.
                    if (it.assignedPackage  != 0){
                        AppSession(this@PendingActivity).putString(Constant.REGULAR_PACKAGE_ID,it.assignedPackage.toString())
                    }

                    if(it.assignedPackage!=0){
                        var intentPackage = Intent(this@PendingActivity, PackageAssignActivity::class.java)
                        intentPackage.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intentPackage)
                    }else if(it.assignedCustom!=0){
                        var intentPackage = Intent(this@PendingActivity, PackageAssignActivity::class.java)
                        intentPackage.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intentPackage)
                    }
                    if(it.assignedPackage==0 && it.assignedCustom==0){
                        showCustomDialog("Info","Your Kyc is Approved. But no Package is assigned to you.")
                    }


                }


            }


        ))


    }

    fun showTwoOptionsDialog(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("KYC-USER-FLOW")

        // Set the positive button and its click listener
        alertDialogBuilder.setPositiveButton("User KYC is Approved") { dialog, which ->
            // Handle Option 1 click
            // You can add your logic here
            val intentReject = Intent(this@PendingActivity, ApproveActivity::class.java)
            intentReject.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intentReject.putExtra("STATUS","Your KYC Application was Approved !")
            startActivity(intentReject)


        }

        // Set the negative button and its click listener
        alertDialogBuilder.setNegativeButton("User KYC is Rejected") { dialog, which ->
            // Handle Option 2 click
            // You can add your logic here
            val intentReject = Intent(this@PendingActivity, RejectActivity::class.java)
            intentReject.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentReject)

        }

        // Create and show the AlertDialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}