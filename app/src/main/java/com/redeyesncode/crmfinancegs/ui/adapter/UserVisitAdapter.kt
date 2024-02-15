package com.redeyesncode.crmfinancegs.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.databinding.ItemVisitUserBinding

class UserVisitAdapter(var context: Context,var data:ArrayList<UserVisitResponse.Data>,var onActivityClick:UserVisitAdapter.onClick):RecyclerView.Adapter<UserVisitAdapter.MyViewholder>() {


    lateinit var binding: ItemVisitUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        binding = ItemVisitUserBinding.inflate(LayoutInflater.from(context),parent,false)



        return MyViewholder(binding)



    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val data = data[position]
        holder.binding.apply {
            tvVisitId.text = "Visit ID : ${data.visitId.toString()}"
            tvAddress.text = "Address : ${data.address.toString()}"
            tvLocation.text = "Location :${data.latitude},${data.longitude}"
            tvCustomerName.text = "Name : ${data.customerName.toString()}"

            btnViewSelfie.setOnClickListener {
                onActivityClick.onViewSelfie(data)
            }

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
    interface onClick{

        fun onViewSelfie(data: UserVisitResponse.Data)
    }
    public class MyViewholder(var binding: ItemVisitUserBinding):RecyclerView.ViewHolder(binding.root)

}