package com.redeyesncode.crmfinancegs.ui.loanadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ItemBankPartnersBinding

class BankPartnerPager(private val xmlFiles: List<String>) :
    RecyclerView.Adapter<BankPartnerPager.BankPartnerViewHolder>() {

    lateinit var binding: ItemBankPartnersBinding

    inner class BankPartnerViewHolder(itemView: ItemBankPartnersBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        // TODO: Add references to UI components in the XML page layout
        // Example: val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankPartnerViewHolder {
        binding = ItemBankPartnersBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BankPartnerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankPartnerViewHolder, position: Int) {
        val url = xmlFiles.get(position)
        Glide.with(binding.root).load(url).placeholder(R.drawable.ic_placeholder).into(binding.ivBankLogo)

        // TODO: Bind XML data to your layout
        // Example: holder.textView.text = xmlDocument.getElementsByTagName("exampleTag").item(0).textContent
    }

    override fun getItemCount(): Int = xmlFiles.size


    override fun getItemViewType(position: Int): Int {
        // Return the layout resource ID for the given position
        return position % xmlFiles.size // You can customize this logic based on your requirements
    }
}