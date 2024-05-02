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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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
                    setupImageDialog(data.pancard_img.toString())

                }else if(selectedOption.equals("Aadharcard-Front")){
                    setupImageDialog(data.aadharFront.toString())

                }else if(selectedOption.equals("Aadharcard-Back")){
                    setupImageDialog(data.aadharBack.toString())
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
            tvLeadAmount.text = data.leadAmount.toString()
//            tvProcessingFees.text = data.processingFees.toString()
            tvFeesAmount.text = data.feesAmount.toString()

            if(data.disbursement_date.toString().isNotEmpty()){
                tvDisbursementDate.text = convertUtcToIst(data.disbursement_date.toString())
                tvCreatedAt.text = convertUtcToIst(data.createdAt.toString())

            }


            btnViewPhotos.setOnClickListener {
                var education = arrayListOf<String>()
                education.add("Selfie")
                education.add("Pancard")
                education.add("Aadharcard-Front")
                education.add("Aadharcard-Back")
//                education.add("Pancard")
//                education.add("Aadharcard")
                showOptionsDialog(requireContext(),education)


            }


        }


        return binding.root
    }

    fun convertUtcToIst(utcDateString: String): String {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")

        val utcDate = utcFormatter.parse(utcDateString)

        val istFormatter = SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a", Locale.ENGLISH)
        istFormatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

        return istFormatter.format(utcDate)
    }
}