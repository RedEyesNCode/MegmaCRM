package com.redeyesncode.crmfinancegs.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


        setContentView(binding.root)
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


        mainViewModel.userLoginResponse.observe(this,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()

                if(it.code==200){
                    showToast(it.message.toString())

                    AppSession(this@LoginActivity).putObject(Constant.USER_LOGIN,it)
                    val dashboardIntent = Intent(this@LoginActivity,DashboardActivity::class.java)
                    startActivity(dashboardIntent)




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