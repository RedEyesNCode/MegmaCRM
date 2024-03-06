package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.redeyesncode.crmfinancegs.data.KycDetails
import com.redeyesncode.crmfinancegs.databinding.ActivityLoanDetailsBinding
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant

class LoanDetailsActivity : BaseActivity() {



    


    lateinit var binding: ActivityLoanDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoanDetailsBinding.inflate(layoutInflater)

        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {


        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnOtpContinue.setOnClickListener {
            if(isValidated()){
                val intentUploadDocs = Intent(this@LoanDetailsActivity, UploadDocumentActivity::class.java)
                startActivity(intentUploadDocs)
            }

        }

        binding.edtState.setOnClickListener {


            showOptionsStateDialog(this@LoanDetailsActivity)

        }






//        binding.edtLoanPurpose.setOnClickListener {
//            var loanPurpose = arrayListOf<String>()
//            loanPurpose.add("1. Debt Consolidation")
//            loanPurpose.add("2. Home Improvements")
//            loanPurpose.add("3. Moving Expenses")
//            loanPurpose.add("3. Moving Expenses")
//            loanPurpose.add("4. Medical Expenses")
//            loanPurpose.add("5. Large Purchase")
//            loanPurpose.add("6. Wedding Expenses")
//            showOptionsDialog(this@LoanDetailsActivity,loanPurpose,binding.edtLoanPurpose)
//
//
//        }




    }

    fun isValidated():Boolean{

        if(binding.edtPincode.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter pincode")
            return false
        }else if(binding.edtRelativeName.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter relative number")

            return false

        }else if(binding.edtRelativeNumber.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter relative number")

            return false
        }else if(binding.edtRelativeNameTwo.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter relative name two")
            return false

        }else if(binding.edtRelativeNumberTwo.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter relative number two")
            return false


        }else if(binding.edtCurrentAdd.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter current address")
            return false

        }else if(binding.edtState.text.toString().isEmpty()){

            showCustomDialog("Info","Please select state")
            return false
        } else{
            val kycDetails = AppSession(this@LoanDetailsActivity).getObject(
                Constant.BODY_UPDATE_KYC,
                KycDetails::class.java) as KycDetails
            kycDetails.pincode = binding.edtPincode.text.toString()
            kycDetails.relativeNumber = binding.edtRelativeNumber.text.toString()
            kycDetails.relativeName = binding.edtRelativeName.text.toString()
            kycDetails.relativeNumber2 = binding.edtRelativeNumberTwo.text.toString()
            kycDetails.relativeNameTwo = binding.edtRelativeNameTwo.text.toString()
            kycDetails.currentAddress = binding.edtCurrentAdd.text.toString()
            kycDetails.state = binding.edtState.text.toString()

            AppSession(this@LoanDetailsActivity).putObject(Constant.BODY_UPDATE_KYC,kycDetails)


            return true
        }



    }

    fun showOptionsDialog(context: Context, options: ArrayList<String>, editText: EditText) {
        val optionsArray = options.toTypedArray()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose an option")
            .setItems(optionsArray) { dialog, which ->
                val selectedOption = optionsArray[which]
                editText.setText(selectedOption)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    data class State(val code: String, val name: String)

    // Function to show the options dialog
    fun showOptionsStateDialog(context: Context) {
        val states = listOf(
            State("AP", "Andhra Pradesh"), State("AR", "Arunachal Pradesh"),
            State("AS", "Assam"), State("BR", "Bihar"), State("CG", "Chhattisgarh"),
            State("GA", "Goa"), State("GJ", "Gujarat"), State("HR", "Haryana"),
            State("HP", "Himachal Pradesh"), State("JK", "Jammu and Kashmir"),
            State("JH", "Jharkhand"), State("KA", "Karnataka"), State("KL", "Kerala"),
            State("MP", "Madhya Pradesh"), State("MH", "Maharashtra"), State("MN", "Manipur"),
            State("ML", "Meghalaya"), State("MZ", "Mizoram"), State("NL", "Nagaland"),
            State("OD", "Odisha"), State("PB", "Punjab"), State("RJ", "Rajasthan"),
            State("SK", "Sikkim"), State("TN", "Tamil Nadu"), State("TS", "Telangana"),
            State("TR", "Tripura"), State("UK", "Uttarakhand"), State("UP", "Uttar Pradesh"),
            State("WB", "West Bengal"), State("AN", "Andaman and Nicobar Islands"),
            State("CH", "Chandigarh"), State("DN", "Dadra and Nagar Haveli and Daman and Diu"),
            State("DL", "Delhi"), State("LD", "Lakshadweep"), State("PY", "Puducherry")
        )

        val stateNames = states.map { it.name }.toTypedArray()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select State")
        builder.setItems(stateNames, DialogInterface.OnClickListener { dialog, which ->
            // Handle the selected option here
            val selectedState = states[which]
            // Do something with the selected state code and name
            val selectedCode = selectedState.code
            val selectedName = selectedState.name

            binding.edtState.setText(selectedCode)

            // Example: Log the selected state code and name
            println("Selected State: Code - $selectedCode, Name - $selectedName")
        })

        val dialog = builder.create()
        dialog.show()
    }
}