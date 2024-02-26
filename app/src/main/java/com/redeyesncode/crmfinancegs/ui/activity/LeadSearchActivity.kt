package com.redeyesncode.crmfinancegs.ui.activity

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityLeadSearchBinding
import com.redeyesncode.crmfinancegs.ui.DateRangePickerDialog
import com.redeyesncode.crmfinancegs.ui.adapter.UserLeadAdapter
import com.redeyesncode.crmfinancegs.ui.fragment.LeadInfoBottomSheet
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class LeadSearchActivity : BaseActivity(),UserLeadAdapter.onClick {





    lateinit var binding:ActivityLeadSearchBinding

    @Inject
    lateinit var mainViewModel: MainViewModel

    var userLeads = arrayListOf<UserLeadResponse.Data>()

    lateinit var adapterUserLeads: UserLeadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLeadSearchBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectLeadSearch(this@LeadSearchActivity)

        attachObservers()
        initClicks()

        initialApiCall()

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
                val filteredData = userLeads.filter { yourItem ->
                    // Implement your own filtering logic here, for example:
                    yourItem.firstname!!.contains(newText.orEmpty(), ignoreCase = true)
                }

                // Update the adapter with the filtered data
                adapterUserLeads.updateData(filteredData as ArrayList<UserLeadResponse.Data>)

                return true
            }
        })

    }

    private fun initialApiCall() {
        val user = AppSession(this@LeadSearchActivity).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse

        val visitUserMap = HashMap<String,String>()
        visitUserMap.put("userId", user.data?.userId.toString())

        mainViewModel.getUserLeads(visitUserMap)
    }

    private fun attachObservers() {



        mainViewModel.responseFilterLeads.observe(this,Event.EventObserver(

            onLoading = {

                        showLoadingDialog()

            },
            onSuccess = {
                hideLoadingDialog()
                if (it.code==200){


                    userLeads = it.data
                    adapterUserLeads = UserLeadAdapter(this@LeadSearchActivity,userLeads,this@LeadSearchActivity)
                    binding.recvLeads.apply {
                        adapter = adapterUserLeads
                        layoutManager = LinearLayoutManager(this@LeadSearchActivity)
                    }
                }else {
                    hideLoadingDialog()


                }
            },
            onError = {
                showToast(it)
                hideLoadingDialog()
            }


        ))







        mainViewModel.userLeadResponse.observe(this,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onError = {
                hideLoadingDialog()
                showToast(it)
            },
            onSuccess = {
                hideLoadingDialog()
                if (it.code==200){


                    userLeads = it.data
                    adapterUserLeads = UserLeadAdapter(this@LeadSearchActivity,it.data,this@LeadSearchActivity)
                    binding.recvLeads.apply {
                        adapter = adapterUserLeads
                        layoutManager = LinearLayoutManager(this@LeadSearchActivity)
                    }
                }else {
                    hideLoadingDialog()


                }

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
            filterOptions.add("Leads Status Filter")
            filterOptions.add("RESET")


            showOptionsDialog(this@LeadSearchActivity,filterOptions)




        }



    }

    fun showOptionsDialog(context: Context, options: ArrayList<String>) {
        val optionsArray = options.toTypedArray()

        val builder = AlertDialog.Builder(context)
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
                }else if(selectedOption.equals("Leads Status Filter")){
                    showLeadStatusFilterDialog(this@LeadSearchActivity)
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
        val user = AppSession(this@LeadSearchActivity).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse

        val filterMap = hashMapOf<String,String>()
        filterMap.put("name","")
        filterMap.put("fromDate",startDate)
        filterMap.put("toDate",endDate)
        filterMap.put("leadStatus","")
        filterMap.put("userId",user.data?.userId.toString())
        mainViewModel.filterLeads(filterMap)




    }

    fun showDatePickerDialog(context: Context, onDateSelected: (startDate: String, endDate: String) -> Unit) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker?, year: Int, month: Int, day: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                val endDate = dateFormat.format(selectedDate.time)

                // Assuming you want to select two dates with a gap of 7 days
                val startDate = dateFormat.format(selectedDate.apply { add(Calendar.DAY_OF_MONTH, -7) }.time)

                // Callback with selected dates
                onDateSelected(startDate, endDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Optionally set date range limits
        // datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        // datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 1000

        datePickerDialog.show()
    }
    fun showLeadStatusFilterDialog(context: Context) {

        val options = arrayListOf<String>()
        options.add("APPROVED")
        options.add("PENDING")
        options.add("REJECTED")
        val optionsArray = options.toTypedArray()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose an option")
            .setItems(optionsArray) { dialog, which ->
                val selectedOption = optionsArray[which]
                callLeadStatusFilterApi(selectedOption)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun callLeadStatusFilterApi(selectedOption: String) {
        val user = AppSession(this@LeadSearchActivity).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse

        val filterMap= hashMapOf<String,String>()
        filterMap.put("name","")
        filterMap.put("fromDate","")
        filterMap.put("toDate","")

        filterMap.put("userId",user.data?.userId.toString())
        filterMap.put("leadStatus",selectedOption)

        mainViewModel.filterLeads(filterMap)



    }

    override fun onLeadInfo(data: UserLeadResponse.Data) {

        val createVisitBottomSheet = LeadInfoBottomSheet(this@LeadSearchActivity,data)
        createVisitBottomSheet.show(supportFragmentManager,"LEAD-INFO")


    }
}