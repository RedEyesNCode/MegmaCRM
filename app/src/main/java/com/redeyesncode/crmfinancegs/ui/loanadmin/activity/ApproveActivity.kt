package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityApproveBinding

class ApproveActivity : AppCompatActivity() {


    lateinit var binding: ActivityApproveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityApproveBinding.inflate(layoutInflater)


        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {

        Glide.with(binding.root).load(R.drawable.ic_complete_payment).into(binding.ivSuccess)

        binding.ivClose.setOnClickListener {
            val intentDashboard = Intent(this@ApproveActivity, DashboardActivity::class.java)
            intentDashboard.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentDashboard)
        }
        binding.btnLoanStatus.setOnClickListener {

            if(binding.tvApproveStatus.text.toString().equals("Your KYC Application was Approved !")){
                // move to select bank activity
                val intentDashboard = Intent(this@ApproveActivity, SelectBankActivity::class.java)
                intentDashboard.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentDashboard)
            }else{
                // dashboard
                val intentDashboard = Intent(this@ApproveActivity, DashboardActivity::class.java)
                intentDashboard.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentDashboard)
            }
        }
        binding.tvApproveStatus.text = intent.getStringExtra("STATUS");









    }
}