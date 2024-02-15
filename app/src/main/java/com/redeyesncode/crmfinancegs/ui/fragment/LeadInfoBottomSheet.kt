package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.LayoutLeadInfoBinding

class LeadInfoBottomSheet(var mContext: Context,var data:UserLeadResponse.Data):BottomSheetDialogFragment() {

    lateinit var binding:LayoutLeadInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LayoutLeadInfoBinding.inflate(layoutInflater)


        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.apply {
            tvLeadId.text = data.leadId.toString()
            tvFirstName.text = data.firstname.toString()
            tvLastName.text = data.lastname.toString()
            tvMiddleName.text = data.middlename.toString()
            tvDOB.text = data.dob.toString()
            tvGender.text = data.gender.toString()
            tvPincode.text = data.pincode.toString()
            tvUserType.text = data.userType.toString()

            tvMonthlySalary.text = data.monthlySalary.toString()
            tvRelativeName.text = data.relativeName.toString()
            tvRelativeNumber.text = data.relativeNumber.toString()
            tvCurrentAddress.text = data.currentAddress.toString()
            tvState.text = data.state.toString()
            tvLeadStatus.text = data.leadStatus.toString()






        }


        return binding.root
    }

}