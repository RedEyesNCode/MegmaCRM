package com.redeyesncode.crmfinancegs.network

import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.FilterLeadsResponse
import com.redeyesncode.crmfinancegs.data.LoanUserLoginResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface ApiInterface {

//    @POST("api/otp_verification")
//    suspend fun otpVerification(@Body otpVerifyMap:HashMap<String,String>) : Response<OtpVerifyResponse>


    @POST("megma-crm/login-user")
    suspend fun loginUser(@Body loginUserMap:HashMap<String,String>) :Response<LoginUserResponse>


    @POST("megma-crm/get-user-leads")
    suspend fun getUserLeads(@Body loginUserMap:HashMap<String,String>) :Response<UserLeadResponse>
    @POST("megma-crm/get-user-visits")
    suspend fun getUserVisits(@Body loginUserMap:HashMap<String,String>) :Response<UserVisitResponse>



    @POST("megma-crm/update-mpass")
    suspend fun updateMpass(@Body updateMPass:HashMap<String,String>):Response<CommonMessageResponse>

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

    @POST
    suspend fun createLoanUser(@Url signUpLoanUrl:String,@Body hashMap: HashMap<String, String>):Response<LoanUserLoginResponse>
    @POST
    suspend fun checkLoginLoanUser(@Url signUpLoanUrl:String,@Body hashMap: HashMap<String, String>):Response<LoanUserLoginResponse>


    @POST("megma-crm/check-unique-lead")
    suspend fun checkUniqueLead(@Body map:HashMap<String,String>):Response<CommonMessageResponse>

    @Multipart
    @POST
    suspend fun submitKYCRequest(
        @Url applyKycUrl:String,
        @Part aadharFront: MultipartBody.Part,
                                 @Part adharBack:MultipartBody.Part,
                                 @Part panCard:MultipartBody.Part,
                                 @Part selfie:MultipartBody.Part,
                                 @Part("id") userId: RequestBody,
                                 @Part("firstname") firstName: RequestBody,
                                 @Part("lastname")lastName: RequestBody,
                                 @Part("dob") dob: RequestBody,
                                 @Part("gender") gender: RequestBody,
                                 @Part("email") email: RequestBody,
                                 @Part("pincode") pincode: RequestBody,
                                 @Part("user_type") userType: RequestBody,
                                 @Part("monthlySalary") monthlySalary: RequestBody,
                                 @Part("relativeNumberName") relativeNumberName: RequestBody,
                                 @Part("relativeNumberTwoName") relativeNumberTwoName: RequestBody,
                                 @Part("relativeNumberTwo") relativeNumberTwo: RequestBody,
                                 @Part("currentAddress") currentAddress: RequestBody,
                                 @Part("pan_number") pan_number: RequestBody,
                                 @Part("name") name: RequestBody,
                                 @Part("adhar_number") adhar_number: RequestBody,
                                 @Part("relativeNumber") relativeNumber: RequestBody,
                                 @Part("state") state: RequestBody,
                                 @Part eSign:MultipartBody.Part,
                                 @Part bankStatement:MultipartBody.Part,



                                 ):Response<CommonMessageResponse>


    @Multipart
    @POST("megma-crm/upload-file")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<CommonMessageResponse>


}