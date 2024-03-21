package com.redeyesncode.crmfinancegs.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.BodyCreateAttendance
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.databinding.LayoutAttendanceRecordBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Locale
import javax.inject.Inject

class CreateAttendanceBottomSheet(var mContext: Context): BottomSheetDialogFragment() {

    private var loadingDialog: AlertDialog? = null

    lateinit var binding:LayoutAttendanceRecordBinding
    private val LOCATION_PERMISSION_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 77
    private val CAMERA_PERMISSION_CODE = 101
    var fileOne: File?=null
    @Inject
    lateinit var mainViewModel: MainViewModel
    interface OnDismissListener {
        fun onDismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, you can now get the location.
            getLocation()
        }else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        loadingDialog = builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutAttendanceRecordBinding.inflate(layoutInflater)
        (requireActivity().application as AndroidApp).getDaggerComponent().injectAddAttendanceSheet(this@CreateAttendanceBottomSheet)

        initClicks()
        attachObservers()

        return binding.root
    }
    fun showLoadingDialog(){
        if(!loadingDialog?.isShowing!!){
            loadingDialog?.show()
        }

    }

    fun dismissLoadingDialog(){
        try {

            if(loadingDialog!!.isShowing){
                loadingDialog?.dismiss()
            }
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun attachObservers() {
        mainViewModel.responseCreateAttendance.observe(viewLifecycleOwner, Event.EventObserver(
            onLoading = {
                        showLoadingDialog()


            },
            onError = {
                      dismissLoadingDialog()
                showMessageDialog(it,"INFO" +
                        "")


            },
            onSuccess = {
                dismissLoadingDialog()
                if(it.code.equals("200")){
                    dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()

                }else{
                    showMessageDialog("INFO",it.message.toString())
                }
            }



        ))
        mainViewModel.responseUploadFile.observe(viewLifecycleOwner, Event.EventObserver(
            onLoading = {

            },
            onSuccess = {
                val bodyCreateVisit = BodyCreateAttendance()
                val user = AppSession(requireContext()).getObject(
                    Constant.USER_LOGIN,
                    LoginUserResponse::class.java) as LoginUserResponse
                bodyCreateVisit.photo = it.message
                bodyCreateVisit.address = binding.edtAddress.text.toString()
                bodyCreateVisit.remark = binding.edtRemark.text.toString()
                bodyCreateVisit.userId = user.data?.userId.toString()
                bodyCreateVisit.status = binding.edtAttendanceStatus.text.toString()




                mainViewModel.createAttendance(bodyCreateVisit)
            },
            onError = {

            }


        ))


    }
    fun showMessageDialog(message:String,title:String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }

        val mDialog = builder.create()
        if(!mDialog.isShowing){
            mDialog.show()

        }

    }
    private fun initClicks() {

        val user = AppSession(requireContext()).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse


        binding.btnLogin.setOnClickListener {
            if(binding.edtAddress.text.toString().isEmpty()){
                binding.edtAddress.error = "Please enter address"
            }else if(binding.edtAttendanceStatus.text.toString().isEmpty()){
                binding.edtAttendanceStatus.error = "Please select status"
            }else if(fileOne==null){
                Toast.makeText(requireContext(),"Please select photo", Toast.LENGTH_LONG).show()
            }else{
                mainViewModel.uploadFile(fileOne!!)



            }
        }
        binding.edtAttendanceStatus.setOnClickListener {
            val options = arrayListOf<String>()
            options.add("PRESENT")
            options.add("ABSENT")
            showOptionsDialog(requireContext(),options,binding.edtAttendanceStatus)

        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.edtAddress.setOnClickListener {
            checkLocationPermission()

        }


        binding.tvAadharBack.setOnClickListener {
            if(checkCameraPermission()){
                dispatchTakePictureIntent()
            }else{
                requestCameraPermission()
            }
        }
    }
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        } else {
            // Permission already granted, you can proceed to get the location.
            getLocation()
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        // Code to get the latitude and longitude goes here.
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

// Get the last known location
        fusedLocationClient.lastLocation
            .addOnSuccessListener(requireActivity(), OnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
//                    binding.editTextLatitude.setText("${latitude}")
//                    binding.editTextLongitude.setText("${longitude}")
                    try {
                        binding.edtAddress.setText(geocoder.getFromLocation(latitude,longitude,10)?.get(0)?.getAddressLine(0))

                    }catch (e:Exception){
                        binding.edtAddress.setText("Unable to autofill address")
                    }

                    // Use latitude and longitude as needed.
                }else{
                    Toast.makeText(requireContext(),"Unable to fetch location",Toast.LENGTH_LONG).show()
                }
            })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            fileOne = bitmapToFile(imageBitmap,requireContext(),"CreateVisit"+System.currentTimeMillis())
            binding.ivAdharBackPreview.setImageBitmap(imageBitmap)
        }
    }
    fun bitmapToFile(bitmap: Bitmap, context: Context, imageName:String): File {
        val filesDir = context.filesDir
        val imageFile = File(filesDir, "${imageName}.png")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imageFile
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    fun showOptionsDialog(context: Context, options: ArrayList<String>, editText: EditText) {
        val optionsArray = options.toTypedArray()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose an option")
            .setItems(optionsArray) { dialog, which ->
                val selectedOption = optionsArray[which]
                editText.setText(selectedOption)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }


}