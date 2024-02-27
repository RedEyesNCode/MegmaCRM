package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.databinding.LayoutBottomSheetCreateLoanUserBinding
import com.redeyesncode.crmfinancegs.ui.activity.CreateLeadActivity
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class CreateLoanUserBottonSheet(var mContext:Context):BottomSheetDialogFragment() {

    lateinit var binding:LayoutBottomSheetCreateLoanUserBinding

    @Inject
    lateinit var mainViewModel: MainViewModel
    interface OnDismissListener {
        fun onDismiss()
    }

    private var dismissListener: CreateVisitBottomSheet.OnDismissListener? = null
    fun setOnDismissListener(listener: CreateVisitBottomSheet.OnDismissListener) {
        dismissListener = listener
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Call the dismiss listener when the BottomSheetDialogFragment is dismissed
        dismissListener?.onDismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = LayoutBottomSheetCreateLoanUserBinding.inflate(layoutInflater)
        (requireActivity().application as AndroidApp).getDaggerComponent().injectCreateLoanUser(this@CreateLoanUserBottonSheet)

       initClicks()
        attachObservers()
        return binding.root
    }

    private fun attachObservers() {

        mainViewModel.responseCheckLoginLoanUser.observe(viewLifecycleOwner,Event.EventObserver(

            onSuccess = {
                dismiss()
                if (it.status==true) {
                    val intentCreateLead = Intent(requireContext(), CreateLeadActivity::class.java)
                    intentCreateLead.putExtra("EMAIL", binding.edtEmail.text.toString())
                    intentCreateLead.putExtra("NUMBER", binding.edtMobileNumber.text.toString())

                    AppSession(requireContext()).putObject(Constant.RESPONSE_CREATE_LOAN_USER, it)
                    startActivity(intentCreateLead)
                }else{
                    dismiss()
                }
            },
            onError = {

            },
            onLoading = {

            }

        ))
        mainViewModel.responseCreateLoanUser.observe(viewLifecycleOwner,Event.EventObserver(

            onLoading = {

            }, onSuccess = {
                if (it.status==true){
                    val loginUrl = "https://megmagroup.loan/newApi/api/login"
                    val loginMap = hashMapOf<String,String>()
                    loginMap.put("mobile",binding.edtMobileNumber.text.toString())
                    mainViewModel.checkLoginUser(loginUrl,loginMap)
                }else{
                    // call the login api instead
                    val loginUrl = "https://megmagroup.loan/newApi/api/login"
                    val loginMap = hashMapOf<String,String>()
                    loginMap.put("mobile",binding.edtMobileNumber.text.toString())
                    mainViewModel.checkLoginUser(loginUrl,loginMap)
                }


            },
            onError = {
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()

            }
        ))


    }

    private fun initClicks() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnLogin.setOnClickListener {
            if(binding.edtEmail.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please enter email",Toast.LENGTH_SHORT).show()

            }else if(binding.edtMobileNumber.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please enter password",Toast.LENGTH_SHORT).show()

            }else{
                // call the signup api of loan app
                val signupUrl = "https://megmagroup.loan/newApi/api/registernew"
                val signupMap = hashMapOf<String,String>()
                signupMap.put("mobile",binding.edtMobileNumber.text.toString())
                signupMap.put("email",binding.edtEmail.text.toString())
                mainViewModel.createLoanUser(signupUrl,signupMap)
            }


        }

    }
}