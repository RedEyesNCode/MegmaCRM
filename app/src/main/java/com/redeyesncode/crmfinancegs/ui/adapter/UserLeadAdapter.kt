package com.redeyesncode.crmfinancegs.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.ItemUserLeadBinding

class UserLeadAdapter(var context: Context,var data :ArrayList<UserLeadResponse.Data>,var onActivityClick:UserLeadAdapter.onClick) :RecyclerView.Adapter<UserLeadAdapter.MyViewholder>(){






    lateinit var binding: ItemUserLeadBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        binding = ItemUserLeadBinding.inflate(LayoutInflater.from(context),parent,false)



        return MyViewholder(binding)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val data = data[position]

        holder.binding.apply {

            tvFirstName.text = "Name : ${data.firstname.toString()} ${data.lastname.toString()}"
            tvLeadId.text = "Lead ID : ${data.leadId.toString()}"
            tvDob.text = "Date of Birth : ${data.dob.toString()}"
            tvPincode.text = "Pincode :${data.pincode.toString()}"
            tvState.text = "State : ${data.state.toString()}"
            tvGender.text = "Gender : ${data.gender.toString()}"
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

    public class MyViewholder(var binding:ItemUserLeadBinding) :RecyclerView.ViewHolder(binding.root)
}