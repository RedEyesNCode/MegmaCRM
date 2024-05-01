package com.redeyesncode.crmfinancegs.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.crmfinancegs.data.BodyAdminLogin
import com.redeyesncode.crmfinancegs.data.BodyCreateAttendance
import com.redeyesncode.crmfinancegs.data.BodyCreateCollection
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.FilterLeadsResponse
import com.redeyesncode.crmfinancegs.data.KycDetails
import com.redeyesncode.crmfinancegs.data.LoanUserLoginResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.ResponseCreateCollection
import com.redeyesncode.crmfinancegs.data.ResponseLeadEMI
import com.redeyesncode.crmfinancegs.data.ResponseLoanDetails
import com.redeyesncode.crmfinancegs.data.ResponseUserAttendance
import com.redeyesncode.crmfinancegs.data.ResponseUserCollection
import com.redeyesncode.crmfinancegs.data.ResponseVersionUpdate
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.repository.DefaultDashboardRepo
import com.redeyesncode.gsfinancenbfc.base.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

class MainViewModel(private val dashboardRepo: DefaultDashboardRepo): ViewModel() {

    private val _userLoginResponse = MutableLiveData<Event<Resource<LoginUserResponse>>>()
    val userLoginResponse : LiveData<Event<Resource<LoginUserResponse>>> = _userLoginResponse

    private val _userLeadResponse = MutableLiveData<Event<Resource<UserLeadResponse>>>()
    val userLeadResponse : LiveData<Event<Resource<UserLeadResponse>>> = _userLeadResponse


    private val _userVisitResponse = MutableLiveData<Event<Resource<UserVisitResponse>>>()
    val userVisitResponse : LiveData<Event<Resource<UserVisitResponse>>> = _userVisitResponse

    private val _createVisitResponse = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val createVisitResponse : LiveData<Event<Resource<CommonMessageResponse>>> = _createVisitResponse

    private val _createLeadResponse = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val createLeadResponse : LiveData<Event<Resource<CommonMessageResponse>>> = _createLeadResponse

   
    private val _responseUploadFile = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val responseUploadFile : LiveData<Event<Resource<CommonMessageResponse>>> = _responseUploadFile

    private val _responseFilterLeads = MutableLiveData<Event<Resource<FilterLeadsResponse>>>()
    val responseFilterLeads : LiveData<Event<Resource<FilterLeadsResponse>>> = _responseFilterLeads

    private val _responseUserApprovedLead = MutableLiveData<Event<Resource<UserLeadResponse>>>()
    val responseUserApprovedLead : LiveData<Event<Resource<UserLeadResponse>>> = _responseUserApprovedLead

    private val _responseFilterVisits = MutableLiveData<Event<Resource<UserVisitResponse>>>()
    val responseFilterVisits : LiveData<Event<Resource<UserVisitResponse>>> = _responseFilterVisits

    private val _responseCreateLoanUser = MutableLiveData<Event<Resource<LoanUserLoginResponse>>>()
    val responseCreateLoanUser : LiveData<Event<Resource<LoanUserLoginResponse>>> = _responseCreateLoanUser

    private val _responseApplyKyc = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val responseApplyKyc : LiveData<Event<Resource<CommonMessageResponse>>> = _responseApplyKyc


    private val _responseCheckLoginLoanUser = MutableLiveData<Event<Resource<LoanUserLoginResponse>>>()
    val responseCheckLoginLoanUser : LiveData<Event<Resource<LoanUserLoginResponse>>> = _responseCheckLoginLoanUser


    private val _responseUpdateMpass = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val responseUpdateMpass : LiveData<Event<Resource<CommonMessageResponse>>> = _responseUpdateMpass



    private val _responseCheckUniqueLead = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val responseCheckUniqueLead : LiveData<Event<Resource<CommonMessageResponse>>> = _responseCheckUniqueLead


    private val _responseCreateAttendance = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val responseCreateAttendance : LiveData<Event<Resource<CommonMessageResponse>>> = _responseCreateAttendance

    private val _responseLogEmp = MutableLiveData<Event<Resource<CommonMessageResponse>>>()
    val responseLogEmp : LiveData<Event<Resource<CommonMessageResponse>>> = _responseLogEmp

    private val _responseVersionUpdate = MutableLiveData<Event<Resource<ResponseVersionUpdate>>>()
    val responseVersionUpdate : LiveData<Event<Resource<ResponseVersionUpdate>>> = _responseVersionUpdate

    private val _responseUserAttendance = MutableLiveData<Event<Resource<ResponseUserAttendance>>>()
    val responseUserAttendance : LiveData<Event<Resource<ResponseUserAttendance>>> = _responseUserAttendance

    private val _eMandateResponse = MutableLiveData<Event<Resource<ResponseBody>>>()
    val eMandateResponse : LiveData<Event<Resource<ResponseBody>>> = _eMandateResponse

    private val _eMandateStatusResponse = MutableLiveData<Event<Resource<ResponseBody>>>()
    val eMandateStatusResponse : LiveData<Event<Resource<ResponseBody>>> = _eMandateStatusResponse

    private val _createCollectionResponse = MutableLiveData<Event<Resource<ResponseCreateCollection>>>()
    val createCollectionResponse : LiveData<Event<Resource<ResponseCreateCollection>>> = _createCollectionResponse

    private val _getUserCollectionResponse = MutableLiveData<Event<Resource<ResponseUserCollection>>>()
    val getUserCollectionResponse : LiveData<Event<Resource<ResponseUserCollection>>> = _getUserCollectionResponse


    private val _responseLoanDetails = MutableLiveData<Event<Resource<ResponseLoanDetails>>>()
    val responseLoanDetails : LiveData<Event<Resource<ResponseLoanDetails>>> = _responseLoanDetails

    private val _responseLeadEmi = MutableLiveData<Event<Resource<ResponseLeadEMI>>>()
    val responseLeadEMI : LiveData<Event<Resource<ResponseLeadEMI>>> = _responseLeadEmi


    fun getLeadEmi(map: HashMap<String, String>){
        _responseLeadEmi.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getLeadEmi(map)
            _responseLeadEmi.postValue(Event(result))
        }
    }


    fun getLoanDetails(url:String){
        _responseLoanDetails.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getLoanDetails(url)
            _responseLoanDetails.postValue(Event(result))
        }
    }

    fun getUserCollection(map: HashMap<String, String>){
        _getUserCollectionResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getUserCollection(map)
            _getUserCollectionResponse.postValue(Event(result))
        }
    }
    fun createCollection(map:BodyCreateCollection){

        _createCollectionResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.createUserCollection(map)
            _createCollectionResponse.postValue(Event(result))
        }
    }
    fun launchEmandateStatus(url:String,userIdMap:HashMap<String,String>){

        _eMandateStatusResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.launchENachStatus(url,userIdMap)
            _eMandateStatusResponse.postValue(Event(result))
        }



    }

    fun getEMandateResponse(url:String){
        _eMandateResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.launchENach(url)
            _eMandateResponse.postValue(Event(result))
        }



    }

    fun checkAppVersion(){
        _responseVersionUpdate.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.checkVersionUpdate()
            _responseVersionUpdate.postValue(Event(result))
        }



    }
    fun createAttendance(map:BodyCreateAttendance){

        _responseCreateAttendance.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.addEmpAttendance(map)
            _responseCreateAttendance.postValue(Event(result))
        }
    }
    fun logEmp(map:BodyAdminLogin){

        _responseLogEmp.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.logEmp(map)
            _responseLogEmp.postValue(Event(result))
        }
    }
    fun getUserAttendance(map:HashMap<String,String>){

        _responseUserAttendance.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.viewUserAttendance(map)
            _responseUserAttendance.postValue(Event(result))
        }
    }


    fun checkUniqueLead(map:HashMap<String,String>){

        _responseCheckUniqueLead.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.checkUniqueLead(map)
            _responseCheckUniqueLead.postValue(Event(result))
        }


    }

    fun updateMpass(hashMap: HashMap<String,String>){
        _responseUpdateMpass.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.updateMpass(hashMap)
            _responseUpdateMpass.postValue(Event(result))
        }

    }
    fun submitKycRequestUser(applyKycUrl:String,imageFileAadharFront: File, imageFileAadharBack:File, imageFilePan:File, fileSelfie:File, eSign:File?, bankStatement:File?, kycDetails: KycDetails, userId: String){
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
        _responseApplyKyc.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.submitKYCRequest(applyKycUrl,filePart,filePartAadharBack,filePartPan,filePartSelfie,
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
            _responseApplyKyc.postValue(Event(result))
        }

    }


    fun createLoanUser(url:String,hashMap: HashMap<String,String>){
        _responseCreateLoanUser.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.createLoanUser(url,hashMap)
            _responseCreateLoanUser.postValue(Event(result))
        }

    }
    fun checkLoginUser(url:String,hashMap: HashMap<String,String>){
        _responseCheckLoginLoanUser.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.checkLoginLoanUser(url,hashMap)
            _responseCheckLoginLoanUser.postValue(Event(result))
        }

    }



    fun getUserApprovedLeads(hashMap: HashMap<String,String>){
        _responseUserApprovedLead.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getUserApprovedLeads(hashMap)
            _responseUserApprovedLead.postValue(Event(result))
        }

    }
    fun filterLeads(hashMap: HashMap<String,String>){
        _responseFilterLeads.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.filterLeads(hashMap)
            _responseFilterLeads.postValue(Event(result))
        }

    }
    fun filterVisits(hashMap: HashMap<String,String>){
        _responseFilterVisits.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.filterVisits(hashMap)
            _responseFilterVisits.postValue(Event(result))
        }

    }
    fun uploadFile(file: File){
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val image = MultipartBody.Part.createFormData("image_file", file.name, requestFile)
        _responseUploadFile.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = dashboardRepo.uploadImage(image)
            _responseUploadFile.postValue(Event(result))
        }
    }

    fun createUserLead(bodyCreateVisit: BodyCreateLead){

        _createLeadResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.createCustomerLead(bodyCreateVisit)
            _createLeadResponse.postValue(Event(result))
        }



    }
    fun createUserVisit(bodyCreateVisit: BodyCreateVisit){

        _createVisitResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.createCustomerVisit(bodyCreateVisit)
            _createVisitResponse.postValue(Event(result))
        }



    }

    fun getUserVisit(loginMap: HashMap<String,String>){

        _userVisitResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getUserVisits(loginMap)
            _userVisitResponse.postValue(Event(result))
        }



    }

    fun getUserLeads(loginMap: HashMap<String,String>){

        _userLeadResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.getUserLeads(loginMap)
            _userLeadResponse.postValue(Event(result))
        }



    }


    fun loginUser(loginMap: HashMap<String,String>){

        _userLoginResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main){
            val result = dashboardRepo.loginUser(loginMap)
            _userLoginResponse.postValue(Event(result))
        }



    }

}