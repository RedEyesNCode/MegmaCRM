package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redeyesncode.crmfinancegs.databinding.ActivityRejectBinding

class RejectActivity : AppCompatActivity() {

    lateinit var binding: ActivityRejectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRejectBinding.inflate(layoutInflater)


        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.btnOtpContinue.setOnClickListener {
            val intentEmiPackage = Intent(this@RejectActivity, ApplyLoanActivity::class.java)
            startActivity(intentEmiPackage)
        }


    }
}