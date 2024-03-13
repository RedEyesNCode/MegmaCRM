package com.redeyesncode.crmfinancegs.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.ActivityLoginBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class LoginActivity : BaseActivity() {


    lateinit var binding:ActivityLoginBinding

    @Inject
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectLoginActivity(this@LoginActivity)


        attachObservers()
        initClicks()
        mainViewModel.checkAppVersion()
        checkLoginSession()
        logEmpBase("LOGIN_ACTIVITY")

        setContentView(binding.root)
    }

    private fun checkLoginSession() {
        val empId = AppSession(this@LoginActivity).getString(Constant.EMP_ID)
        val mPass = AppSession(this@LoginActivity).getString(Constant.MPASS)

        binding.edtEmployeeID.setText(empId)
//        binding.edtMPass.setText(mPass)

    }

    private fun initClicks() {
        binding.btnLogin.setOnClickListener {

            if(binding.edtEmployeeID.text.toString().isEmpty()){
                showCustomDialog("Info","Please enter EmployeeID")
            }else if(binding.edtMPass.text.toString().isEmpty()){
                showCustomDialog("Info","Please enter Mpass")

            }else{
                val loginMap = hashMapOf<String,String>()
                loginMap.put("employeeId",binding.edtEmployeeID.text.toString())
                loginMap.put("mPass" ,binding.edtMPass.text.toString())

                mainViewModel.loginUser(loginMap)
            }


        }


    }

    private fun attachObservers() {
        mainViewModel.responseVersionUpdate.observe(this,Event.EventObserver(
            onLoading = {


            },
            onSuccess = {
                val versionName = packageManager.getPackageInfo(packageName, 0).versionName
                val versionCode = packageManager.getPackageInfo(packageName, 0).versionCode

                if(it.data?.appVersionName.equals(versionName) && it.data?.appVersionCode.equals(versionCode.toString())){
                    showCustomDialog("CHECK-VERSION","Your app is up to date")
                }else{
                    showCustomDialog("IMPORTANT ALERT !","PLEASE UPDATE APP VISIT --> gsfinance.app")
                }

            },
            onError = {
                showToast(it)
            }

        ))

        mainViewModel.userLoginResponse.observe(this,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()

                if(it.code==200){
                    showToast(it.message.toString())


                    Handler().postDelayed(Runnable {
                        AppSession(this@LoginActivity).putObject(Constant.USER_LOGIN,it)
                        AppSession(this@LoginActivity).putString(Constant.EMP_ID,binding.edtEmployeeID.text.toString())
                        AppSession(this@LoginActivity).putString(Constant.MPASS,binding.edtMPass.text.toString())
                        val dashboardIntent = Intent(this@LoginActivity,DashboardActivity::class.java)
                        startActivity(dashboardIntent)
                    },4000)






                }else {
                    showToast(it.message.toString())
                }
            },
            onError = {
                showToast(it)
                hideLoadingDialog()
            }


        ))
    }
}