package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.databinding.LayoutAttendanceRecordBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import javax.inject.Inject

class CreateAttendanceBottomSheet(var mContext: Context): BottomSheetDialogFragment() {

    private var loadingDialog: AlertDialog? = null

    lateinit var binding:LayoutAttendanceRecordBinding

    @Inject
    lateinit var mainViewModel: MainViewModel
    interface OnDismissListener {
        fun onDismiss()
    }

    private var dismissListener: OnDismissListener? = null
    fun setOnDismissListener(listener: OnDismissListener) {
        dismissListener = listener
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Call the dismiss listener when the BottomSheetDialogFragment is dismissed
        dismissListener?.onDismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}