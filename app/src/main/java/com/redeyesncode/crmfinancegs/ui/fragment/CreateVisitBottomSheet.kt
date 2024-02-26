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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.databinding.LayoutBottomSheetCreateVisitBinding
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


class CreateVisitBottomSheet(var mContext: Context):BottomSheetDialogFragment() {

    lateinit var binding:LayoutBottomSheetCreateVisitBinding
    private val LOCATION_PERMISSION_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 77
    private val CAMERA_PERMISSION_CODE = 101
    var fileOne: File?=null

    interface OnDismissListener {
        fun onDismiss()
    }

    // Declare a variable to hold the listener
    private var dismissListener: OnDismissListener? = null

    // Setter method to set the dismiss listener
    fun setOnDismissListener(listener: OnDismissListener) {
        dismissListener = listener
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Call the dismiss listener when the BottomSheetDialogFragment is dismissed
        dismissListener?.onDismiss()
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

    @Inject
    lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutBottomSheetCreateVisitBinding.inflate(layoutInflater)
        (requireActivity().application as AndroidApp).getDaggerComponent().injectCreateVisitBottomSheet(this@CreateVisitBottomSheet)

        initClicks()

        attachObservers()


        return binding.root
    }

    private fun attachObservers() {
        mainViewModel.createVisitResponse.observe(viewLifecycleOwner,Event.EventObserver(
            onLoading = {

            },
            onError = {


            },
            onSuccess = {
                if(it.code.equals("200")){
                    dismiss()
                }
            }



        ))
        mainViewModel.responseUploadFile.observe(viewLifecycleOwner,Event.EventObserver(
            onLoading = {

            },
            onSuccess = {
                val bodyCreateVisit = BodyCreateVisit()
                bodyCreateVisit.latitude = binding.editTextLatitude.text.toString().toDouble()
                bodyCreateVisit.longitude = binding.editTextLongitude.text.toString().toDouble()
                bodyCreateVisit.userId = binding.editTextUserId.text.toString()
                bodyCreateVisit.customerName = binding.editTextCustomerName.text.toString()
                bodyCreateVisit.photo = it.message.toString()
                bodyCreateVisit.address = binding.editTextAddress.text.toString()
                mainViewModel.createUserVisit(bodyCreateVisit)
            },
            onError = {

            }


        ))


    }


    private fun initClicks() {

        val user = AppSession(requireContext()).getObject(Constant.USER_LOGIN,LoginUserResponse::class.java) as LoginUserResponse
        binding.editTextUserId.setText(user.data?.userId.toString())


        binding.btnCreateVisit.setOnClickListener {
            if(binding.editTextCustomerName.text.toString().isEmpty()){
                binding.editTextCustomerName.error = "Please enter name"
            }else if(binding.editTextAddress.text.toString().isEmpty()){
                binding.editTextAddress.error = "Please enter address"
            }else if(fileOne==null){
                Toast.makeText(requireContext(),"Please select photo",Toast.LENGTH_LONG).show()
            }else if(binding.editTextLatitude.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please tap on location",Toast.LENGTH_LONG).show()
            }else if(binding.editTextLongitude.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please tap on location",Toast.LENGTH_LONG).show()
            }else if(binding.editTextUserId.text.toString().isEmpty()){
                binding.editTextUserId.error = "Please enter User Id"
            }else{

                mainViewModel.uploadFile(fileOne!!)



            }
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.editTextLatitude.setOnClickListener {
            checkLocationPermission()

        }
        binding.editTextLongitude.setOnClickListener {
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
                    binding.editTextLatitude.setText("${latitude}")
                    binding.editTextLongitude.setText("${longitude}")
                    try {
                        binding.editTextAddress.setText(geocoder.getFromLocation(latitude,longitude,10)?.get(0)?.getAddressLine(0))

                    }catch (e:Exception){
                        binding.editTextAddress.setText("Unable to autofill address")
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
            binding.editTextPhoto.setText("Selfie is Clicked !")
            binding.ivAdharBackPreview.setImageBitmap(imageBitmap)
        }
    }
    fun bitmapToFile(bitmap: Bitmap, context: Context,imageName:String): File {
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


}