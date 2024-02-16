package com.redeyesncode.crmfinancegs.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityChromeTabBinding

class ChromeTabActivity : AppCompatActivity() {
    lateinit var binding: ActivityChromeTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityChromeTabBinding.inflate(layoutInflater)


        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {


        binding.ivBack.setOnClickListener {
            finish()
        }

        val url = intent.getStringExtra("URL")

        if(url.equals("PRIVACY")){

            binding.webView.settings.javaScriptEnabled = true

            binding.webView.loadUrl("file:///android_asset/gs_privacy.html")

        }else{
            val customIntent = CustomTabsIntent.Builder()

            // below line is setting toolbar color
            // for our custom chrome tab.

            // below line is setting toolbar color
            // for our custom chrome tab.
            customIntent.setToolbarColor(ContextCompat.getColor(this@ChromeTabActivity, android.R.color.holo_green_dark))

            openCustomTab(this@ChromeTabActivity,customIntent.build(), Uri.parse(url))
        }


    }
    fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri?) {
        // package name is the default package
        // for our custom chrome tab
        val packageName = "com.android.chrome"
        if (packageName != null) {

            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName)

            // in that custom tab intent we are passing
            // our url which we have to browse.
            customTabsIntent.launchUrl(activity, uri!!)
            finish()
        } else {
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
            finish()
        }
    }
}