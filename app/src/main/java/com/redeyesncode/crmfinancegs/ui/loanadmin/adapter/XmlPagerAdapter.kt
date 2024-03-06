package com.redeyesncode.crmfinancegs.ui.loanadmin.adapter

// XmlPagerAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.LayoutIntroCardBinding

class XmlPagerAdapter(private val xmlFiles: List<Int>) :
    RecyclerView.Adapter<XmlPagerAdapter.XmlViewHolder>() {

        lateinit var binding: LayoutIntroCardBinding
    inner class XmlViewHolder(itemView: LayoutIntroCardBinding) : RecyclerView.ViewHolder(itemView.root) {
        // TODO: Add references to UI components in the XML page layout
        // Example: val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XmlViewHolder {
        binding = LayoutIntroCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return XmlViewHolder(binding)
    }

    override fun onBindViewHolder(holder: XmlViewHolder, position: Int) {
        val xmlFileResId = xmlFiles[position]
        if(position==0){
            //personal loan
            binding.tvLoanName.text = "Personal Loan"
            binding.tvLoanInfo.text = holder.itemView.context.getString(R.string.personal_loan_info_english)
        }else if(position==1){
            // home loan
            binding.tvLoanName.text = "Salaried Loan"

            binding.tvLoanInfo.text = holder.itemView.context.getString(R.string.salaried_loan_info)
        }else if(position==2){
            binding.tvLoanName.text = "Student Loan"
            binding.tvLoanInfo.text = holder.itemView.context.getString(R.string.student_loan_info)

        }else if(position==3){
            binding.tvLoanName.text = "Business Loan"
            binding.tvLoanInfo.text = holder.itemView.context.getString(R.string.business_loan_)
        }

        // TODO: Bind XML data to your layout
        // Example: holder.textView.text = xmlDocument.getElementsByTagName("exampleTag").item(0).textContent
    }

    override fun getItemCount(): Int = xmlFiles.size


    override fun getItemViewType(position: Int): Int {
        // Return the layout resource ID for the given position
        return position % xmlFiles.size // You can customize this logic based on your requirements
    }


}
