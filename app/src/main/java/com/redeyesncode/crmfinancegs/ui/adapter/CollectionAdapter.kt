package com.redeyesncode.crmfinancegs.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.data.ResponseUserCollection
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.ItemUserCollectionBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CollectionAdapter(var context:Context,var data :ArrayList<ResponseUserCollection.Data>) :RecyclerView.Adapter<CollectionAdapter.MyViewholder>() {

    lateinit var binding: ItemUserCollectionBinding
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        binding = ItemUserCollectionBinding.inflate(LayoutInflater.from(context),parent,false)


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
        val currentData = data[position]
        binding.tvCollectionId.text = "Collection ID : ${currentData.collectionId.toString()}"
        binding.tvCollectionAmount.text = "Collection Amount : ${currentData.collectionAmount.toString()}"
        binding.tvCreatedAt.text = "Created At : ${convertUtcToIst(currentData.createdAt.toString())}"
        binding.tvCustomerName.text = "Full Name ${currentData.fullName.toString()}"
        binding.btnStatus.text = "Status : ${currentData.collectionStatus.toString()}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewholder(var binding:ItemUserCollectionBinding) : RecyclerView.ViewHolder(binding.root)


}