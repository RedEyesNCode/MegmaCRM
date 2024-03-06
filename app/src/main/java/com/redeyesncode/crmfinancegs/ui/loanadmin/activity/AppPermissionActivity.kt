package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityAppPermissionBinding
import com.redeyesncode.crmfinancegs.base.BaseActivity

class AppPermissionActivity : BaseActivity() {

    lateinit var binding: ActivityAppPermissionBinding

    var isGrantedCamers :Boolean = false
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showSnackbar("Permission Granted !")
                isGrantedCamers = true
                val intentLogin = Intent(this@AppPermissionActivity, DashboardActivity::class.java)
                intentLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentLogin)
                Log.i("INFO","ASHUTOSH-GS-FINANCE-PACKAGE!")
            } else {
                showSnackbar("Permission is Denied. Try Again !")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppPermissionBinding.inflate(layoutInflater)


        initClicks()
        if(isCameraPermissionGranted(this@AppPermissionActivity)){
            val intentLogin = Intent(this@AppPermissionActivity, DashboardActivity::class.java)
            intentLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentLogin)
        }else{
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        setContentView(binding.root)
    }
    fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initClicks() {

//        binding.recvPermission.adapter = BaseAdapter<String>(this@AppPermissionActivity,R.layout.item_permission,5)
//        binding.recvPermission.layoutManager = LinearLayoutManager(this@AppPermissionActivity,LinearLayoutManager.VERTICAL,false)


        binding.btnAgree.setOnClickListener {
            if(!binding.ivCheckTerms.drawable.toBitmap().sameAs(getDrawable(R.drawable.ic_check_green)?.toBitmap())){
                showCustomDialog("Info","Please accept our privacy policy")
            }else{
                if(isGrantedCamers){
                    val intentLogin = Intent(this@AppPermissionActivity, DashboardActivity::class.java)
                    intentLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentLogin)

                }else{
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)

                }
            }


        }
        binding.btnDisagree.setOnClickListener {
            finishAffinity()
        }

        binding.layoutTermsnConditons.setOnClickListener {
            if(binding.ivCheckTerms.drawable.toBitmap().sameAs(getDrawable(R.drawable.ic_check_green)?.toBitmap())){
                binding.ivCheckTerms.setImageDrawable(getDrawable(R.drawable.bcakground_green_uncheck))
            }else if(binding.ivCheckTerms.drawable.toBitmap().sameAs(getDrawable(R.drawable.bcakground_green_uncheck)?.toBitmap())){
                binding.ivCheckTerms.setImageDrawable(getDrawable(R.drawable.ic_check_green))

            }

        }

    }
}