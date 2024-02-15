package com.redeyesncode.moneyview.repository



import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body

interface DashboardRepo {

    suspend fun loginUser( loginUserMap:HashMap<String,String>) :Resource<LoginUserResponse>
    suspend fun getUserLeads( loginUserMap:HashMap<String,String>) :Resource<UserLeadResponse>
    suspend fun getUserVisits( loginUserMap:HashMap<String,String>) : Resource<UserVisitResponse>

    suspend fun createCustomerLead( bodyCreateVisit: BodyCreateLead):Resource<CommonMessageResponse>


    suspend fun createCustomerVisit( bodyCreateVisit: BodyCreateVisit):Resource<CommonMessageResponse>
    suspend fun uploadImage(
         image: MultipartBody.Part
    ): Resource<CommonMessageResponse>

}