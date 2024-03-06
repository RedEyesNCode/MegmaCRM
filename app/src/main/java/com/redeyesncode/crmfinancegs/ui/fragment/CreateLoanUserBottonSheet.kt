package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.databinding.LayoutBottomSheetCreateLoanUserBinding
import com.redeyesncode.crmfinancegs.ui.activity.CreateLeadActivity
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class CreateLoanUserBottonSheet(var mContext:Context):BottomSheetDialogFragment() {

    lateinit var binding:LayoutBottomSheetCreateLoanUserBinding
    private var loadingDialog: AlertDialog? = null

    @Inject
    lateinit var mainViewModel: MainViewModel
    interface OnDismissListener {
        fun onDismiss()
    }
    fun showMessageDialog(message:String,title:String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }

        val mDialog = builder.create()
        if(!mDialog.isShowing){
            mDialog.show()

        }

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
                hideLoadingDialog()
                if (it.status==true) {
                    val map = hashMapOf<String,String>()
                    map.put("mobile",binding.edtMobileNumber.text.toString())
                    map.put("pancard",binding.edtPanCard.text.toString())
                    map.put("aadhar",binding.edtAadharNumber.text.toString())

                    val bodyCreateLead = BodyCreateLead()
                    bodyCreateLead.gs_loan_number = it.user?.mobile.toString()
                    bodyCreateLead.gs_loan_password = it.user?.loginOtp.toString()
                    bodyCreateLead.gs_loan_userid = it.user?.id.toString()


                    AppSession(requireContext()).putObject(Constant.BODY_CREATE_LEAD,bodyCreateLead)



                    mainViewModel.checkUniqueLead(map)
                    AppSession(requireContext()).putObject(Constant.RESPONSE_CREATE_LOAN_USER,it)
                    showSnackBar("Login Success....checking if lead is unique or not",binding.root)

                }else{
                    Toast.makeText(requireContext(),"Login Api Error !",Toast.LENGTH_SHORT).show()

                }
            },
            onError = {
                hideLoadingDialog()
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()

            },
            onLoading = {
                showLoadingDialog()

            }

        ))
        mainViewModel.responseCreateLoanUser.observe(viewLifecycleOwner,Event.EventObserver(

            onLoading = {
                        showLoadingDialog()

            }, onSuccess = {
                hideLoadingDialog()
                if (it.status==true){
                    val loginUrl = "https://megmagroup.loan/newApi/api/login"
                    val loginMap = hashMapOf<String,String>()
                    loginMap.put("mobile",binding.edtMobileNumber.text.toString())
                    mainViewModel.checkLoginUser(loginUrl,loginMap)
                    showSnackBar("Register Success on GS Loan...Testing Log In in GS Loan App",binding.root)
                }else if(it.status==false){
                    showMessageDialog("User Already Registered on GS Loan App, If you submitted the lead you may be able to view in lead table !","Info")
                }


            },
            onError = {
                showLoadingDialog()
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()

            }
        ))

        mainViewModel.responseCheckUniqueLead.observe(viewLifecycleOwner,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()

            },
            onSuccess = {
                hideLoadingDialog()
                showMessageDialog("You have created a User on GS Loan App","Info")

                val intentCreateLead = Intent(requireContext(), CreateLeadActivity::class.java)
                val bodyCreateLead = AppSession(requireContext()).getObject(Constant.BODY_CREATE_LEAD,BodyCreateLead::class.java) as BodyCreateLead

                bodyCreateLead.pancard = binding.edtPanCard.text.toString();
                bodyCreateLead.mobileNumber= binding.edtMobileNumber.text.toString()
                bodyCreateLead.aadhar = binding.edtAadharNumber.text.toString()
                bodyCreateLead.gs_loan_number = binding.edtMobileNumber.text.toString()
                bodyCreateLead.gs_loan_userid = ""
                bodyCreateLead.gs_loan_password = ""
                AppSession(requireContext()).putObject(Constant.BODY_CREATE_LEAD,bodyCreateLead)


                intentCreateLead.putExtra("EMAIL", "")
                intentCreateLead.putExtra("NUMBER", binding.edtMobileNumber.text.toString())
                Handler().postDelayed(Runnable {
                    startActivity(intentCreateLead)

                },2000)
            }


        ))


    }
    fun showLoadingDialog() {
        val builder = AlertDialog.Builder(mContext)
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)


        loadingDialog = builder.create()
        if(!loadingDialog?.isShowing!!){
            loadingDialog?.show()

        }
    }

    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }
    private fun initClicks() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnLogin.setOnClickListener {
            if(binding.edtPanCard.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please enter pancard",Toast.LENGTH_SHORT).show()

            }else if(binding.edtMobileNumber.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please enter password",Toast.LENGTH_SHORT).show()

            }else if(binding.edtAadharNumber.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please enter aadhar number",Toast.LENGTH_SHORT).show()

            } else{
                // call the signup api of loan app

                showSnackBar("Checking...Signup in GS Loan",binding.root)
                val signupUrl = "https://megmagroup.loan/newApi/api/registernew"
                val signupMap = hashMapOf<String,String>()
                signupMap.put("mobile",binding.edtMobileNumber.text.toString())
                signupMap.put("email",binding.edtEmailAddress.text.toString())
                mainViewModel.createLoanUser(signupUrl,signupMap)


            // New check in CRM DB only.



            }


        }

    }


    fun showSnackBar(message: String,view:View){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val layoutParams = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.gravity = Gravity.TOP
//        layoutParams.gravity = Gravity.TOP
        snackbarView.layoutParams = layoutParams
        val snackbarText = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarText.maxLines = 5 // set the maximum number of lines to be displayed

        snackbar.show()

    }
}