package com.redeyesncode.crmfinancegs.network

import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

//    @POST("api/otp_verification")
//    suspend fun otpVerification(@Body otpVerifyMap:HashMap<String,String>) : Response<OtpVerifyResponse>


    @POST("megma-crm/login-user")
    suspend fun loginUser(@Body loginUserMap:HashMap<String,String>) :Response<LoginUserResponse>
}