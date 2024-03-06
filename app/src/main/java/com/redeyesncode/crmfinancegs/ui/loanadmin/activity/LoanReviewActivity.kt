package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import com.redeyesncode.crmfinancegs.data.DashboardResponse
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityLoanReviewBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class LoanReviewActivity : BaseActivity() {

    lateinit var binding: ActivityLoanReviewBinding


    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    var bankId:String?=null

    var loanAmount:String?=null

    var loanId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoanReviewBinding.inflate(layoutInflater)

        (application as AndroidApp).getDaggerComponent().injectLoanReviewScreen(this@LoanReviewActivity)

        initClicks()
        initialApiCall()
        attachObservers()

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnOtpContinue.setOnClickListener {

//            intentENach.putExtra("PACKAGE_ID",it.data?.id.toString())
//            intentENach.putExtra("BANK_ID",binding.edtBankList.text.toString())
//            intentENach.putExtra("LOAN_AMOUNT",it.loanInfo.toString())
//            intentENach.putExtra("LOAN_ID",binding.edtBankList.text.toString())


            if(loanAmount!=null){
                val reject = Intent(this@LoanReviewActivity, EMandateActivity::class.java)
                reject.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                reject.putExtra("STATUS","Your Loan was Approved !")
                reject.putExtra("BANK_ID",intent.getStringExtra("BANK_ID"))
                reject.putExtra("LOAN_AMOUNT",loanAmount)
                reject.putExtra("LOAN_ID",intent.getStringExtra("LOAN_ID"))

                startActivity(reject)
            }else{
                showToast("Passing default loan amount --> 1000 as L")
                val reject = Intent(this@LoanReviewActivity, EMandateActivity::class.java)
                reject.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                reject.putExtra("STATUS","Your Loan was Approved !")
                reject.putExtra("BANK_ID",intent.getStringExtra("BANK_ID"))
                reject.putExtra("LOAN_AMOUNT","1000")
                reject.putExtra("LOAN_ID",intent.getStringExtra("LOAN_ID"))

                startActivity(reject)
            }



        }

        val user = AppSession(this@LoanReviewActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        binding.tvUser.setText(user.user?.name.toString())

        val dashboard = AppSession(this@LoanReviewActivity).getObject(Constant.USER_DASHBOARD,
            DashboardResponse::class.java)as DashboardResponse

       // get full name from api.
        binding.tvUser.setText(dashboard.user?.name.toString())



    }

    private fun initialApiCall() {
        val user = AppSession(this@LoanReviewActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        val dashboardResponse = AppSession(this@LoanReviewActivity).getObject(Constant.USER_DASHBOARD,
            DashboardResponse::class.java) as DashboardResponse


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
                val user = AppSession(this@LoanReviewActivity).getObject(
                    Constant.USER_LOGIN,
                    LoginResponse::class.java) as LoginResponse

                AppSession(this@LoanReviewActivity).putObject(Constant.USER_DASHBOARD,it)

                val dashboardResponse = it
                binding.tvUser.setText(dashboardResponse.user?.name.toString())

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
                showLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){


                    try {
                        loanAmount = "${it.data?.loanAmount?.toInt()
                            ?.plus(it.data?.totalInterest?.toInt()!!)}"

                        binding.tvTotalAmountToPay.text = "RS ${loanAmount}"

                    }catch (e:Exception){
                        loanAmount = it.data?.loanAmount.toString()
                        e.printStackTrace()
                    }

                    AppSession(this@LoanReviewActivity).putString(Constant.USER_LOAN_ID,it.data?.loan_id.toString())


                    binding.txtLoanAmt.text = "RS ${it.data?.loanAmount.toString()}"
                    binding.txtLoanFinal.text = "RS ${it.data?.loanAmount.toString()}"
                    binding.txt12Months.text = "${it.data?.tenure.toString()} Months"
                    binding.txtLoanFinal.text ="RS ${it.data?.ttb.toString()}"
                    binding.txtCharges.text = "RS ${it.data?.totalFee.toString()}"
                    binding.tvTotalInterest.text = "RS ${it.data?.totalInterest.toString()}"
                    binding.tvInsurancePremimum.text = "RS ${it.data?.insurance.toString()}"


                    val result = (loanAmount?.toDouble()?.div(it.data?.tenure!!.toDouble())).toString()

                    val roundedResult = "%.2f".format(result.toDouble())

                    binding.tvMonthlyEmi.text = "RS ${roundedResult}"



                }


            },
            onError = {
                hideLoadingDialog()
                showToast(it)
            }


        ))



    }

}