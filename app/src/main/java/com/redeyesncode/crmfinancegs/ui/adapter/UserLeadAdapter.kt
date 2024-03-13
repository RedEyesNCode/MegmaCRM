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

class UserLeadAdapter(var context: Context,var dataAdapter :ArrayList<UserLeadResponse.Data>,var onActivityClick:UserLeadAdapter.onClick) :RecyclerView.Adapter<UserLeadAdapter.MyViewholder>(){




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
        val diffCallback = UserLeadDiffCallback(dataAdapter, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataAdapter.clear()
        dataAdapter.addAll(newData)
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
        val data = dataAdapter[position]

        val binding = holder.binding

        binding.tvFirstName.text = "Name : ${data.firstname.toString()} ${data.lastname.toString()}"
        binding.tvLeadId.text = "Lead ID : ${data.leadId.toString()}"
        binding.tvDob.text = "Created At : ${convertUtcToIst(data.createdAt.toString())}"
        binding.tvPincode.text = "Pincode :${data.pincode.toString()}"
        binding.tvState.text = "State : ${data.state.toString()}"
        binding.tvGender.text = "Gender : ${data.gender.toString()}"
        binding.tvNUMBER.text = "Number : ${data.mobileNumber.toString()}"
        val leadStatus = data.leadStatus.toString()
        binding.tvLeadAmount.text = "Lead Amount : ${data.leadAmount.toString()}"

        when {
            leadStatus == "PENDING" -> {
                binding.tvLeadAmount.visibility = View.GONE
                binding.btnLeadStatus.setBackgroundColor(context.getColor(R.color.yellow))
            }
            leadStatus == "APPROVED" -> {
                binding.btnLeadStatus.setBackgroundColor(context.getColor(R.color.green))
                binding.tvLeadAmount.visibility = View.VISIBLE
                binding.tvLeadAmount.text = "Lead Amount : ${data.leadAmount.toString()}"
            }
            leadStatus == "REJECTED" -> {
                binding.tvLeadAmount.visibility = View.GONE
                binding.btnLeadStatus.setBackgroundColor(context.getColor(R.color.red))
            }
            leadStatus == "DISBURSED" -> {
                binding.tvLeadAmount.visibility = View.VISIBLE
                binding.tvLeadAmount.text = "Lead Amount : ${data.leadAmount.toString()}"
                binding.btnLeadStatus.setBackgroundColor(context.getColor(R.color.blue_disbursed))
            }
        }

        binding.btnLeadStatus.text = "Status : ${data.leadStatus.toString()}"

        binding.ivForward.setOnClickListener {
            onActivityClick.onLeadInfo(data)
        }
    }

    interface onClick{

        fun onLeadInfo(data: UserLeadResponse.Data)

    }

    override fun getItemCount(): Int {
        return dataAdapter.size
    }

    fun updateData(filteredData: ArrayList<UserLeadResponse.Data>) {
        dataAdapter = filteredData
        notifyDataSetChanged()
    }

    public class MyViewholder(var binding:ItemUserLeadBinding) :RecyclerView.ViewHolder(binding.root)
}