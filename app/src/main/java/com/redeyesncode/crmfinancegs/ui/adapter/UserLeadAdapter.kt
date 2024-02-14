package com.redeyesncode.crmfinancegs.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.ItemUserLeadBinding

class UserLeadAdapter(var context: Context,var data :ArrayList<UserLeadResponse.Data>) :RecyclerView.Adapter<UserLeadAdapter.MyViewholder>(){


    lateinit var binding: ItemUserLeadBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        binding = ItemUserLeadBinding.inflate(LayoutInflater.from(context),parent,false)



        return MyViewholder(binding)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val data = data[position]

        holder.binding.apply {

            tvFirstName.text = data.firstname.toString()
            tvLeadId.text = "Lead ID ${data.leadId.toString()}"
            tvDob.text = data.dob.toString()
            tvPincode.text = data.pincode.toString()
            tvState.text = data.state.toString()
            tvGender.text = data.gender.toString()
            btnLeadStatus.text = data.leadStatus.toString()


        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    public class MyViewholder(var binding:ItemUserLeadBinding) :RecyclerView.ViewHolder(binding.root)
}