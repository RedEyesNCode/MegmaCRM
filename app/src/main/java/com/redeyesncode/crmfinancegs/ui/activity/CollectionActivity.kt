package com.redeyesncode.crmfinancegs.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.BodyCreateCollection
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityCollectionBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class CollectionActivity : BaseActivity() {

    lateinit var binding:ActivityCollectionBinding

    @Inject
    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectCollectionActivity(this@CollectionActivity)

        attachObservers()
        initClicks()

        setContentView(binding.root)

    }

    private fun attachObservers() {
        mainViewModel.responseLoanDetails.observe(this,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){
                    binding.tableLayout.visibility = View.VISIBLE
                    binding.tvStatus.text = it.status.toString()

                    binding.edtFullName.setText(it.Name.toString())
                    binding.tvName.text = it.Name.toString()
                    binding.tvMobile.text = it.Mobile.toString()
                    binding.tvAmount.text = it.amount.toString()
                    binding.tvPenalty.text = it.Penalty.toString()
                    binding.tvEmiId.text = it.emiId.toString()
                    binding.edtCollectionAmount.setText(it.amount.toString())
                }else{
                    showToast("API STATUS IS FALSE")
                    binding.tableLayout.visibility = View.GONE
                    binding.tvStatus.text = "FALSE"
                    binding.tvName.text = ""
                    binding.tvMobile.text =""
                    binding.tvAmount.text = ""
                    binding.tvPenalty.text = ""
                    binding.tvEmiId.text = ""
                    binding.edtFullName.setText("")
                    binding.edtCollectionAmount.setText("")
                }

            },
            onError = {
                hideLoadingDialog()
                showToast("API STATUS IS FALSE")
                binding.edtFullName.setText("")
                binding.edtCollectionAmount.setText("")

                showToast(it)
                binding.tableLayout.visibility = View.GONE

            }


        ))

        mainViewModel.createCollectionResponse.observe(this,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()
            },
            onSuccess = {
                hideLoadingDialog()
                showCustomDialog("Info",it.message.toString())
            }

        ))



    }

    private fun initClicks() {
        binding.ivBack.setOnClickListener {

            finish()
        }

        binding.btnFetchLoanDetails.setOnClickListener {
            if(binding.edtLoanID.text.toString().isEmpty()){
                showCustomDialog("Info","Please enter loan id")
            }else{
                // call the api.
                val url = "https://megmagroup.loan/newApi/api/show_emi?loan_id=${binding.edtLoanID.text.toString()}"
                mainViewModel.getLoanDetails(url)
            }

        }
        binding.btnCreateCollection.setOnClickListener {
            if(binding.edtFullName.text.toString().isEmpty()){
                showToast("Please enter full name")
            }else if(binding.edtCollectionAmount.text.toString().isEmpty()){
                showToast("Please enter collection amount")
            }else{
                //call the api
                val user = AppSession(this@CollectionActivity).getObject(
                    Constant.USER_LOGIN,
                    LoginUserResponse::class.java) as LoginUserResponse
                val bodyCreateCollection = BodyCreateCollection()

                bodyCreateCollection.collectionAddress = ""
                bodyCreateCollection.collectionAmount = binding.edtCollectionAmount.text.toString()
                bodyCreateCollection.collectionLocation = ""
                bodyCreateCollection.fullName = binding.edtFullName.text.toString()
                bodyCreateCollection.gsLoanNumber = "GS_LOAN_NUMBER"
                bodyCreateCollection.gsLoanPassword = "GS_LOAN_PASS"
                bodyCreateCollection.gsLoanUserid = "GS_USER_ID"
                bodyCreateCollection.userId = user.data?.userId.toString()
                mainViewModel.createCollection(bodyCreateCollection)

            }


        }


    }
}