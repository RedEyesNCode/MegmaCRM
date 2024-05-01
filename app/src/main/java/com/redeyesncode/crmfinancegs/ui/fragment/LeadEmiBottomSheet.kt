package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.LayoutBottomSheetLeadEmiBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import javax.inject.Inject

class LeadEmiBottomSheet(var mContext: Context, var data: UserLeadResponse.Data):BottomSheetDialogFragment() {

    lateinit var binding:LayoutBottomSheetLeadEmiBinding


    @Inject
    lateinit var mainViewModel: MainViewModel

    private var loadingDialog: AlertDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetLeadEmiBinding.inflate(LayoutInflater.from(context),container,false)
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        (requireActivity().application as AndroidApp).getDaggerComponent().injectLeadEmiSheet(this@LeadEmiBottomSheet)
        attachObservers()

        binding.tvLeadAmount.setText(data.leadAmount.toString())
        binding.tvProcessingFees.setText("Processing Fees : ${data.feesAmount.toString()}")
        binding.edtInterestRate.setText("${data.lead_interest_rate.toString()}%")
        binding.btnCalculateEMI.setOnClickListener {
            val map = hashMapOf<String,String>()
            map.put("leadId",data.leadId.toString())
            map.put("month",binding.edtPeriod.text.toString())
            mainViewModel.getLeadEmi(map)
        }

        return binding.root

    }

    fun attachObservers(){
        mainViewModel.responseLeadEMI.observe(viewLifecycleOwner,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()

            },
            onSuccess = {
                hideLoadingDialog()
                binding.tvEmiAmount.text = "EMI AMOUNT RS ${it.data?.emiAmount.toString()}"
                binding.edtEmiPerMonth.setText( "${it.data?.emiAmount.toString()}")
                binding.tvTotalInterest.setText("Total Interest ${it.data?.totalInterest.toString()}")
                binding.tvTotalAmountPayable.setText("Total Amount ${it.data?.totalPayAmount.toString()}")
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
    fun showOptionsDialog(context: Context, options: ArrayList<String>, editText: EditText) {
        val optionsArray = options.toTypedArray()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose an option")
            .setItems(optionsArray) { dialog, which ->
                val selectedOption = optionsArray[which]
                editText.setText((which+1).toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }




}