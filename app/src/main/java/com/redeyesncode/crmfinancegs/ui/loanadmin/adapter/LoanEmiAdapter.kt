package com.redeyesncode.crmfinancegs.ui.loanadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.EmiResponse
import com.redeyesncode.crmfinancegs.databinding.ItemLoanEmiBinding
import java.text.SimpleDateFormat
import java.util.Locale

class LoanEmiAdapter(var context: Context, var data:ArrayList<EmiResponse.Data>, var onClickActivity: onClickEmi): RecyclerView.Adapter<LoanEmiAdapter.MyViewholder>() {
    lateinit var binding: ItemLoanEmiBinding
    var firstUnpaidPosition: Int = -1

    //api key db71654d-d4cf-46bc-ae0f-1fe605f113fa
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {

        binding = ItemLoanEmiBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewholder(binding)

    }
    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val data =data[position]
        with(holder.binding) {
            txtDate.text = formatDate(data.expiryDate.toString())
            txtAmt.text = "RS ${data.amount.toString()}"

            val loanStatus = data.status.toString()
            txtStatus.text = loanStatus

            when (loanStatus) {
                LoanStatus.PAID -> {
                    txtStatus.visibility = View.VISIBLE
                    btnPay.visibility = View.GONE
                    loanLayout.background =
                        AppCompatResources.getDrawable(context, R.drawable.background_grey_border)
                }
                LoanStatus.UNPAID -> {
                    txtStatus.visibility = View.VISIBLE

                    // Update the first unpaid position if it's not set
                    if (firstUnpaidPosition == -1) {
                        firstUnpaidPosition = position
                    }
                    // Show the "Pay" button only for the first unpaid status
                    if (position == firstUnpaidPosition) {
                        btnPay.visibility = View.VISIBLE
                        loanLayout.background =
                            AppCompatResources.getDrawable(context, R.drawable.background_blue_border)
                    } else {
                        btnPay.visibility = View.GONE
                        loanLayout.background =
                            AppCompatResources.getDrawable(context, R.drawable.background_grey_border)
                    }


                }
                else -> {
                    // Handle other status if needed
                }
            }

            btnPay.setOnClickListener {
                onClickActivity.onEmiPayClick(data)
            }
        }
    }
    object LoanStatus {
        const val PAID = "Paid"
        const val UNPAID = "Unpaid"
    }
    override fun getItemCount(): Int {
        return data.size
    }
    interface onClickEmi{

        fun onEmiPayClick(data : EmiResponse.Data)
    }

    class MyViewholder(var binding: ItemLoanEmiBinding) : RecyclerView.ViewHolder(binding.root)
}