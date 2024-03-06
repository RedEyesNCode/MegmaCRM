package com.redeyesncode.crmfinancegs.ui.loanadmin.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.redeyesncode.crmfinancegs.data.KycDetails
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.databinding.ActivityUploadDocumentBinding
import com.redeyesncode.crmfinancegs.ui.loanadmin.MainViewModelLoanAdmin
import com.redeyesncode.crmfinancegs.base.AndroidApp
import com.redeyesncode.crmfinancegs.base.BaseActivity
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.gsfinancenbfc.ui.AadharNumberTextWatcher
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UploadDocumentActivity : BaseActivity() {

    lateinit var binding: ActivityUploadDocumentBinding

    @Inject
    lateinit var mainViewModel: MainViewModelLoanAdmin

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showSnackbar("Camera Permission is Granted")

            } else {
                showSnackbar("Camera Permission is Denied")
            }
        }

    val GALLERY_REQUEST_CODE_FRONT = 77
    val GALLERY_REQUEST_CODE_BACK = 75
    val GALLERY_REQUEST_CODE_PAN = 70
    val GALLERY_REQUEST_CODE_SELFIE =76
    val GALLERY_REQUEST_CODE_SIGNATURE =440

    val CAMERA_REQUEST_CODE_FRONT = 20
    val CAMERA_REQUEST_CODE_BACK = 21
    val CAMERA_REQUEST_CODE_PAN = 22
    val CAMERA_REQUEST_CODE_SELFIE =23
    val CAMERA_REQUEST_CODE_SIGNATURE =444

    var selfie : File?=null
    var aadharFront : File?=null
    var aadharBack : File?=null
    var panCard : File?=null
    var eSign:File?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityUploadDocumentBinding.inflate(layoutInflater)
        (application as AndroidApp).getDaggerComponent().injectUploadDocsActivity(this@UploadDocumentActivity)

        initClicks()
        attachObservers()
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        binding.edtAadharNumber.addTextChangedListener(AadharNumberTextWatcher(binding.edtAadharNumber))

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvAadharFront.setOnClickListener {
            showOptionsDialog(this@UploadDocumentActivity,GALLERY_REQUEST_CODE_FRONT,CAMERA_REQUEST_CODE_FRONT)

        }
        binding.tvAadharBack.setOnClickListener {
            showOptionsDialog(this@UploadDocumentActivity,GALLERY_REQUEST_CODE_BACK,CAMERA_REQUEST_CODE_BACK)


        }
        binding.tvPanCard.setOnClickListener {

            showOptionsDialog(this@UploadDocumentActivity,GALLERY_REQUEST_CODE_PAN,CAMERA_REQUEST_CODE_PAN)


        }
        binding.tvSign.setOnClickListener {
            showOptionsDialog(this@UploadDocumentActivity,GALLERY_REQUEST_CODE_SIGNATURE,CAMERA_REQUEST_CODE_SIGNATURE)

        }


        binding.tvSelfie.setOnClickListener {
            showToast("CAMERA ONLY")
            openCamera(CAMERA_REQUEST_CODE_SELFIE)

        }

        binding.btnSubmitDocument.setOnClickListener {

            if(panCard==null){
                showCustomDialog("Info","Please select pan card")
            }else if(aadharBack==null){
                showCustomDialog("Info","Please select aadhar back image")
            }else if(aadharFront==null){
                showCustomDialog("Info","Please select aadhar front image")
            } else if(selfie==null){
                showCustomDialog("Info","Please select selfie image")
            }else if(binding.edtPancard.text.toString().isEmpty()){
                showCustomDialog("Info","Please enter pan card number")

            }else if(binding.edtAadharNumber.text.toString().isEmpty()){

                showCustomDialog("Info","Please enter aadhar card number")

            }else if(binding.edtAadharNumber.text.toString().length<14){
                showCustomDialog("Info","Please enter VALID aadhar card number")

            }else if(binding.edtPancard.text.toString().length<10){
                showCustomDialog("Info","Please enter VALID pan card number")

            } else{
                submitKycRequest()
                val intentDashboard = Intent(this@UploadDocumentActivity, VerifyIncomeActivity::class.java)
                intentDashboard.putExtra("FILE_SIGN",eSign)
                intentDashboard.putExtra("PAN",panCard)
                intentDashboard.putExtra("ADHAR_FRONT",aadharFront)
                intentDashboard.putExtra("ADHAR_BACK",aadharBack)
                intentDashboard.putExtra("SELFIE",selfie)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentDashboard)
            }


        }








    }
    fun openFrontCamera(context: Context) {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        val packageManager: PackageManager = context.packageManager
        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

        for (info in activities) {
            val packageName = info.activityInfo.packageName
            if (packageName.contains("camera") || packageName.contains("android")) {
                intent.setPackage(packageName)
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1) // 0 for back camera, 1 for front camera
                context.startActivity(intent)
                break
            }
        }
    }
    private fun attachObservers() {
        mainViewModel.submitKycRequest.observe(this, Event.EventObserver(

            onLoading = {
                showLoadingDialog()

            },
            onError = {
                hideLoadingDialog()
                showCustomDialog("Error !","REQUEST NOT SUBMITTED ${it}")
            },
            onSuccess = {
                hideLoadingDialog()
                if(it.status==true){
                    showToast("KYC REQUEST SUBMITTED")
                    val intentDashboard = Intent(this@UploadDocumentActivity, VerifyIncomeActivity::class.java)
                    intentDashboard.putExtra("FILE_SIGN",eSign)
                    intentDashboard.putExtra("PAN",panCard)
                    intentDashboard.putExtra("ADHAR_FRONT",aadharFront)
                    intentDashboard.putExtra("ADHAR_BACK",aadharBack)
                    intentDashboard.putExtra("SELFIE",selfie)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentDashboard)


                }else{
                    showToast(it.message.toString())
                }
            }


        ))




    }


    fun removeMinusSign(inputString: String): String {
        return inputString.replace("-", "")
    }
    fun submitKycRequest(){


        val kycDetails = AppSession(this@UploadDocumentActivity).getObject(Constant.BODY_UPDATE_KYC,
            KycDetails::class.java) as KycDetails
//                    kycDetails.adhar_number = binding.aadharnumber.text.toString().uppercase()
        kycDetails.pan_number = binding.edtPancard.text.toString().uppercase()

        kycDetails.adhar_number = kycDetails.relativeNumber.toString().uppercase()
        kycDetails.relativeNumber = removeMinusSign(binding.edtAadharNumber.text.toString().uppercase())


        AppSession(this@UploadDocumentActivity).putObject(Constant.BODY_UPDATE_KYC,kycDetails)



        Log.i("KYC_DETAILS",Gson().toJson(kycDetails))
        Log.i("KYC_DETAILS",Gson().toJson(kycDetails))
        Log.i("KYC_DETAILS",Gson().toJson(kycDetails))
        Log.i("KYC_DETAILS",Gson().toJson(kycDetails))

        showToast("Submitting KYC REQUEST")
        val user = AppSession(this@UploadDocumentActivity).getObject(Constant.USER_LOGIN,
            LoginResponse::class.java) as LoginResponse
//        mainViewModel.submitKycRequestUser(aadharFront!!, aadharBack!!,panCard!!,
//            selfie!!,eSign,null,kycDetails,user.user?.id.toString())



    }


    fun showOptionsDialog(context: Context, requestCodeGallery: Int, requestCodeCamera: Int) {

        val options = arrayListOf<String>()
        options.add("CAMERA")
        options.add("GALLERY")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select an Option")
            .setItems(options.toTypedArray()) { _, which ->
                // Handle the selected option here
                val selectedOption = options[which]
                // Do something with the selected option
                // For example, you could pass it to another function or display it
                // Or perform any action based on the selected option
                if(selectedOption.equals("CAMERA")){
                    openCamera(requestCodeCamera)
                }else{
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, requestCodeGallery)

                }

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
    private fun openCamera(requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, requestCode)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            if(requestCode==GALLERY_REQUEST_CODE_FRONT){
                val imageUri = data.data
                try {
                    //getting bitmap object from uri

//                    showCustomDialog("Media","Aadhar Front is Selected !")

                    //displaying selected image to imageview
                    binding.ivAadharFrontPreview.setImageURI(imageUri)

                    aadharFront = getFile(this@UploadDocumentActivity, imageUri!!)
                    //calling the method uploadBitmap to upload image
                    // uploadBitmap(bitmap);



                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }else if(requestCode==GALLERY_REQUEST_CODE_BACK){
                val imageUri = data.data
                try {
                    //getting bitmap object from uri

                    //displaying selected image to imageview
//                    showCustomDialog("Media","Aadhar Back is Selected !")
                    binding.ivAdharBackPreview.setImageURI(imageUri)
                    aadharBack = getFile(this@UploadDocumentActivity, imageUri!!)
                    //calling the method uploadBitmap to upload image
                    // uploadBitmap(bitmap);



                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }else if(requestCode==GALLERY_REQUEST_CODE_PAN){
                val imageUri = data.data
                try {
                    //getting bitmap object from uri
                    val back = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
//                    showCustomDialog("Media","Pancard is Selected !")

                    //displaying selected image to imageview
                    binding.ivPancardPreview.setImageURI(imageUri)

                    panCard = getFile(this@UploadDocumentActivity, imageUri!!)
                    //calling the method uploadBitmap to upload image
                    // uploadBitmap(bitmap);



                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }else if(requestCode==GALLERY_REQUEST_CODE_SELFIE){
                val imageUri = data.data
                try {
                    //getting bitmap object from uri
                    val back = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
//                    showCustomDialog("Media","Selfie is Selected !")
                    binding.ivSelfiePreview.setImageURI(imageUri)

                    //displaying selected image to imageview

                    selfie = getFile(this@UploadDocumentActivity, imageUri!!)
//                    panCard = selfie
//                    aadharBack = selfie
//                    aadharFront = selfie
//                    eSign = selfie


                    //calling the method uploadBitmap to upload image
                    // uploadBitmap(bitmap);



                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }else if (requestCode == CAMERA_REQUEST_CODE_BACK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                // set into the desired image view.

                binding.ivAdharBackPreview.setImageBitmap(imageBitmap)

                try {
                    aadharBack = convertBitmapToFile(imageBitmap)

//                    showCustomDialog("Media","Aadhar Back is Selected !")

                }catch (e: IOException){
                    e.printStackTrace()
                }
            }else if(requestCode==CAMERA_REQUEST_CODE_FRONT){
                val imageBitmap = data?.extras?.get("data") as Bitmap
                // set into the desired image view.
                binding.ivAadharFrontPreview.setImageBitmap(imageBitmap)

                try {
                    aadharFront = convertBitmapToFile(imageBitmap)
//                    showCustomDialog("Media","Aadhar Front is Selected !")


                }catch (e: IOException){
                    e.printStackTrace()
                }
            }else if(requestCode==CAMERA_REQUEST_CODE_SELFIE){
                val imageBitmap = data?.extras?.get("data") as Bitmap

                binding.ivSelfiePreview.setImageBitmap(imageBitmap)

                // set into the desired image view.

                try {
                    selfie = convertBitmapToFile(imageBitmap)
                    panCard = selfie
                    aadharBack = selfie
                    aadharFront = selfie
                    eSign = selfie
//                    showCustomDialog("Media","Selfie is Selected !")

                }catch (e: IOException){
                    e.printStackTrace()
                }
            }else if(requestCode==CAMERA_REQUEST_CODE_PAN){
                val imageBitmap = data?.extras?.get("data") as Bitmap
                // set into the desired image view.
                binding.ivPancardPreview.setImageBitmap(imageBitmap)

                try {
                    panCard = convertBitmapToFile(imageBitmap)



//                    showCustomDialog("Media","Pancard is Selected !")


                }catch (e: IOException){
                    e.printStackTrace()
                }
            }else if(requestCode==CAMERA_REQUEST_CODE_SIGNATURE){
                val imageBitmap = data?.extras?.get("data") as Bitmap
                // set into the desired image view.
                binding.ivSignPreview.setImageBitmap(imageBitmap)

                try {
                    eSign = convertBitmapToFile(imageBitmap)



//                    showCustomDialog("Media","Pancard is Selected !")


                }catch (e: IOException){
                    e.printStackTrace()
                }
            }else if(requestCode==GALLERY_REQUEST_CODE_SIGNATURE){
                val imageUri = data.data
                try {
                    //getting bitmap object from uri
//                    showCustomDialog("Media","Selfie is Selected !")
                    binding.ivSignPreview.setImageURI(imageUri)

                    //displaying selected image to imageview

                    eSign = getFile(this@UploadDocumentActivity, imageUri!!)
                    //calling the method uploadBitmap to upload image
                    // uploadBitmap(bitmap);



                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


        }
    }
    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    //BELOW METHOD IS USED TO GET A FILE FROM THE URI .
    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri): File? {
        return try {
            val destinationFilename =
                File(context.filesDir.path + File.separatorChar + queryName(context, uri))
            try {
                context.contentResolver.openInputStream(uri).use { ins ->
                    createFileFromStream(
                        ins!!,
                        destinationFilename
                    )
                }
            } catch (ex: Exception) {
                Log.e("Save File", ex.message!!)
                ex.printStackTrace()
            }
            destinationFilename
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            var name: String? = null
            if (uri.scheme == "content") {
                cursor = context.contentResolver.query(uri, null, null, null, null)
                assert(cursor != null)
                val nameIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                name = cursor.getString(nameIndex)
            }
            if (name == null) {
                name = uri.path
                val cut = name!!.lastIndexOf('/')
                if (cut != -1) {
                    name = name.substring(cut + 1)
                }
            }
            name
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        } finally {
            cursor?.close()
        }
    }

    @Throws(IOException::class)
    fun convertBitmapToFile(bitmap: Bitmap): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imageFile = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir      /* directory */
        )

        val stream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()

        return imageFile
    }

}