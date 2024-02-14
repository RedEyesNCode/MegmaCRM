package com.redeyesncode.crmfinancegs.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.repository.DefaultDashboardRepo
import com.redeyesncode.gsfinancenbfc.base.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val dashboardRepo: DefaultDashboardRepo): ViewModel() {

    private val _userLoginResponse = MutableLiveData<Event<Resource<LoginUserResponse>>>()
    val userLoginResponse : LiveData<Event<Resource<LoginUserResponse>>> = _userLoginResponse

    private val _userLeadResponse = MutableLiveData<Event<Resource<UserLeadResponse>>>()
    val userLeadResponse : LiveData<Event<Resource<UserLeadResponse>>> = _userLeadResponse


    private val _userVisitResponse = MutableLiveData<Event<Resource<UserVisitResponse>>>()
    val userVisitResponse : LiveData<Event<Resource<UserVisitResponse>>> = _userVisitResponse


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