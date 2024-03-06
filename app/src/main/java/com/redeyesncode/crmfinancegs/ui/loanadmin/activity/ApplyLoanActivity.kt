package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.redeyesncode.crmfinancegs.data.KycDetails
import com.redeyesncode.crmfinancegs.databinding.ActivityApplyLoanBinding
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.util.Calendar

class ApplyLoanActivity : BaseActivity() {
    lateinit var binding: ActivityApplyLoanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityApplyLoanBinding.inflate(layoutInflater)


       initClicks()
        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnOtpContinue.setOnClickListener {

            if(isValidated()){
                val intentRejected = Intent(this@ApplyLoanActivity, LoanDetailsActivity::class.java)
                startActivity(intentRejected)
            }

        }

        binding.edtOccupation.setOnClickListener {
            var education = arrayListOf<String>()
            education.add("Employed")
            education.add("Self-employed")
            education.add("Out of work")
            education.add("Homemaker")
            education.add("Student")
            education.add("Retired")
            education.add("Unable to work")
            showOptionsDialog(this@ApplyLoanActivity,education,binding.edtOccupation)
        }




        binding.edtMonthlyIncome.setOnClickListener {
            var education = arrayListOf<String>()
            education.add("Less than ₹20,000")
            education.add("₹40,000 - ₹50,000")
            education.add("₹60,000 - ₹70,000")
            education.add("₹90,000 - ₹1,00,000")
            education.add("₹1,00,000 - ₹1,50,000")
            education.add("₹2,00,000 - ₹3,00,000")
            education.add("₹4,00,000 - ₹5,00,000")
            showOptionsDialog(this@ApplyLoanActivity,education,binding.edtMonthlyIncome)

        }

        binding.edtGender.setOnClickListener {
            var gender = arrayListOf<String>()
            gender.add("Male")
            gender.add("Female")
            gender.add("Transgender")
            showOptionsDialog(this@ApplyLoanActivity,gender,binding.edtGender)

        }
        binding.edtUserType.setOnClickListener {
            val userType = arrayListOf<String>()
            userType.add("Salaried")
            userType.add("Employed Professional")
            userType.add("Self Employed")
            userType.add("Others")
            showOptionsDialog(this@ApplyLoanActivity,userType,binding.edtUserType)


        }


        binding.edtDOB.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val dialog = DatePickerDialog(this@ApplyLoanActivity,
                { datePicker, i, i1, i2 ->
                    val dates = i2.toString() + "-" + (i1 + 1) + "-" + i
                    binding.edtDOB.setText(dates)
                }, year, month, day
            )
            dialog.show()

        }



    }
    private fun isValidated():Boolean{
        if(binding.edtFirstName.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter first name")
            return false
        }else if(binding.edtLastName.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter last name")
            return false

        }else if(binding.edtDOB.text.toString().isEmpty()){
            showCustomDialog("Info","Please select date of birth")
            return false

        }else if(binding.edtEmail.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter email")
            return false
        }else if(binding.edtOccupation.text.toString().isEmpty()){
            showCustomDialog("Info","Please select occupation")
            return false
        }else if(binding.edtGender.text.toString().isEmpty()){
            showCustomDialog("Info","Please select gender")

            return false
        } else if(binding.edtMonthlyIncome.text.toString().isEmpty()){

            showCustomDialog("Info","Please select monthly income")

            return false
        }else if(binding.edtUserType.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter user type")
            return false
        }

        else{
            val kycDetails = KycDetails()
            kycDetails.firstName = binding.edtFirstName.text.toString()
            kycDetails.lastName = binding.edtLastName.text.toString()
            kycDetails.middleName = binding.edtMiddleName.text.toString()
            kycDetails.dob = binding.edtDOB.text.toString()
            kycDetails.email = binding.edtEmail.text.toString()
            kycDetails.occupation = binding.edtOccupation.text.toString()
            if(binding.edtGender.text.toString().equals("Female")){
                kycDetails.gender = "1"
            }else if(binding.edtGender.text.toString().equals("Male")){
                kycDetails.gender = "2"
            }else{
                kycDetails.gender = "3"
            }




            kycDetails.user_type = binding.edtUserType.text.toString()
            kycDetails.monthlySalary = "100000"
            AppSession(this@ApplyLoanActivity).putObject(Constant.BODY_UPDATE_KYC,kycDetails)




            return true
        }



    }
    private fun parseIncomeRange(selectedItem: String): Any {
        val regex = Regex("[0-9]+")
        val matchResult = regex.findAll(selectedItem)
        val values = matchResult.map { it.value.toInt() }.toList()

        return if (values.size == 1) {
            values[0]
        } else {
            Pair(values[0], values[1])
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
}