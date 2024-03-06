package com.redeyesncode.crmfinancegs.network

import com.redeyesncode.crmfinancegs.data.CheckKycResponse
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.DashboardResponse
import com.redeyesncode.crmfinancegs.data.EmiResponse
import com.redeyesncode.crmfinancegs.data.LoanInfoResponse
import com.redeyesncode.crmfinancegs.data.LoginResponse
import com.redeyesncode.crmfinancegs.data.OtpVerifyResponse
import com.redeyesncode.crmfinancegs.data.PostCommentResponse
import com.redeyesncode.crmfinancegs.data.PostsResponse
import com.redeyesncode.crmfinancegs.data.ShowBankAccountResponse
import com.redeyesncode.crmfinancegs.data.UserCustomPackageResponse
import com.redeyesncode.crmfinancegs.data.UserRegularPackageResponse
import com.redeyesncode.crmfinancegs.data.AddBankAccountResponse
import com.redeyesncode.crmfinancegs.data.ApplyLoanResponse
import com.redeyesncode.crmfinancegs.data.BodyUPIOrder
import com.redeyesncode.crmfinancegs.data.LoanInfo1Response
import com.redeyesncode.crmfinancegs.data.ResponseUPIOrder
import com.redeyesncode.crmfinancegs.data.ResponseUPIOrderStatus
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiInterfaceLoanAdmin {
    @GET("posts")
    suspend fun getAllPosts(): Response<ArrayList<PostsResponse>>

    @GET("posts/{post_id}/comments")
    suspend fun getPostComment(
        @Path("post_id") post_id: String,
    ): Response<ArrayList<PostCommentResponse>>

    @POST("api/login")
    suspend fun loginUser(@Body loginMap :HashMap<String,String>) : Response<LoginResponse>

    @POST("api/otp_verification")
    suspend fun otpVerification(@Body otpVerifyMap:HashMap<String,String>) : Response<OtpVerifyResponse>


    @POST("api/registernew")
    suspend fun registerUser(@Body registerMap:HashMap<String,String>) : Response<CommonMessageResponse>


    @POST("api/dashboard")
    suspend fun dashboard(@Body dashboardMap:HashMap<String,String>) : Response<DashboardResponse>



    @Multipart
    @POST("api/kyc")
    suspend fun submitKYCRequest(@Part aadharFront: MultipartBody.Part,
                                 @Part adharBack: MultipartBody.Part,
                                 @Part panCard: MultipartBody.Part,
                                 @Part selfie: MultipartBody.Part,
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
                                 @Part eSign: MultipartBody.Part,
                                 @Part bankStatement: MultipartBody.Part,



                                 ): Response<CommonMessageResponse>



    @POST("api/checkKYCStatus")
    suspend fun checkKycStatus(@Body userIdMap:HashMap<String,String>) : Response<CheckKycResponse>


    @POST("api/assign_default_package")
    suspend fun assignDefaultPackage(@Body userIdMap:HashMap<String,String>) : Response<UserRegularPackageResponse>

    @POST("api/assign_custom_package")
    suspend fun assignCustomPackage(@Body userIdMap:HashMap<String,String>) : Response<UserCustomPackageResponse>


    @POST("api/loan_info2")
    suspend fun loanInfo(@Body loanInfoMap:HashMap<String,String>) : Response<LoanInfoResponse>

    @POST("api/loan_info1")
    suspend fun loanInfo1(@Body loanInfoMap:HashMap<String,String>) : Response<LoanInfo1Response>

    @POST
    suspend fun addBankAccount(@Url urlAddBank:String, @Body loanInfoMap:HashMap<String,String>) : Response<AddBankAccountResponse>


    @POST
    suspend fun showBankAccount(@Url urlAddBank:String, @Body loanInfoMap:HashMap<String,String>) : Response<ShowBankAccountResponse>


    @POST
    suspend fun launchENach(@Url urlAddBank:String) : Response<ResponseBody>


    @POST
    suspend fun launchENachStatus(@Url urlAddBank:String, @Body userIdMap: HashMap<String, String>) : Response<ResponseBody>



    // HIT E MANDATE API.


    @POST("api/emi_info")
    suspend fun emiInfo(@Body emiInfoMap:HashMap<String,String>) : Response<EmiResponse>
    @POST("api/pay_emi")
    suspend fun payEmi(@Body emiInfoMap:HashMap<String,String>) : Response<CommonMessageResponse>
    @POST("api/forcecloseloan")
    suspend fun forceCloseLoan(@Body emiInfoMap:HashMap<String,String>) : Response<CommonMessageResponse>



    @POST("api/loan_apply")
    suspend fun loanApply(@Body applyLoanMap:HashMap<String,String>): Response<ApplyLoanResponse>


    @POST
    suspend fun createUpiOrder(@Url url:String, @Body bodyUPIOrder: BodyUPIOrder): Response<ResponseUPIOrder>


    @POST
    suspend fun checkUpiOrderStatus(@Url url:String, @Body mapUpiOrderStatus: HashMap<String,String>): Response<ResponseUPIOrderStatus>
}