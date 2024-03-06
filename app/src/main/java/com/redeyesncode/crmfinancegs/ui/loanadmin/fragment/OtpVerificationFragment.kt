package com.redeyesncode.crmfinancegs.ui.loanadmin.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.databinding.FragmentOtpVerificationBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.AppPermissionActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.DashboardActivity
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class OtpVerificationFragment(var correctOtp:String,var userNumber :String) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentOtpVerificationBinding

    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentOtpVerificationBinding.inflate(inflater,container,false)

        (requireActivity().application as AndroidApp).getDaggerComponent().injectOtpFragment(this@OtpVerificationFragment)

        binding.btnOtpContinue.setOnClickListener {
            if(binding.edtOTP.length()==6 && binding.edtOTP.text.toString().equals(correctOtp)){
                // verify otp
                val intentPermission = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intentPermission)
            }else if(binding.edtOTP.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Enter OTP",Toast.LENGTH_LONG).show()
            }else if(!binding.edtOTP.text.toString().equals(correctOtp)){
                Toast.makeText(requireContext(),"Wrong OTP",Toast.LENGTH_LONG).show()
                dismiss()
            }



        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        startCountdown()
        binding.tvResendOtp.setOnClickListener {
            dismiss()
//            val loginMap = hashMapOf<String,String>()
//            loginMap.put("mobile",userNumber)
//            mainViewModel.loginUser(loginMap)
        }
        binding.btnOtpContinue.setOnClickListener {
            val otpVerify = hashMapOf<String,String>()
            otpVerify.put("mobile",userNumber)
            otpVerify.put("otp",binding.edtOTP.text.toString())
            mainViewModel.verifyOtpResponse(otpVerify)

        }
        attachObservers()


        return binding.root
    }

    fun startCountdown() {
        object : CountDownTimer(30000, 1000) { // 30 seconds countdown, with updates every 1 second
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.tvTimer.visibility = View.VISIBLE

                binding.tvTimer.text = "00 : ${secondsRemaining.toString()}"
                binding.tvResendOtp.visibility = View.GONE
            }

            override fun onFinish() {
                binding.tvTimer.visibility = View.GONE

                binding.tvResendOtp.visibility = View.VISIBLE

            }
        }.start()
    }

    private fun attachObservers() {
        mainViewModel.verifyOtpResponse.observe(this, Event.EventObserver(

            onLoading = {

            },
            onSuccess = {
                val loginResponse = it

                if(it.status==true){
                    dismiss()
                    AppSession(requireContext()).put(Constant.IS_LOGGED_IN,true)

                    val intentDashboard = Intent(requireContext(), AppPermissionActivity::class.java)
                    intentDashboard.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intentDashboard)

                }else{
                    Toast.makeText(requireContext(),"Wrong OTP",Toast.LENGTH_LONG).show()


                }

            },
            onError = {
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()

            }


        ))


    }

}
