package com.redeyesncode.crmfinancegs.ui.loanadmin

import com.redeyesncode.androidtechnical.base.Resource
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

interface DashboardLoanAdmin {
    suspend fun getAllPosts(): Resource<ArrayList<PostsResponse>>
    suspend fun getPostComment(post_id:String): Resource<ArrayList<PostCommentResponse>>

    suspend fun loginUser(loginMap :HashMap<String,String>) : Resource<LoginResponse>
    suspend fun otpVerification( otpVerifyMap:HashMap<String,String>) : Resource<OtpVerifyResponse>
    suspend fun registerUser( registerMap:HashMap<String,String>) : Resource<CommonMessageResponse>
    suspend fun dashboard( dashboardMap:HashMap<String,String>) : Resource<DashboardResponse>


    suspend fun checkKycStatus( userIdMap:HashMap<String,String>) : Resource<CheckKycResponse>

    suspend fun assignDefaultPackage( userIdMap:HashMap<String,String>) : Resource<UserRegularPackageResponse>

    suspend fun assignCustomPackage( userIdMap:HashMap<String,String>) : Resource<UserCustomPackageResponse>

    suspend fun loanInfo( loanInfoMap:HashMap<String,String>) : Resource<LoanInfoResponse>
    suspend fun loanInfo1( loanInfoMap:HashMap<String,String>) : Resource<LoanInfo1Response>

    suspend fun addBankAccount( urlAddBank:String, loanInfoMap:HashMap<String,String>) : Resource<AddBankAccountResponse>

    suspend fun showBankAccount( urlAddBank:String,  loanInfoMap:HashMap<String,String>) : Resource<ShowBankAccountResponse>

    suspend fun launchENach( urlAddBank:String) : Resource<ResponseBody>

    suspend fun emiInfo( emiInfoMap:HashMap<String,String>) : Resource<EmiResponse>

    suspend fun loanApply( applyLoanMap:HashMap<String,String>): Resource<ApplyLoanResponse>

    suspend fun createUpiOrder( url:String, bodyUPIOrder: BodyUPIOrder): Resource<ResponseUPIOrder>
    suspend fun payEmi(emiPayMap: HashMap<String,String>): Resource<CommonMessageResponse>
    suspend fun checkUpiOrderStatus( url:String, mapUpiOrderStatus: HashMap<String,String>): Resource<ResponseUPIOrderStatus>



    //    suspend fun getTokenEMandate(urlEmandateToken:String):Response<EMandateTokenResponse>
    suspend fun launchENachStatus( urlAddBank:String, userIdMap: HashMap<String, String>) : Resource<ResponseBody>
    suspend fun forceCloseLoan( emiInfoMap:HashMap<String,String>) : Resource<CommonMessageResponse>

    suspend fun submitKYCRequest(aadharFront: MultipartBody.Part,
                                 adharBack: MultipartBody.Part,
                                 panCard: MultipartBody.Part,
                                 selfie: MultipartBody.Part,
                                 userId: RequestBody,
                                 firstName: RequestBody,
                                 lastName: RequestBody,
                                 dob: RequestBody,
                                 gender: RequestBody,
                                 email: RequestBody,
                                 pincode: RequestBody,
                                 userType: RequestBody,
                                 monthlySalary: RequestBody,
                                 relativeNumberName: RequestBody,
                                 relativeNumberTwoName: RequestBody,
                                 relativeNumberTwo: RequestBody,
                                 currentAddress: RequestBody,
                                 pan_number: RequestBody,
                                 name: RequestBody,
                                 relativeNumber: RequestBody,
                                 adhar_number: RequestBody,
                                 state: RequestBody,
                                 eSign: MultipartBody.Part,
                                 bankStatement: MultipartBody.Part,


                                 ): Resource<CommonMessageResponse>
}