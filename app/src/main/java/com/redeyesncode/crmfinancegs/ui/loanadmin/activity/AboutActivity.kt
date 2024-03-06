package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import com.chaos.view.BuildConfig
import com.redeyesncode.crmfinancegs.databinding.ActivityAboutBinding
import com.redeyesncode.crmfinancegs.base.BaseActivity


class AboutActivity : BaseActivity() {

    lateinit var binding: ActivityAboutBinding

    val NBFC_LIST = "https://rbidocs.rbi.org.in/rdocs/content/PDFs/NBFCsandARCs10012023.PDF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)



        initClicks()
        binding.tvVersionCode.setText(BuildConfig.VERSION_NAME)


        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.btnViewNBFCList.setOnClickListener {

//            val url = "https://docs.google.com/gview?embedded=true&url="
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            startActivity(intent)

            val intentApplyLoan = Intent(this@AboutActivity, ChromeTabActivity::class.java)
            intentApplyLoan.putExtra("URL","https://rbidocs.rbi.org.in/rdocs/content/PDFs/NBFCsandARCs10012023.PDF")
            startActivity(intentApplyLoan)
        }




    }
}