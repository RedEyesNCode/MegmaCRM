package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redeyesncode.crmfinancegs.databinding.ActivityVerifyKycBinding

class VerifyKycActivity : AppCompatActivity() {



    lateinit var binding: ActivityVerifyKycBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityVerifyKycBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.kycCard.setOnClickListener {
            val intentKycDocument = Intent(this@VerifyKycActivity, SelectBankActivity::class.java)
            startActivity(intentKycDocument)


        }


        setContentView(binding.root)
    }
}