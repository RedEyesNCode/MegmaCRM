package com.redeyesncode.crmfinancegs.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.FilterLeadsResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.repository.DefaultDashboardRepo
import com.redeyesncode.gsfinancenbfc.base.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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