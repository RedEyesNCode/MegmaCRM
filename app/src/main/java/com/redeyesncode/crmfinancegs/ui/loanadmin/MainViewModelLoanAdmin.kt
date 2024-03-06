package com.redeyesncode.crmfinancegs.ui.loanadmin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.crmfinancegs.data.CheckKycResponse
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.DashboardResponse
import com.redeyesncode.crmfinancegs.data.EmiResponse
import com.redeyesncode.crmfinancegs.data.KycDetails
import com.redeyesncode.crmfinancegs.data.LoanInfoResponse
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.data.OtpVerifyResponse
import com.redeyesncode.crmfinancegs.data.PostCommentResponse
import com.redeyesncode.crmfinancegs.data.PostsResponse
import com.redeyesncode.crmfinancegs.data.ShowBankAccountResponse
import com.redeyesncode.crmfinancegs.data.UserCustomPackageResponse
import com.redeyesncode.crmfinancegs.data.UserRegularPackageResponse
import com.redeyesncode.crmfinancegs.data.AddBankAccountResponse
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.crmfinancegs.data.ApplyLoanResponse
import com.redeyesncode.crmfinancegs.data.BodyUPIOrder
import com.redeyesncode.crmfinancegs.data.LoanInfo1Response
import com.redeyesncode.crmfinancegs.data.ResponseUPIOrder
import com.redeyesncode.crmfinancegs.data.ResponseUPIOrderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

class MainViewModelLoanAdmin(private val dashboardRepo: DefaultDashboardLoanAdmin): ViewModel() {

    private val _userFeedResponse = MutableLiveData<Event<Resource<ArrayList<PostsResponse>>>>()
    val userFeedResponse : LiveData<Event<Resource<ArrayList<PostsResponse>>>> = _userFeedResponse


    private val _postCommentResponse = MutableLiveData<Event<Resource<ArrayList<PostCommentResponse>>>>()
    val postCommentResponse : LiveData<Event<Resource<ArrayList<PostCommentResponse>>>> = _postCommentResponse


    private val _userLoginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()
    val userLoginResponse : LiveData<Event<Resource<LoginResponse>>> = _userLoginResponse
    private val _verifyOtpResponse = MutableLiveData<Event<Resource<OtpVerifyResponse>>>()
    val verifyOtpResponse : LiveData<Event<Resource<OtpVerifyResponse>>> = _verifyOtpResponse


    private val _registerUserResponse = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val registerUserResponse : LiveData<Event<Resource<CommonMessageResponse>>> = _registerUserResponse

    private val _dashboardResponse = MutableLiveData<Event<Resource<DashboardResponse>>>()
    val dashboardResponse : LiveData<Event<Resource<DashboardResponse>>> = _dashboardResponse

    private val _submitKycRequest = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val submitKycRequest : LiveData<Event<Resource<CommonMessageResponse>>> = _submitKycRequest

    private val _checkKycResponse = MutableLiveData<Event<Resource<CheckKycResponse>>>()
    val checkKycResponse : LiveData<Event<Resource<CheckKycResponse>>> = _checkKycResponse

    private val _assignRegularPackageResponse = MutableLiveData<Event<Resource<UserRegularPackageResponse>>>()
    val assignRegularPackageResponse : LiveData<Event<Resource<UserRegularPackageResponse>>> = _assignRegularPackageResponse



    private val _assignCustomPackageResponse = MutableLiveData<Event<Resource<UserCustomPackageResponse>>>()
    val assignCustomPackageResponse : LiveData<Event<Resource<UserCustomPackageResponse>>> = _assignCustomPackageResponse

    private val _loanInfoResponse = MutableLiveData<Event<Resource<LoanInfoResponse>>>()
    val loanInfoResponse : LiveData<Event<Resource<LoanInfoResponse>>> = _loanInfoResponse

    private val _showBankResponse = MutableLiveData<Event<Resource<ShowBankAccountResponse>>>()
    val showBankAccountResponse : LiveData<Event<Resource<ShowBankAccountResponse>>> = _showBankResponse

    private val _addBankResponse = MutableLiveData<Event<Resource<AddBankAccountResponse>>>()
    val addBankAccountResponse : LiveData<Event<Resource<AddBankAccountResponse>>> = _addBankResponse


    private val _loanEmiResponse = MutableLiveData<Event<Resource<EmiResponse>>>()
    val loanEmiResponse : LiveData<Event<Resource<EmiResponse>>> = _loanEmiResponse

    private val _eMandateResponse = MutableLiveData<Event<Resource<ResponseBody>>>()
    val eMandateResponse : LiveData<Event<Resource<ResponseBody>>> = _eMandateResponse


    private val _applyLoanResponse = MutableLiveData<Event<Resource<ApplyLoanResponse>>>()
    val applyLoanResponse : LiveData<Event<Resource<ApplyLoanResponse>>> = _applyLoanResponse


    private val _loanInfo1StatusResponse = MutableLiveData<Event<Resource<LoanInfo1Response>>>()
    val loanInfo1StatusResponse : LiveData<Event<Resource<LoanInfo1Response>>> = _loanInfo1StatusResponse


    private val _eMandateStatusResponse = MutableLiveData<Event<Resource<ResponseBody>>>()
    val eMandateStatusResponse : LiveData<Event<Resource<ResponseBody>>> = _eMandateStatusResponse


    private val _createUpiOrderResponse = MutableLiveData<Event<Resource<ResponseUPIOrder>>>()
    val createUpiOrderResponse : LiveData<Event<Resource<ResponseUPIOrder>>> = _createUpiOrderResponse

    private val _checkUpiOrderStatus = MutableLiveData<Event<Resource<ResponseUPIOrderStatus>>>()
    val checkUPIOrderStatus : LiveData<Event<Resource<ResponseUPIOrderStatus>>> = _checkUpiOrderStatus

    private val _payEmiResponse = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val payEmiResponse : LiveData<Event<Resource<CommonMessageResponse>>> = _payEmiResponse

    private val _forceCloseResponse = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val forceCloseResponse : LiveData<Event<Resource<CommonMessageResponse>>> = _forceCloseResponse

    fun forceCloseLOan(bodyUPIOrder: HashMap<String,String>){

        _forceCloseResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.forceCloseLoan(bodyUPIOrder)
            _forceCloseResponse.postValue(Event(result))
        }



    }

    fun payEmi(bodyUPIOrder: HashMap<String,String>){

        _payEmiResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.payEmi(bodyUPIOrder)
            _payEmiResponse.postValue(Event(result))
        }



    }

    fun checkUpiStatus(url:String,bodyUPIOrder: HashMap<String,String>){

        _checkUpiOrderStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.checkUpiOrderStatus(url,bodyUPIOrder)
            _checkUpiOrderStatus.postValue(Event(result))
        }



    }
    fun createUpiOrder(url:String,bodyUPIOrder: BodyUPIOrder){

        _createUpiOrderResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.createUpiOrder(url,bodyUPIOrder)
            _createUpiOrderResponse.postValue(Event(result))
        }



    }

    fun launchEmandateStatus(url:String,userIdMap:HashMap<String,String>){

        _eMandateStatusResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.launchENachStatus(url,userIdMap)
            _eMandateStatusResponse.postValue(Event(result))
        }



    }


    fun submitKycRequestUser(imageFileAadharFront: File, imageFileAadharBack: File, imageFilePan: File, fileSelfie: File, eSign: File?, bankStatement: File?, kycDetails: KycDetails, userId: String){
        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "adhar_front",
            imageFileAadharFront.getName(),
            RequestBody.create("image/*".toMediaTypeOrNull(), imageFileAadharFront)
        )
        val filePartPan: MultipartBody.Part = MultipartBody.Part.createFormData(
            "pan_image",
            imageFilePan.getName(),
            RequestBody.create("image/*".toMediaTypeOrNull(), imageFilePan)
        )
        val filePartAadharBack: MultipartBody.Part = MultipartBody.Part.createFormData(
            "adhar_back",
            imageFileAadharBack.getName(),
            RequestBody.create("image/*".toMediaTypeOrNull(), imageFileAadharBack)
        )
        val filePartSelfie: MultipartBody.Part = MultipartBody.Part.createFormData(
            "selfie",
            fileSelfie.getName(),
            RequestBody.create("image/*".toMediaTypeOrNull(), fileSelfie)
        )
        var filePartESign: MultipartBody.Part?=null
        var fileBankStatement: MultipartBody.Part?=null
        val emptyRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), ByteArray(0))


        if(eSign!=null){
            filePartESign  = MultipartBody.Part.createFormData(
                "sign_image",
                eSign.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), eSign)
            )
        }else{
            filePartESign = MultipartBody.Part.createFormData(
                "sign_image",
                "no_sign_image",
                emptyRequestBody)
        }
        if(bankStatement!=null){
            fileBankStatement= MultipartBody.Part.createFormData(
                "bank_statement",
                bankStatement.getName(),
                RequestBody.create("image/*".toMediaTypeOrNull(), bankStatement)
            )
        }else {
            fileBankStatement = MultipartBody.Part.createFormData(
                "bank_statement",
                "no_bank_statement",
                emptyRequestBody)
        }


        val name: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.firstName
                .toString()
        )
        val firstName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.firstName
                .toString()
        )
        val lastName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.lastName
                .toString()
        )
        val id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            userId
                .toString()
        )
        val gender: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.gender
                .toString()
        )

        val email: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.email
                .toString()
        )
        val pincode: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.pincode
                .toString()
        )
        var user_type: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "-1"
        )
        //putting user type check
        if(kycDetails.user_type.equals("Salaried")){
            user_type = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                "01"
            )
        }else if(kycDetails.user_type.equals("Employed Professional")){
            user_type = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                "02"
            )
        }else if(kycDetails.user_type.equals("Self Employed")){
            user_type = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                "03"
            )
        }else if(kycDetails.user_type.equals("Others")){
            user_type = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                "04"
            )
        }




        val monthlySalary: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.monthlySalary
                .toString()
        )
        val relativeNumberName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.relativeName
                .toString()
        )
        val relativeNumber: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.relativeNumber
                .toString()
        )
        val relativeNumberTwoName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.relativeNameTwo
                .toString()
        )
        val relativeNumberTwo: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.relativeNumber2
                .toString()
        )
        val panNumber: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.pan_number
                .toString()
        )
        val dob: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.dob
                .toString()
        )

        val address: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.currentAddress
                .toString()
        )
        val aadharNumber: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.adhar_number
                .toString()
        )
        val state: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            kycDetails.state
                .toString()
        )
        _submitKycRequest.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.submitKYCRequest(filePart,filePartAadharBack,filePartPan,filePartSelfie,
                id,
                firstName,
                lastName,
                dob,
                gender,
                email,pincode,
                user_type,
                monthlySalary,
                relativeNumberName,
                relativeNumberTwoName,
                relativeNumberTwo,
                address,
                panNumber,
                name,
                relativeNumber,
                aadharNumber,
                state,
                filePartESign,
                fileBankStatement)
            _submitKycRequest.postValue(Event(result))
        }

    }
    fun toRequestBody(value: String?): RequestBody? {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value!!)
    }
    fun getDashboardResponse(dashboardMap:HashMap<String,String>){
        _dashboardResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.dashboard(dashboardMap)
            _dashboardResponse.postValue(Event(result))
        }



    }
    fun getLoanInfoResponse(dashboardMap:HashMap<String,String>){
        _loanInfoResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.loanInfo(dashboardMap)
            _loanInfoResponse.postValue(Event(result))
        }



    }

    fun getLoanInfo1StatusResponse(dashboardMap:HashMap<String,String>){
        _loanInfo1StatusResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.loanInfo1(dashboardMap)
            _loanInfo1StatusResponse.postValue(Event(result))
        }



    }
    fun applyLoan(dashboardMap:HashMap<String,String>){
        _applyLoanResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.loanApply(dashboardMap)
            _applyLoanResponse.postValue(Event(result))
        }



    }
    fun getEMandateResponse(url:String){
        _eMandateResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.launchENach(url)
            _eMandateResponse.postValue(Event(result))
        }



    }

    fun showBankInfo(url:String,dashboardMap:HashMap<String,String>){
        _showBankResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.showBankAccount(url,dashboardMap)
            _showBankResponse.postValue(Event(result))
        }



    }
    fun addBankAccount(url:String,dashboardMap:HashMap<String,String>){
        _addBankResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.addBankAccount(url,dashboardMap)
            _addBankResponse.postValue(Event(result))
        }



    }
    fun getEmiResponse(dashboardMap:HashMap<String,String>){
        _loanEmiResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.emiInfo(dashboardMap)
            _loanEmiResponse.postValue(Event(result))
        }



    }


    fun checkKycResponse(dashboardMap:HashMap<String,String>){
        _checkKycResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.checkKycStatus(dashboardMap)
            _checkKycResponse.postValue(Event(result))
        }



    }
    fun assignRegularPackage(dashboardMap:HashMap<String,String>){
        _assignRegularPackageResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.assignDefaultPackage(dashboardMap)
            _assignRegularPackageResponse.postValue(Event(result))
        }



    }
    fun assignCustomPackage(dashboardMap:HashMap<String,String>){
        _assignCustomPackageResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.assignCustomPackage(dashboardMap)
            _assignCustomPackageResponse.postValue(Event(result))
        }



    }

    fun getAllPosts(){
        _userFeedResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getAllPosts()
            _userFeedResponse.postValue(Event(result))
        }

    }

    fun verifyOtpResponse(otpVerifyMap:HashMap<String,String>){

        _verifyOtpResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.otpVerification(otpVerifyMap)
            _verifyOtpResponse.postValue(Event(result))
        }
    }
    fun registerUser(registerMap:HashMap<String,String>){

        _registerUserResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.registerUser(registerMap)
            _registerUserResponse.postValue(Event(result))
        }
    }

    fun loginUser(loginMap: HashMap<String,String>){
        _userLoginResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.loginUser(loginMap)
            _userLoginResponse.postValue(Event(result))
        }
    }

    fun getCommentPost(post_id:String){
        _postCommentResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getPostComment(post_id)
            _postCommentResponse.postValue(Event(result))
        }
    }
}