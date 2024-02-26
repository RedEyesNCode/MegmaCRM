package com.redeyesncode.crmfinancegs.network

import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.FilterLeadsResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {

//    @POST("api/otp_verification")
//    suspend fun otpVerification(@Body otpVerifyMap:HashMap<String,String>) : Response<OtpVerifyResponse>


    @POST("megma-crm/login-user")
    suspend fun loginUser(@Body loginUserMap:HashMap<String,String>) :Response<LoginUserResponse>


    @POST("megma-crm/get-user-leads")
    suspend fun getUserLeads(@Body loginUserMap:HashMap<String,String>) :Response<UserLeadResponse>
    @POST("megma-crm/get-user-visits")
    suspend fun getUserVisits(@Body loginUserMap:HashMap<String,String>) :Response<UserVisitResponse>



    @POST("megma-crm/create-visit")
    suspend fun createCustomerVisit(@Body bodyCreateVisit: BodyCreateVisit):Response<CommonMessageResponse>

    @POST("megma-crm/create-lead")
    suspend fun createCustomerLead(@Body bodyCreateVisit: BodyCreateLead):Response<CommonMessageResponse>


    @POST("megma-crm/filter-leads")
    suspend fun filterLeads(@Body hashMap: HashMap<String,String>):Response<FilterLeadsResponse>
    @POST("megma-crm/filter-visits")
    suspend fun filterVisits(@Body hashMap: HashMap<String,String>):Response<UserVisitResponse>

    @POST("megma-crm/get-user-approved-leads")
    suspend fun getUserApprovedLeads(@Body hashMap: HashMap<String, String>):Response<UserLeadResponse>

    @Multipart
    @POST("megma-crm/upload-file")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<CommonMessageResponse>


}