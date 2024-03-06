package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityPackageAssignBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class PackageAssignActivity : BaseActivity() {

    lateinit var binding: ActivityPackageAssignBinding

    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPackageAssignBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectPackageAssignActivity(this@PackageAssignActivity)

        attachObservers()

        initClicks()
        initialApiCall()

        setContentView(binding.root)
    }

    private fun initialApiCall() {
        binding.btnProcess.isEnabled = false
        val user = AppSession(this@PackageAssignActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        val regularPackageMap = hashMapOf<String,String>()
        regularPackageMap.put("user_id",user.user?.id.toString())

        val customPackageId = AppSession(this@PackageAssignActivity).getString(Constant.CUSTOM_PACKAGE_ID)
        val regularPackageId = AppSession(this@PackageAssignActivity).getString(Constant.REGULAR_PACKAGE_ID)




        try {
            if(regularPackageId!!.isNotEmpty()){
                mainViewModel.assignRegularPackage(regularPackageMap)

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

        try {
            if(customPackageId!!.isNotEmpty()){
                mainViewModel.assignCustomPackage(regularPackageMap)

            }
        }catch (e:Exception){

            e.printStackTrace()
            showToast("ID NOT FOUND")

        }







        binding.btnProcess.setOnClickListener {



            checkApplyLoan()
//            applyLoan()


        }




    }

    private fun checkApplyLoan() {
        val user = AppSession(this@PackageAssignActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse
        val loanInfo1Map = hashMapOf<String,String>()

        val regularPackageId = AppSession(this@PackageAssignActivity).getString(Constant.REGULAR_PACKAGE_ID)
        val customPackageId = AppSession(this@PackageAssignActivity).getString(Constant.CUSTOM_PACKAGE_ID)

        if(regularPackageId!=null){
            loanInfo1Map.put("package_id",regularPackageId)
            loanInfo1Map.put("id",user.user?.id.toString())
            mainViewModel.getLoanInfo1StatusResponse(loanInfo1Map)
        }else if(customPackageId!=null){
            loanInfo1Map.put("package_id",customPackageId)
            loanInfo1Map.put("id",user.user?.id.toString())
            mainViewModel.getLoanInfo1StatusResponse(loanInfo1Map)
        }


    }

    private fun applyLoan() {
        val user = AppSession(this@PackageAssignActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse
        val regularPackageId = AppSession(this@PackageAssignActivity).getString(Constant.REGULAR_PACKAGE_ID)
        val customPackageId = AppSession(this@PackageAssignActivity).getString(Constant.CUSTOM_PACKAGE_ID)
        if(regularPackageId!=null){
            val regularApplyLoan = hashMapOf<String,String>()
            regularApplyLoan.put("user_id",user.user?.id?.toString()!!)
            regularApplyLoan.put("package_id",regularPackageId)
            regularApplyLoan.put("type","Regular")
            mainViewModel.applyLoan(regularApplyLoan)
            val intentPackageAmount = Intent(this@PackageAssignActivity, SelectBankActivity::class.java)
            intentPackageAmount.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intentPackageAmount.putExtra("APPLY_LOAN",true)

            startActivity(intentPackageAmount)

        }
        if(customPackageId!=null){
            val regularApplyLoan = hashMapOf<String,String>()
            regularApplyLoan.put("user_id",user.user?.id?.toString()!!)
            regularApplyLoan.put("package_id",customPackageId)
            regularApplyLoan.put("type","Custom")
            mainViewModel.applyLoan(regularApplyLoan)
            val intentPackageAmount = Intent(this@PackageAssignActivity, SelectBankActivity::class.java)
            intentPackageAmount.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intentPackageAmount.putExtra("APPLY_LOAN",true)
            startActivity(intentPackageAmount)

        }




    }

    private fun attachObservers() {

        mainViewModel.loanInfo1StatusResponse.observe(this,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {
                showToast(it)
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){

                    if(it.data?.currentStatus.equals("Closed")){
                        applyLoan()
                    }else{
                        showCustomDialog("INFO","STATUS ${it.data?.status.toString()} Current status ${it.data?.currentStatus}")
                    val intentPackageAmount = Intent(this@PackageAssignActivity,
                        SelectBankActivity::class.java)
                    intentPackageAmount.putExtra("APPLY_LOAN",false)
                    intentPackageAmount.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intentPackageAmount)
                    }



                }else{
                    // check dashboard api for -1 loan id.
                    val user = AppSession(this@PackageAssignActivity).getObject(
                        Constant.USER_LOGIN,
                        LoginResponse::class.java) as LoginResponse
                    val regularApplyLoan = hashMapOf<String,String>()

                    regularApplyLoan.put("id",user?.user?.id.toString())
                    mainViewModel.getDashboardResponse(regularApplyLoan)



                }


            }


        ))

        val user = AppSession(this@PackageAssignActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        mainViewModel.assignRegularPackageResponse.observe(this, Event.EventObserver(
            onLoading = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                binding.packageState.text = "Package Assigned !"

                try {

                    if(it.userPackage?.package_data?.isNotEmpty() == true){
                        binding.packageState.text = "Package Assigned !"
                        binding.amount.setText(it.userPackage?.package_data?.get(0)?.amount.toString())
                        binding.btnProcess.isEnabled = true
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                    binding.amount.setText("NONE")
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


                binding.packageState.text = "Package Assigned !"

                try {
                    if(it.userPackage?.packagesData?.isNotEmpty() == true){

                        binding.packageState.text = "Package Assigned !"
                        binding.amount.setText(it.userPackage?.packagesData?.get(0)?.amount.toString())
                        binding.btnProcess.isEnabled = true

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    binding.amount.setText("NONE")
                }
            },
            onError = {
                hideLoadingDialog()
            }


        ))
        mainViewModel.dashboardResponse.observe(this,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()

            },
            onSuccess = {
                hideLoadingDialog()

                if(it.status==true){

                    if(it.assignedPackage!=0){
                        val regularPackageMap = hashMapOf<String,String>()
                        regularPackageMap.put("user_id",user.user?.id.toString())
                        mainViewModel.assignRegularPackage(regularPackageMap)
                    }else if(it.assignedCustom!=0){
                        val regularPackageMap = hashMapOf<String,String>()
                        regularPackageMap.put("user_id",user.user?.id.toString())
                        mainViewModel.assignCustomPackage(regularPackageMap)
                    }

                    if(it.loanInfo?.loanId==-1){
                        applyLoan()

                    }


                }


            }


        ))


    }
    private fun initClicks() {



    }
}