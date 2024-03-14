package com.redeyesncode.crmfinancegs.ui.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityEmandateCrmactivityBinding
import com.redeyesncode.crmfinancegs.databinding.SearchBankDialogBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject
import kotlin.random.Random

class EMandateCRMActivity : BaseActivity() {

    lateinit var binding:ActivityEmandateCrmactivityBinding
    var bankAccounts = arrayListOf<String>()
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

        val map = hashMapOf<String, String>()
        map["user_id"] = binding.edtUserId.text.toString()

        //&loan_id=1122

        loanId = AppSession(this@EMandateCRMActivity).getString(Constant.USER_LOAN_ID)

        mainViewModel.launchEmandateStatus(urlEMandateStatus+binding.edtUserId.text.toString()+"&loan_id=${loanId}", map)
        coroutineScope.launch {
            while (true) {

                val map = hashMapOf<String, String>()
                map["user_id"] = binding.edtUserId.text.toString()

                mainViewModel.launchEmandateStatus(urlEMandateStatus+binding.edtUserId.text.toString()+"&loan_id=${loanId}", map)
                // Delay for 6 seconds before making the next API call
                delay(6000L)
            }
        }
    }

    @Inject
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmandateCrmactivityBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectEmandateCRM(this@EMandateCRMActivity)
        initClicks()
        attachObservers()

        showCustomDialog("EMANDATE","EMandate-Status Api is Called Every 6 SECONDS")
        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.btnBack.setOnClickListener {
            finish()

        }
        getBankList()

        binding.edtSelectBank.setOnClickListener {
            showStringListDialog(bankAccounts)
        }
        binding.edtLoanId.setText(generateNumber(10,"111"))
        binding.edtUserId.setText(generateNumber(10,"777"))

        binding.btnLogin.setOnClickListener {
            if(binding.edtSelectBank.text.toString().isEmpty()){
                showSnackbar("Please select bank")
            }else if(binding.edtLoanAmount.text.toString().isEmpty()){

                showSnackbar("Please enter amount")

            }else{
                val url = "https://megmagroup.loan/Api2/enach.php?userid=${binding.edtUserId.text.toString()}&bankid=${extractBankId(binding.edtSelectBank.text.toString())}&loan=${binding.edtLoanAmount.text.toString()}&loan_id=${binding.edtLoanId.text.toString()}"
                val userId= hashMapOf<String,String>()
                userId.put("user_id",binding.edtUserId.text.toString())
                mainViewModel.launchEmandateStatus(url,userId)
            }

        }


    }
    private fun attachObservers() {

        mainViewModel.eMandateStatusResponse.observe(this, Event.EventObserver(
            onLoading = {
                hideLoadingDialog()

            },
            onError = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()

                val loanId = AppSession(this@EMandateCRMActivity).getString(Constant.USER_LOAN_ID)

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




                    } else {
                        // Handle false status

                        showSnackbar("FALSE EMANDATE NOT COMPLETED YET.")



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
                hideLoadingDialog()
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

                        val intentApplyLoan = Intent(this@EMandateCRMActivity, ChromeTabActivity::class.java)
                        intentApplyLoan.putExtra("URL",url)
                        startActivity(intentApplyLoan)

                    } else if (url2!=null) {
                        val intentApplyLoan = Intent(this@EMandateCRMActivity, ChromeTabActivity::class.java)
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
    fun generateNumber(length: Int, prefix: String): String {
        require(length >= prefix.length) { "Length must be greater than or equal to the length of the prefix." }

        val randomNumberLength = length - prefix.length
        val randomNumber = buildString {
            repeat(randomNumberLength) {
                append(Random.nextInt(10))
            }
        }
        return prefix + randomNumber
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


        val dialogBinding = SearchBankDialogBinding.inflate(LayoutInflater.from(this@EMandateCRMActivity))


        val adapter = ArrayAdapter(this@EMandateCRMActivity, android.R.layout.simple_list_item_1, items)
        dialogBinding.listView.adapter = adapter
        val dialog = AlertDialog.Builder(this@EMandateCRMActivity)
            .setView(dialogBinding.root)
            .setNegativeButton("Cancel") { dialog, which ->

                dialog.dismiss()

            }
            .create()
        dialog.show()
        // Set up item click listener
        dialogBinding.listView.setOnItemClickListener { _, _, which, _ ->
            val selectedItem = stringList[which]
            binding.edtSelectBank.setText(selectedItem)

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