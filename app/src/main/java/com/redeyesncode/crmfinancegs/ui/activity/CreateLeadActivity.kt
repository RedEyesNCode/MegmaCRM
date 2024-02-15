package com.redeyesncode.crmfinancegs.ui.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.databinding.ActivityCreateLeadBinding
import com.redeyesncode.gsfinancenbfc.base.BaseActivity
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.util.Calendar

class CreateLeadActivity : BaseActivity() {


    lateinit var binding:ActivityCreateLeadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateLeadBinding.inflate(layoutInflater)
        initClicks()


        setContentView(binding.root)
    }

    private fun initClicks() {

        binding.ivBack.setOnClickListener {
            finish()

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
            showOptionsDialog(this@CreateLeadActivity,education,binding.edtOccupation)
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
            showOptionsDialog(this@CreateLeadActivity,education,binding.edtMonthlyIncome)

        }

        binding.edtGender.setOnClickListener {
            var gender = arrayListOf<String>()
            gender.add("Male")
            gender.add("Female")
            gender.add("Transgender")
            showOptionsDialog(this@CreateLeadActivity,gender,binding.edtGender)

        }
        binding.edtUserType.setOnClickListener {
            val userType = arrayListOf<String>()
            userType.add("Salaried")
            userType.add("Employed Professional")
            userType.add("Self Employed")
            userType.add("Others")
            showOptionsDialog(this@CreateLeadActivity,userType,binding.edtUserType)


        }

        binding.btnOtpContinue.setOnClickListener {

            if(isValidated()){
                val intentCreateLead = Intent(this@CreateLeadActivity,LeadDocumentActivity::class.java)
                startActivity(intentCreateLead)



            }
        }


        binding.edtDOB.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val dialog = DatePickerDialog(this@CreateLeadActivity,
                { datePicker, i, i1, i2 ->
                    val dates = i2.toString() + "-" + (i1 + 1) + "-" + i
                    binding.edtDOB.setText(dates)
                }, year, month, day
            )
            dialog.show()

        }

    }
    private fun isValidated():Boolean {
        if (binding.edtFirstName.text.toString().isEmpty()) {
            showCustomDialog("Info", "Please enter first name")
            return false
        } else if (binding.edtLastName.text.toString().isEmpty()) {
            showCustomDialog("Info", "Please enter last name")
            return false

        } else if (binding.edtDOB.text.toString().isEmpty()) {
            showCustomDialog("Info", "Please select date of birth")
            return false

        } else if (binding.edtEmail.text.toString().isEmpty()) {
            showCustomDialog("Info", "Please enter email")
            return false
        } else if (binding.edtOccupation.text.toString().isEmpty()) {
            showCustomDialog("Info", "Please select occupation")
            return false
        } else if (binding.edtGender.text.toString().isEmpty()) {
            showCustomDialog("Info", "Please select gender")

            return false
        } else if (binding.edtMonthlyIncome.text.toString().isEmpty()) {

            showCustomDialog("Info", "Please select monthly income")

            return false
        } else if (binding.edtUserType.text.toString().isEmpty()) {
            showCustomDialog("Info", "Please enter user type")
            return false
        } else if(binding.edtAddress.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter address")
            return false
        }else if(binding.edtState.text.toString().isEmpty()){

            showCustomDialog("Info","Please select state")
            return false
        }else if (binding.edtPincode.text.toString().isEmpty()){

            showCustomDialog("Info","Please enter pincode")
            return false
        }else if(binding.edtRelativeOne.text.toString().isEmpty()){
            showCustomDialog("Info","Plesae enter relative one name")
            return false
        }else if(binding.edtRelativeTwo.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter relative two name")
            return false

        }else if(binding.edtRelativeOneNumber.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter relative one number")

            return false
        }else if(binding.edtRelativeTwoNumber.text.toString().isEmpty()){
            showCustomDialog("Info","please enter relative two number")
            return false
        } else {
            val bodyCreateLead = BodyCreateLead()
            bodyCreateLead.firstname = binding.edtFirstName.text.toString()
            bodyCreateLead.lastname = binding.edtLastName.text.toString()
            bodyCreateLead.middlename = binding.edtMiddleName.text.toString()
            bodyCreateLead.dob = binding.edtDOB.text.toString()
            bodyCreateLead.email = binding.edtEmail.text.toString()
            bodyCreateLead.occupation = binding.edtOccupation.text.toString()
            if (binding.edtGender.text.toString().equals("Female")) {
                bodyCreateLead.gender = "1"
            } else if (binding.edtGender.text.toString().equals("Male")) {
                bodyCreateLead.gender = "2"
            } else {
                bodyCreateLead.gender = "3"
            }




            bodyCreateLead.userType = binding.edtUserType.text.toString()
            bodyCreateLead.monthlySalary = "100000"
            bodyCreateLead.gender = binding.edtGender.text.toString()
            bodyCreateLead.currentAddress = binding.edtAddress.text.toString()
            bodyCreateLead.relativeName = binding.edtRelativeOne.text.toString()
            bodyCreateLead.relativeNumber = binding.edtRelativeOneNumber.text.toString()

            bodyCreateLead.state = binding.edtState.text.toString()
            bodyCreateLead.pincode = binding.edtPincode.text.toString()

            AppSession(this@CreateLeadActivity).putObject(Constant.BODY_CREATE_LEAD, bodyCreateLead)




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
}