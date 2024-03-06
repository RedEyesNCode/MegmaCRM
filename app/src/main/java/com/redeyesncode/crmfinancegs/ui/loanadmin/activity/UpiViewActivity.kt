package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import androidx.appcompat.app.AlertDialog
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityUpiViewBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.crmfinancegs.data.BodyUPIOrder
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

class UpiViewActivity : BaseActivity() {

    lateinit var binding: ActivityUpiViewBinding

    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    lateinit var client_txn_id: String

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityUpiViewBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectUpiViewActivity(this@UpiViewActivity)
        client_txn_id = generateAlphaNumericString()
        initWebView()
        initialApiCall()
        attachObservers()

        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.ivFakeUpi.setOnClickListener {
            callPayEmiApi()

        }


    }

    fun generateAlphaNumericString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun hookUPIStatus(){
        coroutineScope.launch {
            while (true) {
                val map = hashMapOf<String, String>()
                map["key"] = getString(R.string.EMI_API_KEY)
                map["client_txn_id"] = client_txn_id
                map["txn_date"] = getCurrentDate()

                val url = "https://api.ekqr.in/api/check_order_status"

                mainViewModel.checkUpiStatus(url, map)
                // Delay for 14 seconds before making the next API call
                delay(14000L)
            }
        }

    }

    private fun initialApiCall() {

        val user = AppSession(this@UpiViewActivity).getObject(Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse
        val bodyUPIOrder = BodyUPIOrder()
        bodyUPIOrder.amount = intent.getStringExtra("LOAN_AMOUNT")
        bodyUPIOrder.key = getString(R.string.EMI_API_KEY)

        bodyUPIOrder.clientTxnId =client_txn_id
        bodyUPIOrder.pInfo = "GS-LOAN-REPAYMENT-EMI"
        bodyUPIOrder.customerName = user.user?.name.toString()
        bodyUPIOrder.customerEmail = user.user?.email.toString()
        bodyUPIOrder.customerMobile = user.user?.mobile.toString()
        bodyUPIOrder.redirectUrl = "https://gsfinance.in/"
        bodyUPIOrder.udf1 = "Android-CreateOrder"
        bodyUPIOrder.udf2 = "Android-CreateOrder"
        bodyUPIOrder.udf3 = "Android-CreateOrder"
        val url = "https://api.ekqr.in/api/create_order"
        mainViewModel.createUpiOrder(url,bodyUPIOrder)
    }
    private fun attachObservers() {
        mainViewModel.createUpiOrderResponse.observe(this, Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {hideLoadingDialog()},
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){

                    // load the webview with the given payment url
                    showSnackbar("UPI_ID_HASH : ${it.data?.upiIdHash.toString()}")

//                    val chromeTabActivity = Intent(this@UpiViewActivity,ChromeTabActivity::class.java)
//                    chromeTabActivity.putExtra("URL",it.data?.paymentUrl.toString())
//                    startActivity(chromeTabActivity)
                    binding.webViewPay.loadUrl(it.data?.paymentUrl.toString())
                    hookUPIStatus()
                }else{
                    showCustomDialog("Oops Something went wrong ! Message from server : ${it.msg.toString()}","Error !")
                }

            }


        ))

        mainViewModel.forceCloseResponse.observe(this,Event.EventObserver(
            onLoading = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){
                    showToast(it.message.toString())
                    val intentEmi = Intent(this@UpiViewActivity, DashboardActivity::class.java)
                    intentEmi.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intentEmi)
                }
            },
            onError = {
                hideLoadingDialog()
            }
        ))
        mainViewModel.checkUPIOrderStatus.observe(this,Event.EventObserver(
            onLoading = {
                hideLoadingDialog()
            },
            onSuccess = {
                        hideLoadingDialog()
                if(it.status==true){
                    if(it.data?.status.equals("success")){
                        showCustomDialog("Info","UPI PAYMENT IS RECEIVED !")
                        callPayEmiApi()
                    }else if(it.data?.status.equals("scanning")){
                        Log.i("UPI","SCANNING")
                    }
                }else{
                    Log.i("UPI","SCANNING-FALSE-STATUS")
                }

            },
            onError = {
                hideLoadingDialog()
            }



        ))
        mainViewModel.payEmiResponse.observe(this,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){
                    //{"status":true,"message":"Loan closed Successfully."}
                    if (it.message.equals("Loan closed Successfully.")){

                        showToast(it.message.toString())

                        val intentEmi = Intent(this@UpiViewActivity, DashboardActivity::class.java)
                        intentEmi.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intentEmi)
                    }else{
                        val intentEmi = Intent(this@UpiViewActivity, LoanEmiActivity::class.java)
                        intentEmi.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intentEmi)
                    }



                }

            }


        ))




    }
    private fun callPayEmiApi(){


        val payEmiMap = hashMapOf<String,String>()
        payEmiMap.put("loan_id",intent.getStringExtra("LOAN_ID").toString())
        payEmiMap.put("id",intent.getStringExtra("LOAN_ID").toString())
        if(intent.getStringExtra("EMI_ID").toString().isNotEmpty()){
            payEmiMap.put("emi_id",intent.getStringExtra("EMI_ID").toString())
            payEmiMap.put("payment_method","UPI")
            payEmiMap.put("txn_id",client_txn_id)
            payEmiMap.put("status","success")
            mainViewModel.payEmi(payEmiMap)

        }else{
            payEmiMap.put("payment_method","UPI")
            payEmiMap.put("txn_id",client_txn_id)
            payEmiMap.put("status","success")
            mainViewModel.forceCloseLOan(payEmiMap)

        }









    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webViewPay.settings.javaScriptEnabled = true
        binding.webViewPay.settings.loadWithOverviewMode = true
        binding.webViewPay.settings.setSupportMultipleWindows(true)
        // Do not change Useragent otherwise it will not work. even if not working uncommit below
//         binding.webViewPay.settings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.135 Mobile Safari/537.36");
        binding.webViewPay.webChromeClient = WebChromeClient()
//        binding.webViewPay.addJavascriptInterface(WebviewInterface(this), "window.Interface")
        binding.webViewPay.addJavascriptInterface(WebviewInterface(this), "Interface")
    }
    class WebviewInterface(var activity: Activity) {


        fun showCustomDialogWebView(title: String, message: String) {
            // Show a custom dialog with an OK button
            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }

        @JavascriptInterface
        fun paymentResponse(client_txn_id: String, txn_id: String) {

            // Close the Webview.
            showCustomDialogWebView("UPI-GATEWAY","Client Transaction ID : ${client_txn_id} and Transaction ID : ${txn_id}")




        }

        @JavascriptInterface
        fun errorResponse() {
            // this function is called when Transaction in Already Done or Any other Issue.
            // Close the Webview.
            showCustomDialogWebView("UPI-GATEWAY","Oops something went wrong !")

        }

        //CREATED NEW INTERFACE.
        @JavascriptInterface
        fun redirect_user(){
            activity.finish()
        }
    }




}