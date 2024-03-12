package com.redeyesncode.crmfinancegs.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.ItemUserLeadBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserLeadAdapter(var context: Context,var data :ArrayList<UserLeadResponse.Data>,var onActivityClick:UserLeadAdapter.onClick) :RecyclerView.Adapter<UserLeadAdapter.MyViewholder>(){




    class UserLeadDiffCallback(
        private val oldList: List<UserLeadResponse.Data>,
        private val newList: List<UserLeadResponse.Data>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].leadId == newList[newItemPosition].leadId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
    companion object {
        fun calculateDiff(
            oldList: List<UserLeadResponse.Data>,
            newList: List<UserLeadResponse.Data>
        ): DiffUtil.DiffResult {
            val diffCallback = UserLeadDiffCallback(oldList, newList)
            return DiffUtil.calculateDiff(diffCallback)
        }
    }
    fun updateData(newData: List<UserLeadResponse.Data> = emptyList()) {
        val diffCallback = UserLeadDiffCallback(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
    lateinit var binding: ItemUserLeadBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        binding = ItemUserLeadBinding.inflate(LayoutInflater.from(context),parent,false)



        return MyViewholder(binding)
    }
    fun convertUtcToIst(utcDateString: String): String {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.ENGLISH)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")

        val utcDate = utcFormatter.parse(utcDateString)

        val istFormatter = SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a", Locale.ENGLISH)
        istFormatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

        return istFormatter.format(utcDate)
    }
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val data = data[position]

        holder.binding.apply {

            tvFirstName.text = "Name : ${data.firstname.toString()} ${data.lastname.toString()}"
            tvLeadId.text = "Lead ID : ${data.leadId.toString()}"
            tvDob.text = "Created At : ${convertUtcToIst(data.createdAt.toString())}"
            tvPincode.text = "Pincode :${data.pincode.toString()}"
            tvState.text = "State : ${data.state.toString()}"
            tvGender.text = "Gender : ${data.gender.toString()}"
            tvNUMBER.text = "Number : ${data.mobileNumber.toString()}"
            val leadStatus = data.leadStatus.toString()

            if(leadStatus.equals("PENDING")){
                btnLeadStatus.setBackgroundColor(context.getColor(R.color.yellow))
            }else if(leadStatus.equals("APPROVED")){
                btnLeadStatus.setBackgroundColor(context.getColor(R.color.green))
                binding.tvLeadAmount.visibility = View.VISIBLE
                binding.tvLeadAmount.text = "Lead Amount : ${data.leadAmount.toString()}"

            }else if(leadStatus.equals("REJECTED")){
                btnLeadStatus.setBackgroundColor(context.getColor(R.color.red))

            }else if(leadStatus.equals("DISBURSED")){
                btnLeadStatus.setBackgroundColor(context.getColor(R.color.blue_disbursed))

            }


            btnLeadStatus.text = "Status : ${data.leadStatus.toString()}"

            ivForward.setOnClickListener {
                onActivityClick.onLeadInfo(data)

            }

        }
    }

    interface onClick{

        fun onLeadInfo(data: UserLeadResponse.Data)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(filteredData: ArrayList<UserLeadResponse.Data>) {
        data = filteredData
        notifyDataSetChanged()
    }

    public class MyViewholder(var binding:ItemUserLeadBinding) :RecyclerView.ViewHolder(binding.root)
}