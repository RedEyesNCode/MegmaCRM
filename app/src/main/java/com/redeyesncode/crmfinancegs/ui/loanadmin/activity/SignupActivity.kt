package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivitySignupBinding
import com.redeyesncode.crmfinancegs.ui.activity.LoginActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import javax.inject.Inject

class SignupActivity : BaseActivity() {


    lateinit var binding: ActivitySignupBinding
    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySignupBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectSignupActivity(this@SignupActivity)
        attachObservers()

        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.layoutTermsnConditons.setOnClickListener {
            if(binding.ivCheckTerms.drawable.toBitmap().sameAs(getDrawable(R.drawable.ic_check_green)?.toBitmap())){
                binding.ivCheckTerms.setImageDrawable(getDrawable(R.drawable.bcakground_green_uncheck))
            }else if(binding.ivCheckTerms.drawable.toBitmap().sameAs(getDrawable(R.drawable.bcakground_green_uncheck)?.toBitmap())){
                binding.ivCheckTerms.setImageDrawable(getDrawable(R.drawable.ic_check_green))

            }

        }
        binding.btnOtpContinue.setOnClickListener {
            if(isValidated()){
                val signupMap = hashMapOf<String,String>()
                signupMap.put("mobile",binding.edtMobileNumber.text.toString())
                signupMap.put("email",binding.edtEmail.text.toString())
                mainViewModel.registerUser(signupMap)
            }



        }

    }
    private fun isValidated():Boolean{
        if(binding.edtEmail.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter email")
            return false

        }else if(binding.edtMobileNumber.text.toString().isEmpty()){
            showCustomDialog("Info","Please enter mobile number")
            return false

        }else if(!binding.ivCheckTerms.drawable.toBitmap().sameAs(getDrawable(R.drawable.ic_check_green)?.toBitmap())){

            showCustomDialog("Info","Please accept terms and conditions")

            return false
        } else{
            return true
        }



    }
    private fun attachObservers() {
        mainViewModel.registerUserResponse.observe(this, Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {
                val loginResponse = it
                hideLoadingDialog()
                if(it.status==true){
                    showToast("Register Success")
                    val intentLoginActivity = Intent(this@SignupActivity, LoginActivity::class.java)
                    intentLoginActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intentLoginActivity)

                }else{
                    // account not found

                    showToast(it.message.toString())

                }

            },
            onError = {
                hideLoadingDialog()
                showCustomDialog("ERROR !",it.toString())
            }


        ))


    }

}