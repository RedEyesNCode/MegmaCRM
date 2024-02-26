package com.redeyesncode.crmfinancegs.ui.activity

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityVisitSearchBinding
import com.redeyesncode.crmfinancegs.databinding.ImageDialogBinding
import com.redeyesncode.crmfinancegs.ui.DateRangePickerDialog
import com.redeyesncode.crmfinancegs.ui.adapter.UserLeadAdapter
import com.redeyesncode.crmfinancegs.ui.adapter.UserVisitAdapter
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

class VisitSearchActivity : BaseActivity() ,UserVisitAdapter.onClick{

    lateinit var binding:ActivityVisitSearchBinding
    var userVisits = arrayListOf<UserVisitResponse.Data>()

    lateinit var adapterUserVists: UserVisitAdapter

    override fun onViewSelfie(data: UserVisitResponse.Data) {

        setupImageDialog(data)

    }
    private fun setupImageDialog(data: UserVisitResponse.Data){
        val binding = ImageDialogBinding.inflate(LayoutInflater.from(this@VisitSearchActivity))
        val builder = AlertDialog.Builder(this@VisitSearchActivity)
        builder.setView(binding.root)
        val dialog = builder.create()
        Glide.with(binding.root).load(data.photo.toString()).into(binding.ivSelfie)
        dialog.show()
        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
    }
    @Inject
    lateinit var mainViewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVisitSearchBinding.inflate(layoutInflater)

        (application as AndroidApp).getDaggerComponent().injectVisitSearch(this@VisitSearchActivity)

        attachObservers()
        initialApiCall()
        initClicks()
        setupSearchView()

        setContentView(binding.root)
    }
    private fun setupSearchView() {

        binding.searchView.isIconified = false
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the original data based on the new text
                val filteredData = userVisits.filter { yourItem ->
                    // Implement your own filtering logic here, for example:
                    yourItem.customerName!!.contains(newText.orEmpty(), ignoreCase = true)
                }

                // Update the adapter with the filtered data
                adapterUserVists.updateData(filteredData as ArrayList<UserVisitResponse.Data>)

                return true
            }
        })

    }
    private fun initialApiCall() {
        val user = AppSession(this@VisitSearchActivity).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse

        val visitUserMap = HashMap<String,String>()
        visitUserMap.put("userId", user.data?.userId.toString())
        mainViewModel.getUserVisit(visitUserMap)

    }

    private fun attachObservers() {
        mainViewModel.userVisitResponse.observe(this, Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {

                hideLoadingDialog()

                if(it.code==200){
                    userVisits = it.data

                    adapterUserVists = UserVisitAdapter(this@VisitSearchActivity,userVisits,this@VisitSearchActivity)
                    binding.recvVisit.apply {
                        adapter = adapterUserVists
                        layoutManager = LinearLayoutManager(this@VisitSearchActivity,
                            LinearLayoutManager.VERTICAL,false)
                    }
                }else{
                    binding.recvVisit.visibility = View.GONE
                    binding.ivNoData.visibility = View.VISIBLE
                }


            },
            onError = {

                binding.recvVisit.visibility = View.GONE
                binding.ivNoData.visibility = View.VISIBLE
                showToast(it)
                hideLoadingDialog()
            }
        ))

        mainViewModel.responseFilterVisits.observe(this,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onSuccess = {

                hideLoadingDialog()

                if(it.code==200){
                    userVisits = it.data

                    adapterUserVists = UserVisitAdapter(this@VisitSearchActivity,userVisits,this@VisitSearchActivity)
                    binding.recvVisit.apply {
                        adapter = adapterUserVists
                        layoutManager = LinearLayoutManager(this@VisitSearchActivity,
                            LinearLayoutManager.VERTICAL,false)
                    }
                }else{
                    binding.recvVisit.visibility = View.GONE
                    binding.ivNoData.visibility = View.VISIBLE
                }


            },
            onError = {

                binding.recvVisit.visibility = View.GONE
                binding.ivNoData.visibility = View.VISIBLE
                showToast(it)
                hideLoadingDialog()
            }


        ))
    }

    private fun initClicks() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.fabFilter.setOnClickListener {

            val filterOptions = arrayListOf<String>()
            filterOptions.add("Date Filter")
            filterOptions.add("RESET")


            showOptionsDialog(this@VisitSearchActivity,filterOptions)

        }



    }
    fun showOptionsDialog(context: Context, options: ArrayList<String>) {
        val optionsArray = options.toTypedArray()

        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Choose an option")
            .setItems(optionsArray) { dialog, which ->
                val selectedOption = optionsArray[which]
                if(selectedOption.equals("Date Filter")){

                    val dateRangePickerDialog = DateRangePickerDialog(this) { startDate, endDate ->
                        // Use startDate and endDate as needed
                        println("Selected start date: $startDate")
                        println("Selected end date: $endDate")

                        filterApiDate(startDate,endDate)


                    }
                    dateRangePickerDialog.show(supportFragmentManager,"DATE-RANGER")
                }else if(selectedOption.equals("RESET")){
                    initialApiCall()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }
    private fun filterApiDate(startDate: String, endDate: String) {
        val user = AppSession(this@VisitSearchActivity).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse

        val filterMap = hashMapOf<String,String>()
        filterMap.put("customerName","")
        filterMap.put("fromDate",startDate)
        filterMap.put("toDate",endDate)
        filterMap.put("userId",user.data?.userId.toString())
        mainViewModel.filterVisits(filterMap)




    }
}