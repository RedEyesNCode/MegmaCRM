package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityIntroBinding
import com.redeyesncode.crmfinancegs.ui.activity.LoginActivity
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.adapter.XmlPagerAdapter
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant

class IntroActivity : BaseActivity() {
    lateinit var binding: ActivityIntroBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var xmlPagerAdapter: XmlPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)

        initClicks()

        setupViewPagerServices()

        setContentView(binding.root)
    }

    private fun setupViewPagerServices() {
        viewPager = binding.viewPager

        val xmlFiles = listOf(R.layout.layout_intro_card, R.layout.layout_intro_card,R.layout.layout_intro_card,R.layout.layout_intro_card) // Add your XML files to the list
        xmlPagerAdapter = XmlPagerAdapter(xmlFiles)
        viewPager.adapter = xmlPagerAdapter

        binding.dotsIndicator.attachTo(binding.viewPager)


    }

    private fun initClicks() {


        binding.btnGetStarted.setOnClickListener {

            val isLoggedIn = AppSession(this@IntroActivity).getBoolean(Constant.IS_LOGGED_IN)
            if(isLoggedIn){
                val intentLogin = Intent(this@IntroActivity, AppPermissionActivity::class.java)
                intentLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentLogin)

            }else{
                val intentLogin = Intent(this@IntroActivity, LoginActivity::class.java)
                intentLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intentLogin)
                finishAffinity()

            }

        }

    }
}