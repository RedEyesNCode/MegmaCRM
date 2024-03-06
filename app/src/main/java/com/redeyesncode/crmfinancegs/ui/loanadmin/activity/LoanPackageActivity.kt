package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityLoanPackageBinding
import com.redeyesncode.gsfinancenbfc.base.BaseAdapter

class LoanPackageActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoanPackageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoanPackageBinding.inflate(layoutInflater)



        binding.recvEmi.adapter = BaseAdapter<String>(this@LoanPackageActivity,
            R.layout.item_emi_package,4)


        binding.recvEmi.layoutManager = LinearLayoutManager(this@LoanPackageActivity,LinearLayoutManager.VERTICAL,false)
        binding.ivBack.setOnClickListener {

            finish()

        }

        binding.btnConfirm.setOnClickListener {
            val intentVerifyKyc = Intent(this@LoanPackageActivity, LoanReviewActivity::class.java)
            startActivity(intentVerifyKyc)

        }

        setContentView(binding.root)
    }
}