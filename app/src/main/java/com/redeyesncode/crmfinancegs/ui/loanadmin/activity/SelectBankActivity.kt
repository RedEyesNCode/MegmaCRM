package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.databinding.ActivitySelectBankBinding
import com.redeyesncode.crmfinancegs.databinding.SearchBankDialogBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class SelectBankActivity : BaseActivity() {
    var bankAccounts = arrayListOf<String>()


    lateinit var binding: ActivitySelectBankBinding

    var isFirstCall = true

    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectBankBinding.inflate(layoutInflater)

        (application as AndroidApp).getDaggerComponent().injectBankActivity(this@SelectBankActivity)
//        showApplyLoanDialog()
        initClicks()
        attachObservers()
        setContentView(binding.root)
    }

    private fun showApplyLoanDialog() {
        if(intent.getBooleanExtra("APPLY_LOAN",false)){
            showCustomDialog("INFO","YOU HAVE APPLIED FOR LOAN JUST NOW !")
        }else {
            showCustomDialog("INFO","You have already applied for loan")
        }



    }

    private fun attachObservers() {
        val bankMap = hashMapOf<String,String>()
        val user = AppSession(this@SelectBankActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        bankMap.put("id",user.user?.id.toString())
        mainViewModel.showBankInfo("https://megmagroup.loan/newApi/api/show_bank",bankMap)


        mainViewModel.showBankAccountResponse.observe(this, Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()

                if(it.status==true){

                    binding.edtFullName.setText(it.data?.accHolderName.toString())
                    binding.edtBankList.setText(it.data?.bankName.toString())
                    binding.edtIfscCode.setText(it.data?.ifsc.toString())
                    binding.edtAccountNumber.setText(it.data?.accNo.toString())
                    binding.edtConfirmBankAccount.setText(it.data?.accNo.toString())
                    binding.edtBankList.setText(it.data?.bankName.toString())

                    if(it.loanInfo?.currentStatus.equals("Closed")){
                        //E nach
                        showCustomDialog("LOAN STATUS","YOUR LOAN IS CLOSED")
                    }else if(it.loanInfo?.currentStatus.equals("Underprocess")){
                        // Loan emi
                        val user = AppSession(this@SelectBankActivity).getObject(
                            Constant.USER_LOGIN,
                            LoginResponse::class.java) as LoginResponse

                        val dashboardMap = hashMapOf<String,String>()
                        dashboardMap.put("id",user.user?.id.toString())
                        mainViewModel.getDashboardResponse(dashboardMap)
                    }else if(it.loanInfo?.currentStatus.equals("Pending")){
                        if(!isFirstCall){
                            Handler().postDelayed(Runnable {
                                val intentENach = Intent(this@SelectBankActivity,
                                    LoanReviewActivity::class.java)
                                intentENach.putExtra("PACKAGE_ID",it.data?.id.toString())
                                intentENach.putExtra("BANK_ID",binding.edtBankList.text.toString())
                                intentENach.putExtra("LOAN_ID",binding.edtBankList.text.toString())
                                startActivity(intentENach)

                            },200)
                        }





                    }else if(it.loanInfo?.currentStatus.equals("No data found")){



                        showCustomDialog("Error !","Your Loan status is Unknown : ${it.loanInfo?.currentStatus.toString()}")



//                        applyLoan()


                    }

                }



            },
            onError = {
                hideLoadingDialog()
            }



        ))

        mainViewModel.addBankAccountResponse.observe(this,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()

            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){
                    mainViewModel.showBankInfo("https://megmagroup.loan/newApi/api/show_bank",bankMap)

                }

            }
        ))

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
                    showCustomDialog("Loan Status","Your current loan status :" +
                            "${it.data?.currentStatus.toString()} and " +
                            "Loan ID is ${it.data?.loanId.toString()}")



                }


            }


        ))
        mainViewModel.applyLoanResponse.observe(this,Event.EventObserver(
            onLoading = {
                        hideLoadingDialog()

            },
            onSuccess = {
                        hideLoadingDialog()
                if(it.status==true){
                    // hit the loanInfo1 api
                    val user = AppSession(this@SelectBankActivity).getObject(
                        Constant.USER_LOGIN,
                        LoginResponse::class.java) as LoginResponse
                    val loanInfo1Map = hashMapOf<String,String>()

                    val regularPackageId = AppSession(this@SelectBankActivity).getString(Constant.REGULAR_PACKAGE_ID)
                    val customPackageId = AppSession(this@SelectBankActivity).getString(Constant.CUSTOM_PACKAGE_ID)

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

            },
            onError = {

                showToast(it)

            }


        ))


        mainViewModel.dashboardResponse.observe(this,Event.EventObserver(

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

                    if(it.loanInfo?.loanId!=-1){


                        Handler().postDelayed(Runnable {
                            val intentLoanEmi = Intent(this@SelectBankActivity, LoanEmiActivity::class.java)
                            intentLoanEmi.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intentLoanEmi)

                        },1000)

                        AppSession(this@SelectBankActivity).putString(Constant.USER_LOAN_ID,it.loanInfo?.loanId.toString())

                    }

                }


            }


        ))




    }

    private fun applyLoan() {
        val user = AppSession(this@SelectBankActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse
        val regularPackageId = AppSession(this@SelectBankActivity).getString(Constant.REGULAR_PACKAGE_ID)
        val customPackageId = AppSession(this@SelectBankActivity).getString(Constant.CUSTOM_PACKAGE_ID)
        if(regularPackageId!=null){
            val regularApplyLoan = hashMapOf<String,String>()
            regularApplyLoan.put("user_id",user.user?.id?.toString()!!)
            regularApplyLoan.put("package_id",regularPackageId)
            regularApplyLoan.put("type","Regular")
            mainViewModel.applyLoan(regularApplyLoan)

        }
        if(customPackageId!=null){
            val regularApplyLoan = hashMapOf<String,String>()
            regularApplyLoan.put("user_id",user.user?.id?.toString()!!)
            regularApplyLoan.put("package_id",customPackageId)
            regularApplyLoan.put("type","Custom")
            mainViewModel.applyLoan(regularApplyLoan)

        }


    }


    private fun initClicks() {
        getBankList()
        binding.edtBankList.setOnClickListener {
            showStringListDialog(bankAccounts)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSubmit.setOnClickListener {

            if(isValidated()){
                val user = AppSession(this@SelectBankActivity).getObject(
                    Constant.USER_LOGIN,
                    LoginResponse::class.java) as LoginResponse
                val addBankMap = hashMapOf<String,String>()
                addBankMap.put("id",user.user?.id.toString())
                addBankMap.put("full_name",binding.edtFullName.text.toString())
                addBankMap.put("acc_number",binding.edtAccountNumber.text.toString())
                addBankMap.put("ifsc_code",binding.edtIfscCode.text.toString())
                addBankMap.put("bank_name",binding.edtBankList.text.toString())
                isFirstCall = false
                mainViewModel.addBankAccount("https://megmagroup.loan/newApi/api/add_bank",addBankMap)

            }

        }






    }
    private fun isValidated():Boolean{

        if(binding.edtFullName.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter full name")

            return false

        }else if(binding.edtAccountNumber.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter account number")

            return false

        }else if(binding.edtConfirmBankAccount.text.toString().isEmpty()){


            showCustomDialog("Info","Please enter confirm bank account")

            return false

        }else if(!binding.edtConfirmBankAccount.text.toString().equals(binding.edtAccountNumber.text.toString())){


            showCustomDialog("Info","Account numeber does not match")

            return false

        }else if(binding.edtIfscCode.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter IFSC Code")

            return false

        }else if(binding.edtIfscCode.text.toString().length<11){
            showCustomDialog("Info","Please enter VALID IFSC Code")
            return false
        }


        else{
            return true
        }





    }


    private fun getBankList() {
        try {
            // Your JSON data as a string
            //String jsonData = "{\"StatusCode\":\"NP000\",\"data\":{\"banks\":[{\"id\":0,\"name\":\"Select Your Bank\",\"bank_code\":\"SBIN\",\"mode\":\"netbanking, debit card\"},{\"id\":627,\"name\":\"STATE BANK OF INDIA\",\"bank_code\":\"SBIN\",\"mode\":\"netbanking, debit card\"},{\"id\":631,\"name\":\"THE COSMOS CO-OPERATIVE BANK LTD\",\"bank_code\":\"COSB\",\"mode\":\"netbanking\"},{\"id\":633,\"name\":\"THE HONGKONG AND SHANGHAI BANKING CORPORATION LTD\",\"bank_code\":\"HSBC\",\"mode\":\"netbanking\"},{\"id\":636,\"name\":\"KARUR VYSA BANK\",\"bank_code\":\"KVBL\",\"mode\":\"netbanking\"},{\"id\":77,\"name\":\"TAMILNAD MERCANTILE BANK LTD\",\"bank_code\":\"TMBL\",\"mode\":\"netbanking, debit card\"},{\"id\":1,\"name\":\"AXIS BANK\",\"bank_code\":\"UTIB\",\"mode\":\"netbanking, debit card\"},{\"id\":69,\"name\":\"SOUTH INDIAN BANK\",\"bank_code\":\"SIBL\",\"mode\":\"netbanking, debit card\"},{\"id\":630,\"name\":\"CITIBANK\",\"bank_code\":\"CITI\",\"mode\":\"netbanking, debit card\"},{\"id\":37,\"name\":\"DEUTSCHE BANK AG\",\"bank_code\":\"DEUT\",\"mode\":\"netbanking, debit card\"},{\"id\":638,\"name\":\"DHANALAXMI BANK\",\"bank_code\":\"DLXB\",\"mode\":\"netbanking, debit card\"},{\"id\":639,\"name\":\"DBS BANK INDIA LTD\",\"bank_code\":\"DBSS\",\"mode\":\"netbanking, debit card\"},{\"id\":641,\"name\":\"JANA SMALL FINANCE BANK LTD\",\"bank_code\":\"JSFB\",\"mode\":\"netbanking, debit card\"},{\"id\":328,\"name\":\"PAYTM PAYMENTS BANK LTD\",\"bank_code\":\"PYTM\",\"mode\":\"netbanking, debit card\"},{\"id\":52,\"name\":\"KOTAK MAHINDRA BANK LTD\",\"bank_code\":\"KKBK\",\"mode\":\"netbanking, debit card\"},{\"id\":15,\"name\":\"YES BANK\",\"bank_code\":\"YESB\",\"mode\":\"netbanking, debit card\"},{\"id\":247,\"name\":\"IDFC FIRST BANK LTD\",\"bank_code\":\"IDFB\",\"mode\":\"netbanking, debit card\"},{\"id\":6,\"name\":\"HDFC BANK LTD\",\"bank_code\":\"HDFC\",\"mode\":\"netbanking, debit card\"},{\"id\":284,\"name\":\"EQUITAS SMALL FINANCE BANK LTD\",\"bank_code\":\"ESFB\",\"mode\":\"netbanking, debit card\"},{\"id\":33,\"name\":\"CITY UNION BANK LTD\",\"bank_code\":\"CIUB\",\"mode\":\"netbanking\"},{\"id\":629,\"name\":\"ORIENTAL BANK OF COMMERCE\",\"bank_code\":\"ORBC\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":634,\"name\":\"SYNDICATE BANK\",\"bank_code\":\"SYNB\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":70,\"name\":\"STANDARD CHARTERED BANK\",\"bank_code\":\"SCBL\",\"mode\":\"netbanking, debit card\"},{\"id\":10,\"name\":\"INDIAN OVERSEAS BANK\",\"bank_code\":\"IOBA\",\"mode\":\"netbanking\"},{\"id\":347,\"name\":\"AU SMALL FINANCE BANK\",\"bank_code\":\"AUBL\",\"mode\":\"netbanking, debit card\"},{\"id\":29,\"name\":\"CANARA BANK\",\"bank_code\":\"CNRB\",\"mode\":\"netbanking, debit card\"},{\"id\":637,\"name\":\"DCB BANK LTD\",\"bank_code\":\"DCBL\",\"mode\":\"netbanking, debit card\"},{\"id\":615,\"name\":\"UJJIVAN SMALL FINANCE BANK LTD\",\"bank_code\":\"USFB\",\"mode\":\"netbanking\"},{\"id\":8,\"name\":\"IDBI BANK\",\"bank_code\":\"IBKL\",\"mode\":\"netbanking, debit card\"},{\"id\":632,\"name\":\"UNION BANK OF INDIA\",\"bank_code\":\"UBIN\",\"mode\":\"netbanking, debit card\"},{\"id\":4,\"name\":\"CENTRAL BANK OF INDIA\",\"bank_code\":\"CBIN\",\"mode\":\"netbanking\"},{\"id\":44,\"name\":\"INDUSIND BANK\",\"bank_code\":\"INDB\",\"mode\":\"netbanking, debit card\"},{\"id\":642,\"name\":\"BANDHAN BANK LTD\",\"bank_code\":\"BDBL\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":11,\"name\":\"PUNJAB NATIONAL BANK\",\"bank_code\":\"PUNB\",\"mode\":\"netbanking, debit card\"},{\"id\":82,\"name\":\"FEDERAL BANK\",\"bank_code\":\"FDRL\",\"mode\":\"netbanking, debit card\"},{\"id\":91,\"name\":\"RBL BANK LIMITED\",\"bank_code\":\"RATN\",\"mode\":\"netbanking, debit card\"},{\"id\":50,\"name\":\"KARNATAKA BANK LTD\",\"bank_code\":\"KARB\",\"mode\":\"netbanking, debit card\"},{\"id\":7,\"name\":\"ICICI BANK LTD\",\"bank_code\":\"ICIC\",\"mode\":\"netbanking, debit card\"},{\"id\":2,\"name\":\"BANK OF BARODA\",\"bank_code\":\"BARB\",\"mode\":\"netbanking, debit card\"},{\"id\":692,\"name\":\"THE CATHOLIC SYRIAN BANK\",\"bank_code\":\"CSBK\",\"mode\":\"netbanking, debit card\"},{\"id\":20,\"name\":\"ANDHRA BANK\",\"bank_code\":\"ANDB\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":680,\"name\":\"UCO BANK\",\"bank_code\":\"UCBA\",\"mode\":\"netbanking\"},{\"id\":711,\"name\":\"THE CHEMBUR NAGARIK SAHAKARI BANK\",\"bank_code\":\"CNSX\",\"mode\":\"debit card\"},{\"id\":712,\"name\":\"BANK OF INDIA\",\"bank_code\":\"BKID\",\"mode\":\"debit card\"},{\"id\":713,\"name\":\"SHIVALIK SMALL FINANCE BANK LTD\",\"bank_code\":\"SHIX\",\"mode\":\"debit card\"},{\"id\":691,\"name\":\"JIO PAYMENTS BANK LTD\",\"bank_code\":\"JIOP\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":640,\"name\":\"INDIAN BANK\",\"bank_code\":\"IDIB\",\"mode\":\"netbanking, debit card\"},{\"id\":710,\"name\":\"KARNATAKA VIKAS GRAMEENA BANK\",\"bank_code\":\"KVGB\",\"mode\":\"netbanking\"},{\"id\":715,\"name\":\" THE NATIONAL CO OP BANK LTD\",\"bank_code\":\"NCBL\",\"mode\":\"debit card\"},{\"id\":716,\"name\":\"KURLA NAGARIK SAHAKARI BANK LTD\",\"bank_code\":\"KNSB\",\"mode\":\"debit card\"},{\"id\":717,\"name\":\"THE VARACHHA CO OP BANK LTD\",\"bank_code\":\"VARA\",\"mode\":\"netbanking\"},{\"id\":709,\"name\":\"ANDHRA PRAGATHI GRAMEENA BANK\",\"bank_code\":\"APGB\",\"mode\":\"netbanking, debit card\"},{\"id\":985,\"name\":\"CAPITAL SMALL FINANCE BANK LTD\",\"bank_code\":\"CLBL\",\"mode\":\"debit card\"},{\"id\":986,\"name\":\"MAHESH SAHAKARI BANK LTD PUNE\",\"bank_code\":\"MHSX\",\"mode\":\"debit card\"},{\"id\":987,\"name\":\"THE ZOROASTRIAN CO OP BANK LTD\",\"bank_code\":\"ZCBL\",\"mode\":\"debit card\"},{\"id\":989,\"name\":\"Gramin Bank of Aryavart\",\"bank_code\":\"ARYX\",\"mode\":\"esign\"},{\"id\":1038,\"name\":\"THE ADARSH CO OP URBAN BANK LTD\",\"bank_code\":\"ACUX\",\"mode\":\"debit card\"},{\"id\":1039,\"name\":\"THE JUNAGADH COMMERCIAL CO OP BANK LTD\",\"bank_code\":\"JUCX\",\"mode\":\"debit card\"},{\"id\":1040,\"name\":\"THE SURAT PEOPLES CO OP BANK LTD\",\"bank_code\":\"SPCB\",\"mode\":\"debit card\"},{\"id\":984,\"name\":\"AIRTEL PAYMENTS BANK LTD\",\"bank_code\":\"AIRP\",\"mode\":\"netbanking, debit card\"},{\"id\":635,\"name\":\"PUNJAB AND SIND BANK\",\"bank_code\":\"PSIB\",\"mode\":\"netbanking, debit card\"},{\"id\":1047,\"name\":\"ESAF SMALL FINANCE BANK LTD\",\"bank_code\":\"ESAF\",\"mode\":\"netbanking, debit card\"},{\"id\":1046,\"name\":\"THE KALUPUR COMMERCIAL CO OP BANK\",\"bank_code\":\"KCCB\",\"mode\":\"debit card\"},{\"id\":1048,\"name\":\"RAJARSHI SHAHU SAHAKARI BANK LTD\",\"bank_code\":\"RSSX\",\"mode\":\"debit card\"},{\"id\":1044,\"name\":\"SBM BANK INDIA LTD\",\"bank_code\":\"STCB\",\"mode\":\"netbanking, debit card\"},{\"id\":24,\"name\":\"BANK OF MAHARASHTRA\",\"bank_code\":\"MAHB\",\"mode\":\"netbanking, debit card\"},{\"id\":1043,\"name\":\"THE JAMMU AND KASHMIR BANK LTD\",\"bank_code\":\"JAKA\",\"mode\":\"netbanking, debit card\"},{\"id\":1049,\"name\":\"FINCARE SMALL FINANCE BANK LTD\",\"bank_code\":\"FINF\",\"mode\":\"debit card\"},{\"id\":1051,\"name\":\"UTKARSH SMALL FINANCE BANK LTD\",\"bank_code\":\"UTKS\",\"mode\":\"debit card\"},{\"id\":1052,\"name\":\"NSDL Payments Banks Ltd\",\"bank_code\":\"NSPB\",\"mode\":\"netbanking, debit card\"},{\"id\":1045,\"name\":\"SURYODAY SMALL FINANCE BANK LTD\",\"bank_code\":\"SURY\",\"mode\":\"netbanking, debit card\"},{\"id\":1050,\"name\":\"NATIONAL SECURITIES DEPOSITORY LTD\",\"bank_code\":\"NSDL\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":1117,\"name\":\"CHHATTISGARH GRAMIN BANK\",\"bank_code\":\"CGBX\",\"mode\":\"debit card\"},{\"id\":1118,\"name\":\"ELLAQUAI DEHATI BANK\",\"bank_code\":\"EDBX\",\"mode\":\"debit card\"},{\"id\":1113,\"name\":\"THE SONEPAT CENTRAL CO OP BANK LTD\",\"bank_code\":\"SNPX\",\"mode\":\"esign\"},{\"id\":1114,\"name\":\"SANGLI URBAN CO OP BANK LTD\",\"bank_code\":\"SUCX\",\"mode\":\"esign\"},{\"id\":1071,\"name\":\"ORIENTAL BANK OF COMMERCE\",\"bank_code\":\"ORBC\",\"mode\":\"esign\"},{\"id\":1107,\"name\":\"THE CHOPDA PEOPLES CO OP BANK LTD\",\"bank_code\":\"CHPX\",\"mode\":\"esign\"},{\"id\":1108,\"name\":\"CHAITANYA MAHILA SAHAKARI BANK LTD VIJAYAPUR\",\"bank_code\":\"CMSV\",\"mode\":\"esign\"},{\"id\":1109,\"name\":\"THE JALNA PEOPLES CO OP BANK LTD JALNA\",\"bank_code\":\"JLNX\",\"mode\":\"esign\"},{\"id\":1110,\"name\":\"KARNATAKA VIKAS GRAMEENA BANK\",\"bank_code\":\"KVGB\",\"mode\":\"esign\"},{\"id\":1111,\"name\":\"RAJKOT NAGARIK SAHAKARI BANK LTD\",\"bank_code\":\"RNSB\",\"mode\":\"esign\"},{\"id\":1112,\"name\":\"SEHORE NAGRIK SAHAKARI BANK LTD SEHORE\",\"bank_code\":\"SENX\",\"mode\":\"esign\"},{\"id\":1115,\"name\":\"THE MEGHRAJ NAGARIK SAHAKARI BANK LTD\",\"bank_code\":\"TNBX\",\"mode\":\"esign\"},{\"id\":1116,\"name\":\"THE VAISH CO OP ADARSH BANK LTD\",\"bank_code\":\"VCAX\",\"mode\":\"esign\"}]}}";
            var jsonData =
                "{\"StatusCode\":\"NP000\",\"data\":{\"banks\":[{\"id\":0,\"name\":\"" + "Select Bank"
                    .toString() + "\",\"bank_code\":\"SBIN\",\"mode\":\"netbanking, debit card\"},{\"id\":627,\"name\":\"STATE BANK OF INDIA\",\"bank_code\":\"SBIN\",\"mode\":\"netbanking, debit card\"},{\"id\":631,\"name\":\"THE COSMOS CO-OPERATIVE BANK LTD\",\"bank_code\":\"COSB\",\"mode\":\"netbanking\"},{\"id\":633,\"name\":\"THE HONGKONG AND SHANGHAI BANKING CORPORATION LTD\",\"bank_code\":\"HSBC\",\"mode\":\"netbanking\"},{\"id\":636,\"name\":\"KARUR VYSA BANK\",\"bank_code\":\"KVBL\",\"mode\":\"netbanking\"},{\"id\":77,\"name\":\"TAMILNAD MERCANTILE BANK LTD\",\"bank_code\":\"TMBL\",\"mode\":\"netbanking, debit card\"},{\"id\":1,\"name\":\"AXIS BANK\",\"bank_code\":\"UTIB\",\"mode\":\"netbanking, debit card\"},{\"id\":69,\"name\":\"SOUTH INDIAN BANK\",\"bank_code\":\"SIBL\",\"mode\":\"netbanking, debit card\"},{\"id\":630,\"name\":\"CITIBANK\",\"bank_code\":\"CITI\",\"mode\":\"netbanking, debit card\"},{\"id\":37,\"name\":\"DEUTSCHE BANK AG\",\"bank_code\":\"DEUT\",\"mode\":\"netbanking, debit card\"},{\"id\":638,\"name\":\"DHANALAXMI BANK\",\"bank_code\":\"DLXB\",\"mode\":\"netbanking, debit card\"},{\"id\":639,\"name\":\"DBS BANK INDIA LTD\",\"bank_code\":\"DBSS\",\"mode\":\"netbanking, debit card\"},{\"id\":641,\"name\":\"JANA SMALL FINANCE BANK LTD\",\"bank_code\":\"JSFB\",\"mode\":\"netbanking, debit card\"},{\"id\":328,\"name\":\"PAYTM PAYMENTS BANK LTD\",\"bank_code\":\"PYTM\",\"mode\":\"netbanking, debit card\"},{\"id\":52,\"name\":\"KOTAK MAHINDRA BANK LTD\",\"bank_code\":\"KKBK\",\"mode\":\"netbanking, debit card\"},{\"id\":15,\"name\":\"YES BANK\",\"bank_code\":\"YESB\",\"mode\":\"netbanking, debit card\"},{\"id\":247,\"name\":\"IDFC FIRST BANK LTD\",\"bank_code\":\"IDFB\",\"mode\":\"netbanking, debit card\"},{\"id\":6,\"name\":\"HDFC BANK LTD\",\"bank_code\":\"HDFC\",\"mode\":\"netbanking, debit card\"},{\"id\":284,\"name\":\"EQUITAS SMALL FINANCE BANK LTD\",\"bank_code\":\"ESFB\",\"mode\":\"netbanking, debit card\"},{\"id\":33,\"name\":\"CITY UNION BANK LTD\",\"bank_code\":\"CIUB\",\"mode\":\"netbanking\"},{\"id\":629,\"name\":\"ORIENTAL BANK OF COMMERCE\",\"bank_code\":\"ORBC\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":634,\"name\":\"SYNDICATE BANK\",\"bank_code\":\"SYNB\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":70,\"name\":\"STANDARD CHARTERED BANK\",\"bank_code\":\"SCBL\",\"mode\":\"netbanking, debit card\"},{\"id\":10,\"name\":\"INDIAN OVERSEAS BANK\",\"bank_code\":\"IOBA\",\"mode\":\"netbanking\"},{\"id\":347,\"name\":\"AU SMALL FINANCE BANK\",\"bank_code\":\"AUBL\",\"mode\":\"netbanking, debit card\"},{\"id\":29,\"name\":\"CANARA BANK\",\"bank_code\":\"CNRB\",\"mode\":\"netbanking, debit card\"},{\"id\":637,\"name\":\"DCB BANK LTD\",\"bank_code\":\"DCBL\",\"mode\":\"netbanking, debit card\"},{\"id\":615,\"name\":\"UJJIVAN SMALL FINANCE BANK LTD\",\"bank_code\":\"USFB\",\"mode\":\"netbanking\"},{\"id\":8,\"name\":\"IDBI BANK\",\"bank_code\":\"IBKL\",\"mode\":\"netbanking, debit card\"},{\"id\":632,\"name\":\"UNION BANK OF INDIA\",\"bank_code\":\"UBIN\",\"mode\":\"netbanking, debit card\"},{\"id\":4,\"name\":\"CENTRAL BANK OF INDIA\",\"bank_code\":\"CBIN\",\"mode\":\"netbanking\"},{\"id\":44,\"name\":\"INDUSIND BANK\",\"bank_code\":\"INDB\",\"mode\":\"netbanking, debit card\"},{\"id\":642,\"name\":\"BANDHAN BANK LTD\",\"bank_code\":\"BDBL\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":11,\"name\":\"PUNJAB NATIONAL BANK\",\"bank_code\":\"PUNB\",\"mode\":\"netbanking, debit card\"},{\"id\":82,\"name\":\"FEDERAL BANK\",\"bank_code\":\"FDRL\",\"mode\":\"netbanking, debit card\"},{\"id\":91,\"name\":\"RBL BANK LIMITED\",\"bank_code\":\"RATN\",\"mode\":\"netbanking, debit card\"},{\"id\":50,\"name\":\"KARNATAKA BANK LTD\",\"bank_code\":\"KARB\",\"mode\":\"netbanking, debit card\"},{\"id\":7,\"name\":\"ICICI BANK LTD\",\"bank_code\":\"ICIC\",\"mode\":\"netbanking, debit card\"},{\"id\":2,\"name\":\"BANK OF BARODA\",\"bank_code\":\"BARB\",\"mode\":\"netbanking, debit card\"},{\"id\":692,\"name\":\"THE CATHOLIC SYRIAN BANK\",\"bank_code\":\"CSBK\",\"mode\":\"netbanking, debit card\"},{\"id\":20,\"name\":\"ANDHRA BANK\",\"bank_code\":\"ANDB\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":680,\"name\":\"UCO BANK\",\"bank_code\":\"UCBA\",\"mode\":\"netbanking\"},{\"id\":711,\"name\":\"THE CHEMBUR NAGARIK SAHAKARI BANK\",\"bank_code\":\"CNSX\",\"mode\":\"debit card\"},{\"id\":712,\"name\":\"BANK OF INDIA\",\"bank_code\":\"BKID\",\"mode\":\"debit card\"},{\"id\":713,\"name\":\"SHIVALIK SMALL FINANCE BANK LTD\",\"bank_code\":\"SHIX\",\"mode\":\"debit card\"},{\"id\":691,\"name\":\"JIO PAYMENTS BANK LTD\",\"bank_code\":\"JIOP\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":640,\"name\":\"INDIAN BANK\",\"bank_code\":\"IDIB\",\"mode\":\"netbanking, debit card\"},{\"id\":710,\"name\":\"KARNATAKA VIKAS GRAMEENA BANK\",\"bank_code\":\"KVGB\",\"mode\":\"netbanking\"},{\"id\":715,\"name\":\" THE NATIONAL CO OP BANK LTD\",\"bank_code\":\"NCBL\",\"mode\":\"debit card\"},{\"id\":716,\"name\":\"KURLA NAGARIK SAHAKARI BANK LTD\",\"bank_code\":\"KNSB\",\"mode\":\"debit card\"},{\"id\":717,\"name\":\"THE VARACHHA CO OP BANK LTD\",\"bank_code\":\"VARA\",\"mode\":\"netbanking\"},{\"id\":709,\"name\":\"ANDHRA PRAGATHI GRAMEENA BANK\",\"bank_code\":\"APGB\",\"mode\":\"netbanking, debit card\"},{\"id\":985,\"name\":\"CAPITAL SMALL FINANCE BANK LTD\",\"bank_code\":\"CLBL\",\"mode\":\"debit card\"},{\"id\":986,\"name\":\"MAHESH SAHAKARI BANK LTD PUNE\",\"bank_code\":\"MHSX\",\"mode\":\"debit card\"},{\"id\":987,\"name\":\"THE ZOROASTRIAN CO OP BANK LTD\",\"bank_code\":\"ZCBL\",\"mode\":\"debit card\"},{\"id\":989,\"name\":\"Gramin Bank of Aryavart\",\"bank_code\":\"ARYX\",\"mode\":\"esign\"},{\"id\":1038,\"name\":\"THE ADARSH CO OP URBAN BANK LTD\",\"bank_code\":\"ACUX\",\"mode\":\"debit card\"},{\"id\":1039,\"name\":\"THE JUNAGADH COMMERCIAL CO OP BANK LTD\",\"bank_code\":\"JUCX\",\"mode\":\"debit card\"},{\"id\":1040,\"name\":\"THE SURAT PEOPLES CO OP BANK LTD\",\"bank_code\":\"SPCB\",\"mode\":\"debit card\"},{\"id\":984,\"name\":\"AIRTEL PAYMENTS BANK LTD\",\"bank_code\":\"AIRP\",\"mode\":\"netbanking, debit card\"},{\"id\":635,\"name\":\"PUNJAB AND SIND BANK\",\"bank_code\":\"PSIB\",\"mode\":\"netbanking, debit card\"},{\"id\":1047,\"name\":\"ESAF SMALL FINANCE BANK LTD\",\"bank_code\":\"ESAF\",\"mode\":\"netbanking, debit card\"},{\"id\":1046,\"name\":\"THE KALUPUR COMMERCIAL CO OP BANK\",\"bank_code\":\"KCCB\",\"mode\":\"debit card\"},{\"id\":1048,\"name\":\"RAJARSHI SHAHU SAHAKARI BANK LTD\",\"bank_code\":\"RSSX\",\"mode\":\"debit card\"},{\"id\":1044,\"name\":\"SBM BANK INDIA LTD\",\"bank_code\":\"STCB\",\"mode\":\"netbanking, debit card\"},{\"id\":24,\"name\":\"BANK OF MAHARASHTRA\",\"bank_code\":\"MAHB\",\"mode\":\"netbanking, debit card\"},{\"id\":1043,\"name\":\"THE JAMMU AND KASHMIR BANK LTD\",\"bank_code\":\"JAKA\",\"mode\":\"netbanking, debit card\"},{\"id\":1049,\"name\":\"FINCARE SMALL FINANCE BANK LTD\",\"bank_code\":\"FINF\",\"mode\":\"debit card\"},{\"id\":1051,\"name\":\"UTKARSH SMALL FINANCE BANK LTD\",\"bank_code\":\"UTKS\",\"mode\":\"debit card\"},{\"id\":1052,\"name\":\"NSDL Payments Banks Ltd\",\"bank_code\":\"NSPB\",\"mode\":\"netbanking, debit card\"},{\"id\":1045,\"name\":\"SURYODAY SMALL FINANCE BANK LTD\",\"bank_code\":\"SURY\",\"mode\":\"netbanking, debit card\"},{\"id\":1050,\"name\":\"NATIONAL SECURITIES DEPOSITORY LTD\",\"bank_code\":\"NSDL\",\"mode\":\"E-mandate facility not available for this bank\"},{\"id\":1117,\"name\":\"CHHATTISGARH GRAMIN BANK\",\"bank_code\":\"CGBX\",\"mode\":\"debit card\"},{\"id\":1118,\"name\":\"ELLAQUAI DEHATI BANK\",\"bank_code\":\"EDBX\",\"mode\":\"debit card\"},{\"id\":1113,\"name\":\"THE SONEPAT CENTRAL CO OP BANK LTD\",\"bank_code\":\"SNPX\",\"mode\":\"esign\"},{\"id\":1114,\"name\":\"SANGLI URBAN CO OP BANK LTD\",\"bank_code\":\"SUCX\",\"mode\":\"esign\"},{\"id\":1071,\"name\":\"ORIENTAL BANK OF COMMERCE\",\"bank_code\":\"ORBC\",\"mode\":\"esign\"},{\"id\":1107,\"name\":\"THE CHOPDA PEOPLES CO OP BANK LTD\",\"bank_code\":\"CHPX\",\"mode\":\"esign\"},{\"id\":1108,\"name\":\"CHAITANYA MAHILA SAHAKARI BANK LTD VIJAYAPUR\",\"bank_code\":\"CMSV\",\"mode\":\"esign\"},{\"id\":1109,\"name\":\"THE JALNA PEOPLES CO OP BANK LTD JALNA\",\"bank_code\":\"JLNX\",\"mode\":\"esign\"},{\"id\":1110,\"name\":\"KARNATAKA VIKAS GRAMEENA BANK\",\"bank_code\":\"KVGB\",\"mode\":\"esign\"},{\"id\":1111,\"name\":\"RAJKOT NAGARIK SAHAKARI BANK LTD\",\"bank_code\":\"RNSB\",\"mode\":\"esign\"},{\"id\":1112,\"name\":\"SEHORE NAGRIK SAHAKARI BANK LTD SEHORE\",\"bank_code\":\"SENX\",\"mode\":\"esign\"},{\"id\":1115,\"name\":\"THE MEGHRAJ NAGARIK SAHAKARI BANK LTD\",\"bank_code\":\"TNBX\",\"mode\":\"esign\"},{\"id\":1116,\"name\":\"THE VAISH CO OP ADARSH BANK LTD\",\"bank_code\":\"VCAX\",\"mode\":\"esign\"}]}}"

            //ifsc.setText(bname.getText().toString());
            // Parse the JSON string
            val json = JSONObject(jsonData)

            // Get the "data" object
            val data: JSONObject = json.getJSONObject("data")

            // Get the "banks" array
            val banksArray = data.getJSONArray("banks")
            bankAccounts = ArrayList<String>(banksArray.length())
            // Iterate through the array
            for (i in 0 until banksArray.length()) {
                val bank = banksArray.getJSONObject(i)
                val accountData =
                    bank.getString("name").split(": ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                if (accountData.size == 2) {
                    val id = accountData[1].toInt()
                    val name = accountData[0]
                    val bankCode = bank.getString("bank_code")
                    val mode = bank.getString("mode")
                    //bname.setText(name);
                    bankAccounts.add("${name}:${id}")

                } else {
                    val id = bank.getInt("id")
                    val name = bank.getString("name")
                    val bankCode = bank.getString("bank_code")
                    val mode = bank.getString("mode")
                    bankAccounts.add("${name}:${id}")
                }
                // Extract data from the bank object
            }


        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    fun showStringListDialog(stringList: ArrayList<String>) {
        val items = extractNamesFromList(stringList).toTypedArray()


        val dialogBinding = SearchBankDialogBinding.inflate(LayoutInflater.from(this@SelectBankActivity))


        val adapter = ArrayAdapter(this@SelectBankActivity, R.layout.simple_list_item_1, items)
        dialogBinding.listView.adapter = adapter
        val dialog = AlertDialog.Builder(this@SelectBankActivity)
            .setView(dialogBinding.root)
            .setNegativeButton("Cancel") { dialog, which ->

                dialog.dismiss()

            }
            .create()
        dialog.show()
        // Set up item click listener
        dialogBinding.listView.setOnItemClickListener { _, _, which, _ ->
            val selectedItem = stringList[which]
            binding.edtBankList.setText(selectedItem)

            dialog.dismiss()
            // Perform actions with the selected item
            // For example, you can display a Toast
            // Toast.makeText(context, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
        }
        dialogBinding.searchView.performClick()
        dialogBinding.searchView.requestFocus()

        // Set up search bar functionality
        dialogBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })


    }
    fun extractNamesFromList(list: ArrayList<String>): ArrayList<String> {
        val namesList = ArrayList<String>()

        for (item in list) {
            val parts = item.split(":")
            val name = parts.getOrNull(0)?.trim()
            name?.let { namesList.add(it) }
        }

        return namesList
    }
}