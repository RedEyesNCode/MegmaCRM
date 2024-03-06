package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.gson.Gson
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityEmandateBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class EMandateActivity : BaseActivity() {
    lateinit var binding: ActivityEmandateBinding


    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    var bankId:String?=null

    var loanAmount:String?=null

    var loanId:String?=null
    val urlEMandateStatus = "https://megmagroup.loan/Api2/eStatus.php?user_id="
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())


    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }


    private fun  hookEMandateStateApi(){
        val user = AppSession(this@EMandateActivity).getObject(Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse

        val map = hashMapOf<String, String>()
        map["user_id"] = user?.user?.id.toString()

        //&loan_id=1122

        loanId = AppSession(this@EMandateActivity).getString(Constant.USER_LOAN_ID)

        mainViewModel.launchEmandateStatus(urlEMandateStatus+user?.user?.id.toString()+"&loan_id=${loanId}", map)
        coroutineScope.launch {
            while (true) {

                val map = hashMapOf<String, String>()
                map["user_id"] = user?.user?.id.toString()

                mainViewModel.launchEmandateStatus(urlEMandateStatus+user?.user?.id.toString()+"&loan_id=${loanId}", map)
                // Delay for 6 seconds before making the next API call
                delay(6000L)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityEmandateBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectEMandateActivity(this@EMandateActivity)

        initClicks()
        attachObservers()
        hookEMandateStateApi()
        setContentView(binding.root)
    }
    fun extractBankId(inputString: String): String? {
        // Split the input string by colon
        val parts = inputString.split(":")

        // Check if there are at least two parts (bank name and bank ID)
        if (parts.size >= 2) {
            // Return the second part, which is the bank ID
            return parts[1].trim()
        } else {
            // Return null if there is no bank ID
            return null
        }
    }
    private fun initClicks() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnCheckEmandateStatus.setOnClickListener {
            val user = AppSession(this@EMandateActivity).getObject(Constant.USER_LOGIN,
                LoginResponse::class.java) as LoginResponse

            val userId= hashMapOf<String,String>()
            userId.put("user_id",user.user?.id.toString())
            mainViewModel.launchEmandateStatus(urlEMandateStatus,userId)

        }
        bankId = intent.getStringExtra("BANK_ID")
        loanAmount = intent.getStringExtra("LOAN_AMOUNT")
        loanId = intent.getStringExtra("LOAN_ID")


        binding.cardLayout.setOnClickListener {

            bankId = intent.getStringExtra("BANK_ID")
            loanAmount = intent.getStringExtra("LOAN_AMOUNT")
            loanId = AppSession(this@EMandateActivity).getString(Constant.USER_LOAN_ID)

//            showCustomDialog("INFO","LOAN-ID ${loanId} & LOAN-AMOUNT ${loanAmount}")



            val user = AppSession(this@EMandateActivity).getObject(Constant.USER_LOGIN,
                LoginResponse::class.java) as LoginResponse

            val url = "https://megmagroup.loan/Api2/enach.php?userid=${user.user?.id.toString()}&bankid=${extractBankId(bankId!!)}&loan=${loanAmount}&loan_id=${loanId}"
//
            mainViewModel.getEMandateResponse(url)


        }

        binding.cardLayoutProcessing.setOnClickListener {
            binding.cardLayout.performClick()

        }

    }



    private fun attachObservers() {

        mainViewModel.eMandateStatusResponse.observe(this,Event.EventObserver(
            onLoading = {
                hideLoadingDialog()

            },
            onError = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()

                val loanId = AppSession(this@EMandateActivity).getString(Constant.USER_LOAN_ID)

                val response = Gson().toJson(it)

                Log.i("E-MANDATE-STATUS",response)
                Log.i("E-MANDATE-STATUS",response)
                Log.i("E-MANDATE-STATUS",response)
                Log.i("E-MANDATE-STATUS",response)

                val html: String = it.string()
                Log.d("EMANDATE", " myHTMLResponseCallback : $html")

                val document: Document = Jsoup.parse(html)
                val jsonElement: Element? = document.selectFirst("script:containsData({\"status\":)")

                val jsonPayload: String? = jsonElement?.data()

                val regex = """\{.*?"status"\s*:\s*"(true|false)".*?\}""".toRegex()

// Find the first match in the HTML string
                val matchResult = regex.find(html)

// Extract the matched JSON part
                val jsonPart = matchResult?.value

// Extract the status directly from the JSON part
                val status: String? = jsonPart?.substringAfter("\"status\":\"")?.substringBefore("\"")


                if (status != null) {
                    if (status == "true") {
                        showSnackbar("E-Mandate is Completed. Loan will be disbursed soon.")

                        Handler().postDelayed(Runnable {
                            val intentApproved = Intent(this@EMandateActivity, ApproveActivity::class.java)
                            intentApproved.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            intentApproved.putExtra("STATUS","Your Loan has been approved ! It will be disbursed shortly")
                            startActivity(intentApproved)

                        },2000)

                        binding.tvEMandateStatus.setText("Completed.")

                    } else {
                        // Handle false status

                        Log.i("EMANDATE","FALSE EMANDATE NOT COMPLETED YET.")


                        binding.tvEMandateStatus.setText("Not Completed yet.")

                    }
                }
//
//                val jsonData = document.select("body").text()
//                val jsonObject = Gson().fromJson(jsonData,EMandateHTMLResponse::class.java)
//                Log.i("E-MANDATE-STATUS-GSON",jsonObject.toString())
//



            }


        ))


        mainViewModel.eMandateResponse.observe(this, Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                try {
                    val html: String = it.string()
                    Log.d("EMANDATE", " myHTMLResponseCallback : $html")

                    val document: Document = Jsoup.parse(html)

                    // Select the script tag containing location.replace statement
                    val scriptElement: Element = document.select("script:containsData(location.replace)").first()

                    // Extract the URL from the script content
                    val url: String? = extractUrlFromScript(scriptElement.data())
                    val url2: String? = extractUrlFromHtml(scriptElement.data())

                    if (url != null) {
                        Log.d("EMANDATE", "Extracted URL: $url")
                        // Now you can use the extracted URL as needed
//                        val httpIntent = Intent(Intent.ACTION_VIEW)
//                        httpIntent.setData(Uri.parse(url))
//
//                        startActivity(httpIntent)

                        val intentApplyLoan = Intent(this@EMandateActivity, ChromeTabActivity::class.java)
                        intentApplyLoan.putExtra("URL",url)
                        startActivity(intentApplyLoan)

                    } else if (url2!=null) {
                        val intentApplyLoan = Intent(this@EMandateActivity, ChromeTabActivity::class.java)
                        intentApplyLoan.putExtra("URL",url2)
                        startActivity(intentApplyLoan)


                    }else{
                        Log.d("EMANDATE", "URL not found in script")
                        showCustomDialog("Error !","REDIRECT URL NOT FOUND !")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            },
            onError = {
                hideLoadingDialog()
                showToast(it)
            }



        ))


    }

    fun extractUrlFromHtml(htmlString: String): String? {
        // Define the regex pattern to find URLs
        val regex = """(?i)\b((?:https?://|www\d{0,3}[.]|[a-z0-9.\-]+[.][a-z]{2,4}/)(?:[^\s()<>]+|\(([^\s()<>]+|(\([^\s()<>]+\)))*\))+(?:\(([^\s()<>]+|(\([^\s()<>]+\)))*\)|[^\s`!()\[\]{};:'".,<>?«»“”‘’]))""".toRegex()

        // Find the first match in the HTML string
        val matchResult = regex.find(htmlString)

        // Return the matched URL or null if not found
        return matchResult?.value
    }

    private fun extractUrlFromScript(scriptContent: String): String? {
        val urlStartIndex = scriptContent.indexOf("\"") + 1
        val urlEndIndex = scriptContent.indexOf("\"", urlStartIndex)
        return if (urlStartIndex != -1 && urlEndIndex != -1) {
            scriptContent.substring(urlStartIndex, urlEndIndex)
        } else {
            null
        }
    }

}