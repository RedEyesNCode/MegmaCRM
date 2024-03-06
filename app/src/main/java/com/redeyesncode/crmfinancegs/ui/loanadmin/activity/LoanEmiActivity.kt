package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.crmfinancegs.data.DashboardResponse
import com.redeyesncode.crmfinancegs.data.EmiResponse
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityLoanEmiBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.crmfinancegs.ui.loanadmin.adapter.LoanEmiAdapter
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class LoanEmiActivity : BaseActivity() , LoanEmiAdapter.onClickEmi{


    lateinit var dataEmi : ArrayList<EmiResponse.Data>
    override fun onEmiPayClick(data: EmiResponse.Data) {
        // navigate to UPI-VIEW ACTIVITY
        val loanId = AppSession(this@LoanEmiActivity).getString(Constant.USER_LOAN_ID)

        val upiViewActivity = Intent(this@LoanEmiActivity, UpiViewActivity::class.java)
        upiViewActivity.putExtra("LOAN_ID",loanId)
        upiViewActivity.putExtra("EMI_ID",data.id.toString())
        upiViewActivity.putExtra("LOAN_AMOUNT",data.amount.toString())

        startActivity(upiViewActivity)



    }

    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    lateinit var binding: ActivityLoanEmiBinding
    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoanEmiBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectLoamEmiActivity(this@LoanEmiActivity)

        initialApiCall()
        attachObservers()
        initClicks()
        setContentView(binding.root)
    }

    private fun initClicks() {

        binding.btnPayAll.setOnClickListener {
            var calculatedAmount = -1
            for (emi in dataEmi){
                if(emi.status.equals("Unpaid")){
                    calculatedAmount += emi.amount!!.toInt()
                }


            }
            val loanId = AppSession(this@LoanEmiActivity).getString(Constant.USER_LOAN_ID)

            val upiViewActivity = Intent(this@LoanEmiActivity, UpiViewActivity::class.java)
            upiViewActivity.putExtra("LOAN_ID",loanId)
            upiViewActivity.putExtra("LOAN_AMOUNT",calculatedAmount.toString())
            upiViewActivity.putExtra("EMI_ID","")
            startActivity(upiViewActivity)

        }


    }


    private fun initialApiCall() {
        val user = AppSession(this@LoanEmiActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        val loanId = AppSession(this@LoanEmiActivity).getString(Constant.USER_LOAN_ID)
        val map = hashMapOf<String,String>()

        val loanInfo = hashMapOf<String,String>()
        val loanInfoMap = hashMapOf<String,String>()
        loanInfoMap.put("user_id",user.user?.id.toString())


        //GET EMI INFO API
        if(loanId!!.isNotEmpty()){
            map.put("id",user.user?.id.toString())

            map.put("loan_id",loanId)

            mainViewModel.getEmiResponse(map)
        }else{
            showSnackbar("Loan ID Not Found !")
        }


        // LOAN INFO API CALL
        val dashboardResponse = AppSession(this@LoanEmiActivity).getObject(Constant.USER_DASHBOARD,
            DashboardResponse::class.java) as DashboardResponse


        loanInfoMap.put("user_id",user.user?.id.toString())

        if(dashboardResponse.assignedPackage!=0 || dashboardResponse.assignedCustom!=0) {
            if (dashboardResponse.assignedCustom != 0) {
                loanInfoMap.put("type", "Custom")
                loanInfoMap.put("id", dashboardResponse.assignedCustom.toString())
                mainViewModel.getLoanInfoResponse(loanInfoMap)

            } else if (dashboardResponse.assignedPackage != 0) {
                loanInfoMap.put("type", "Regular")
                loanInfoMap.put("id", dashboardResponse.assignedPackage.toString())

                mainViewModel.getLoanInfoResponse(loanInfoMap)

            }
        }else{
            val dashboardMap = hashMapOf<String,String>()
            dashboardMap.put("id",user.user?.id.toString())
            mainViewModel.getDashboardResponse(dashboardMap)

        }

    }
    private fun attachObservers() {
        mainViewModel.dashboardResponse.observe(this,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                val user = AppSession(this@LoanEmiActivity).getObject(
                    Constant.USER_LOGIN,
                    LoginResponse::class.java) as LoginResponse

                AppSession(this@LoanEmiActivity).putObject(Constant.USER_DASHBOARD,it)

                val dashboardResponse = it
                val loanInfoMap = hashMapOf<String,String>()
                loanInfoMap.put("user_id",user.user?.id.toString())

                if(dashboardResponse.assignedPackage!=0 || dashboardResponse.assignedCustom!=0) {
                    if (dashboardResponse.assignedCustom != 0) {
                        loanInfoMap.put("type", "Custom")
                        loanInfoMap.put("id", dashboardResponse.assignedCustom.toString())
                        mainViewModel.getLoanInfoResponse(loanInfoMap)

                    } else if (dashboardResponse.assignedPackage != 0) {
                        loanInfoMap.put("type", "Regular")
                        loanInfoMap.put("id", dashboardResponse.assignedPackage.toString())

                        mainViewModel.getLoanInfoResponse(loanInfoMap)

                    }
                }
            }



        ))
        mainViewModel.loanInfoResponse.observe(this,Event.EventObserver(

            onLoading = {
                hideLoadingDialog()
            },
            onSuccess = {
                        hideLoadingDialog()


                if(it.status==true){
                    binding.tvLoanAmount.text = "${it.data?.loanAmount.toString()}"
                    binding.tvLoanId.text = "Fee : ${it.data?.totalFee.toString()}"
                    binding.tvTenure.text = "${it.data?.tenure.toString()} Months"



                }else{
                    binding.layoutLoanDetails.visibility = View.INVISIBLE
                }

            },
            onError = {
                hideLoadingDialog()
                showToast(it)

            }



        ))
        mainViewModel.loanEmiResponse.observe(this, Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()
                showToast(it)
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){
                    try {
                        dataEmi = it.data
                        binding.recyclerEmiList.apply {
                            layoutManager = LinearLayoutManager(this@LoanEmiActivity,
                                LinearLayoutManager.VERTICAL,false)
                            adapter = LoanEmiAdapter(this@LoanEmiActivity,it.data,this@LoanEmiActivity)

                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                        showToast("Unable to fetch EMI's")
                    }

                }else{
                    showSnackbar("NO DATA FOUND !")
                }

            }



        ))



    }

}