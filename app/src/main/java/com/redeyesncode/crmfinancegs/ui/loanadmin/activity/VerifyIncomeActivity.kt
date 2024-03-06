package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.redeyesncode.crmfinancegs.data.KycDetails
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityVerifyIncomeBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class VerifyIncomeActivity : BaseActivity() {

    lateinit var binding: ActivityVerifyIncomeBinding
    val PICK_PDF_REQUEST_CODE = 123
    var bankStatement:File?=null

    @Inject
    lateinit var mainViewModel:MainViewModelLoanAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityVerifyIncomeBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectVerifyBank(this@VerifyIncomeActivity)


        initClicks()
        attachObservers()

        setContentView(binding.root)
    }

    private fun initClicks() {

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnSubmitKycRequest.setOnClickListener {

            // resubmit the request if esign or bankstatement is there
            val eSign = intent.getSerializableExtra("FILE_SIGN") as File
            val aadharBack = intent.getSerializableExtra("ADHAR_BACK") as File
            val aadharFront = intent.getSerializableExtra("ADHAR_FRONT") as File
            val pancard = intent.getSerializableExtra("SELFIE") as File
            val selfie = intent.getSerializableExtra("FILE_SIGN") as File



            if (bankStatement!=null || eSign!=null){

                submitKycRequest()

            }else{
                val intentKycRequest = Intent(this@VerifyIncomeActivity, PendingActivity::class.java)
                intentKycRequest.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentKycRequest)

            }








        }

        binding.cardNetBanking.setOnClickListener {

            showCustomDialog("Info","Under-Development !")


        }
        binding.cardBankStatement.setOnClickListener {


            pickPdfFile(this@VerifyIncomeActivity)

        }

    }
    private fun attachObservers() {
        mainViewModel.submitKycRequest.observe(this, Event.EventObserver(

            onLoading = {
                showLoadingDialog()

            },
            onError = {
                hideLoadingDialog()
                showCustomDialog("Error !","REQUEST NOT SUBMITTED ${it}")
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){
                    val intentKycRequest = Intent(this@VerifyIncomeActivity, PendingActivity::class.java)
                    intentKycRequest.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentKycRequest)


                }else{
                    showToast(it.message.toString())
                }
            }


        ))




    }
    fun removeMinusSign(inputString: String): String {
        return inputString.replace("-", "")
    }


    fun submitKycRequest(){


        val kycDetails = AppSession(this@VerifyIncomeActivity).getObject(
            Constant.BODY_UPDATE_KYC,
            KycDetails::class.java) as KycDetails
//                    kycDetails.adhar_number = binding.aadharnumber.text.toString().uppercase()

        AppSession(this@VerifyIncomeActivity).putObject(Constant.BODY_UPDATE_KYC,kycDetails)

        val eSign = intent.getSerializableExtra("FILE_SIGN") as File
        val aadharBack = intent.getSerializableExtra("ADHAR_BACK") as File
        val aadharFront = intent.getSerializableExtra("ADHAR_FRONT") as File
        val pancard = intent.getSerializableExtra("SELFIE") as File
        val selfie = intent.getSerializableExtra("FILE_SIGN") as File


        Log.i("KYC_DETAILS", Gson().toJson(kycDetails))
        Log.i("KYC_DETAILS", Gson().toJson(kycDetails))
        Log.i("KYC_DETAILS", Gson().toJson(kycDetails))
        Log.i("KYC_DETAILS", Gson().toJson(kycDetails))

        showToast("Submitting KYC REQUEST")
        val user = AppSession(this@VerifyIncomeActivity).getObject(
            Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse
        mainViewModel.submitKycRequestUser(aadharFront!!, aadharBack!!,pancard!!,
            selfie!!,eSign,bankStatement,kycDetails,user.user?.id.toString())



    }
    fun pickPdfFile(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"

        try {
            startActivityForResult(intent, PICK_PDF_REQUEST_CODE, null)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(activity, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPdfUri: Uri = data.data!!

            // Now you can use the selectedPdfUri to get the selected PDF file
            // Example: val selectedPdfFile = File(selectedPdfUri.path)
            bankStatement = getFile(this@VerifyIncomeActivity,selectedPdfUri)
            showToast(bankStatement!!.path.toString())
        }else{
            showCustomDialog("Info","File Not Selected")
        }
    }



    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri): File? {
        return try {
            val destinationFilename =
                File(context.filesDir.path + File.separatorChar + queryName(context, uri))
            try {
                context.contentResolver.openInputStream(uri).use { ins ->
                    createFileFromStream(
                        ins!!,
                        destinationFilename
                    )
                }
            } catch (ex: Exception) {
                Log.e("Save File", ex.message!!)
                ex.printStackTrace()
            }
            destinationFilename
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            var name: String? = null
            if (uri.scheme == "content") {
                cursor = context.contentResolver.query(uri, null, null, null, null)
                assert(cursor != null)
                val nameIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                name = cursor.getString(nameIndex)
            }
            if (name == null) {
                name = uri.path
                val cut = name!!.lastIndexOf('/')
                if (cut != -1) {
                    name = name.substring(cut + 1)
                }
            }
            name
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        } finally {
            cursor?.close()
        }
    }

}