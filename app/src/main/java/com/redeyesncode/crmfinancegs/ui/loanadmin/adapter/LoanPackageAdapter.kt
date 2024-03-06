package com.redeyesncode.crmfinancegs.ui.loanadmin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.crmfinancegs.data.DashboardResponse
import com.redeyesncode.crmfinancegs.databinding.LayoutListPackageBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.ApplyLoanActivity

class LoanPackageAdapter(var context: Context, var data:ArrayList<DashboardResponse.Packages>, var isKycApproved:Boolean) :
    RecyclerView.Adapter<LoanPackageAdapter.MyViewHolder>() {

    lateinit var binding: LayoutListPackageBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = LayoutListPackageBinding.inflate(LayoutInflater.from(context))



        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val packageData = data[position]
        holder.binding.apply {
            txtAmount.setText("₹"+packageData.amount.toString())
            txtTenure.setText("Tenure : "+packageData.tenure.toString())

            if(!isKycApproved){
                packageBtn.setOnClickListener({ v ->
                    val alert = AlertDialog.Builder(v.rootView.context)
                    alert.setTitle("")
                    alert.setMessage("KYC Not Completed please Complete KYC")
                    alert.setPositiveButton(
                        "Yes"
                    ) { dialogInterface, i -> // Start a new activity or perform an action
                        //context.startActivity(new Intent(context,ApplyKYC.class));
                        val ini = Intent(context, ApplyLoanActivity::class.java)
                        ini.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(ini)
                    }
                    alert.setNegativeButton(
                        "No"
                    ) { dialogInterface, i -> dialogInterface.dismiss() }
                    alert.show()
                })
            }



        }


    }

    override fun getItemCount(): Int {

        return data.size
    }

    class MyViewHolder(var binding: LayoutListPackageBinding): RecyclerView.ViewHolder(binding.root)

}