package com.redeyesncode.crmfinancegs.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.databinding.DialogDateRangePickerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateRangePickerDialog(
    context: Context,
    private val onDateRangeSelected: (startDate: String, endDate: String) -> Unit
) : BottomSheetDialogFragment() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendarStart = Calendar.getInstance()
    private val calendarEnd = Calendar.getInstance()

    lateinit var binding:DialogDateRangePickerBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDateRangePickerBinding.inflate(layoutInflater)
        updateDateLabels()

        binding.btnSelect.setOnClickListener {
            onDateRangeSelected(
                dateFormat.format(calendarStart.time),
                dateFormat.format(calendarEnd.time)
            )
            dismiss()
        }

        binding.datePickerStart.init(
            calendarStart.get(Calendar.YEAR),
            calendarStart.get(Calendar.MONTH),
            calendarStart.get(Calendar.DAY_OF_MONTH),
            DatePickerListener(calendarStart)
        )

        binding.datePickerEnd.init(
            calendarEnd.get(Calendar.YEAR),
            calendarEnd.get(Calendar.MONTH),
            calendarEnd.get(Calendar.DAY_OF_MONTH),
            DatePickerListener(calendarEnd)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }


    private fun updateDateLabels() {
        binding.tvStartDate.text = dateFormat.format(calendarStart.time)
        binding.tvEndDate.text = dateFormat.format(calendarEnd.time)
    }

    private inner class DatePickerListener(private val calendar: Calendar) : DatePicker.OnDateChangedListener {
        override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            calendar.set(year, monthOfYear, dayOfMonth)
            updateDateLabels()
        }
    }
}
