package com.redeyesncode.crmfinancegs.network

import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

//    @POST("api/otp_verification")
//    suspend fun otpVerification(@Body otpVerifyMap:HashMap<String,String>) : Response<OtpVerifyResponse>


    @POST("megma-crm/login-user")
    suspend fun loginUser(@Body loginUserMap:HashMap<String,String>) :Response<LoginUserResponse>


    @POST("megma-crm/get-user-leads")
    suspend fun getUserLeads(@Body loginUserMap:HashMap<String,String>) :Response<UserLeadResponse>
    @POST("megma-crm/get-user-visits")
    suspend fun getUserVisits(@Body loginUserMap:HashMap<String,String>) :Response<UserVisitResponse>
}