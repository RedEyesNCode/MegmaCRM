package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.databinding.ImageDialogBinding
import com.redeyesncode.crmfinancegs.databinding.LayoutLeadInfoBinding

class LeadInfoBottomSheet(var mContext: Context,var data:UserLeadResponse.Data):BottomSheetDialogFragment() {

    lateinit var binding:LayoutLeadInfoBinding
    fun showOptionsDialog(context: Context, options: ArrayList<String>) {
        val optionsArray = options.toTypedArray()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose an option")
            .setItems(optionsArray) { dialog, which ->
                val selectedOption = optionsArray[which]

                if(selectedOption.equals("Selfie")){
                    setupImageDialog(data.selfie.toString())
                }else if(selectedOption.equals("Pancard")){
                    setupImageDialog(data.selfie.toString())

                }else if(selectedOption.equals("Aadharcard")){
                    setupImageDialog(data.selfie.toString())

                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }
    private fun setupImageDialog(data: String){
        val binding = ImageDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()
        Glide.with(binding.root).load(data).into(binding.ivSelfie)
        dialog.show()
        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
    }
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



            btnViewPhotos.setOnClickListener {
                var education = arrayListOf<String>()
                education.add("Selfie")
//                education.add("Pancard")
//                education.add("Aadharcard")
                showOptionsDialog(requireContext(),education)


            }


        }


        return binding.root
    }

}