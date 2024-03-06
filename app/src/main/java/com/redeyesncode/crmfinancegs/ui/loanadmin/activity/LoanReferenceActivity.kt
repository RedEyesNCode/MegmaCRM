package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import com.redeyesncode.crmfinancegs.databinding.ActivityLoanReferenceBinding
import com.redeyesncode.crmfinancegs.base.BaseActivity

class LoanReferenceActivity : BaseActivity() {

    lateinit var binding: ActivityLoanReferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoanReferenceBinding.inflate(layoutInflater)

        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.btnSubmitReference.setOnClickListener {
            val intentLoanOffer = Intent(this@LoanReferenceActivity, EMandateActivity::class.java)
            startActivity(intentLoanOffer)

        }






    }
}