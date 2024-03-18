package com.redeyesncode.crmfinancegs.ui.fragment

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.EventDay
import com.redeyesncode.crmfinancegs.base.BaseFragment
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.ResponseUserAttendance
import com.redeyesncode.crmfinancegs.databinding.FragmentAttendanceBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AttendanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttendanceFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val events: ArrayList<EventDay> = ArrayList()

    lateinit var binding:FragmentAttendanceBinding

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        (activity?.application as AndroidApp).getDaggerComponent().injectAttendance(this@AttendanceFragment)

        attachObservers()
        initialApiCall()
        binding.fabAttendance.setOnClickListener {
            val createAttendance = CreateAttendanceBottomSheet(requireContext())
            createAttendance.show(requireFragmentManager(),"ATTENDANCE")

        }


        return binding.root
    }

    private fun initialApiCall() {
        val user = AppSession(requireContext()).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse

        val map = hashMapOf<String,String>()
        map.put("userId",user.data?.userId.toString())
        mainViewModel.getUserAttendance(map)

    }

    private fun attachObservers() {
        mainViewModel.responseUserAttendance.observe(viewLifecycleOwner,Event.EventObserver(
            onLoading = {
                showLoadingDialog()
            },
            onError = {
                dismissLoadingDialog()
                showToast(it)
                binding.ivNoData.visibility = View.VISIBLE
                binding.layoutCalender.visibility = View.INVISIBLE
                      },
            onSuccess = {
                dismissLoadingDialog()
                binding.ivNoData.visibility = View.GONE
                binding.layoutCalender.visibility = View.VISIBLE
                showToast("Attendance Loaded ! ${it.data.size}")
                //setup the attendance calender here.
                updateCalendar(it)

            }


        ))



    }
    private fun updateCalendar(apiResponse: ResponseUserAttendance) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

        for (attendanceData in apiResponse.data) {
            val createdAtDate = dateFormatter.parse(attendanceData.createdAt)
            val calendar = Calendar.getInstance()
            if (createdAtDate != null) {
                calendar.time = createdAtDate
            }
            Log.i("ATTENDANCE",calendar.time.toString())
            Log.i("ATTENDANCE",createdAtDate.toString())

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            Log.i("ATTENDANCE",day.toString())
            Log.i("ATTENDANCE",day.toString())

            // Set the status on the calendar based on your logic
            setCalendarStatus(year, month, day, attendanceData.status!!,createdAtDate.toString())
//            try {
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                showToast(e.message.toString())
//            }
        }
    }
    private fun setCalendarStatus(
        year: Int,
        month: Int,
        day: Int,
        status: String,
        createdAtDate: String?
    ) {
        // Validate input parameters
//        require(year >= 0 && month in 1..12 && day in 1..31) { "Invalid date parameters" }
//        require(!status.isBlank()) { "Status cannot be empty" }

        // Parse createdAtDate string into a Date object
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())
        val date: Date? = try {
            createdAtDate?.let { dateFormat.parse(it) }
        } catch (e: ParseException) {
            null
        }
        val dateFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
        val createdAtDateFormatted = dateFormatter.parse(createdAtDate)

        // Check if the date is today
        val calendar = Calendar.getInstance()
        calendar.time = createdAtDateFormatted
//        if (createdAtDate != null) {
//            calendar.time = date
//        }
//        date?.let { calendar.time = it }
        val isToday = (year == calendar.get(Calendar.YEAR) &&
                month == calendar.get(Calendar.MONTH) &&
                day == calendar.get(Calendar.DAY_OF_MONTH))

        // If the date is today and the status is either "present" or "absent", mark it as already punched
        val isAlreadyPunched = isToday && (status.equals("present", ignoreCase = true) || status.equals("absent", ignoreCase = true))

        if(isAlreadyPunched){
            binding.fabAttendance.visibility = View.GONE
//            showMessageDialog("You have already punched for today !","Info")
        }
        // Retrieve drawable for the given status
        val drawable: Drawable = getDrawableForStatus(status)

        // Create an event for the specified date with the given status drawable
        val eventDay = EventDay(calendar, drawable)

        // Add the event to the list of events
        events.add(eventDay)

        // Update the calendar view with the new events
        binding.calenderView.setEvents(events)
    }

    private fun getDateInMillis(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.timeInMillis
    }

    private fun getDrawableForStatus(status: String): Drawable {
        // Customize the drawable based on the status
        // For simplicity, just using a colored circle
        val circleDrawable = CircleDrawable()

        when (status) {
            "PRESENT" -> circleDrawable.setColor(Color.GREEN)
            "ABSENT" -> circleDrawable.setColor(Color.RED)
            // Add more cases for other statuses if needed
            else -> circleDrawable.setColor(Color.GRAY)
        }

        return circleDrawable
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AttendanceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AttendanceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}