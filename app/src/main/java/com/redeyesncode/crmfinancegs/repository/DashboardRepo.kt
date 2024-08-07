package com.redeyesncode.moneyview.repository



import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.crmfinancegs.data.BodyAdminLogin
import com.redeyesncode.crmfinancegs.data.BodyCreateAttendance
import com.redeyesncode.crmfinancegs.data.BodyCreateCollection
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.FilterLeadsResponse
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Url

interface DashboardRepo {
    suspend fun launchENach( urlAddBank:String) :Resource<ResponseBody>
    suspend fun launchENachStatus( urlAddBank:String, userIdMap: HashMap<String, String>) :Resource<ResponseBody>

    suspend fun getLeadEmi( map: HashMap<String, String>):Resource<ResponseLeadEMI>

    suspend fun getLoanDetails( url:String):Resource<ResponseLoanDetails>
    suspend fun createUserCollection( bodyCreateCollection: BodyCreateCollection):Resource<ResponseCreateCollection>
    suspend fun getUserCollection( hashMap: HashMap<String, String>):Resource<ResponseUserCollection>
    suspend fun addEmpAttendance( map: BodyCreateAttendance):Resource<CommonMessageResponse>
    suspend fun viewUserAttendance( map:HashMap<String,String>):Resource<ResponseUserAttendance>
    suspend fun logEmp( map: BodyAdminLogin):Resource<CommonMessageResponse>
    suspend fun checkVersionUpdate():Resource<ResponseVersionUpdate>

    suspend fun updateMpass( updateMPass:HashMap<String,String>):Resource<CommonMessageResponse>
    suspend fun loginUser( loginUserMap:HashMap<String,String>) :Resource<LoginUserResponse>
    suspend fun getUserLeads( loginUserMap:HashMap<String,String>) :Resource<UserLeadResponse>
    suspend fun getUserVisits( loginUserMap:HashMap<String,String>) : Resource<UserVisitResponse>
    suspend fun filterLeads( hashMap: HashMap<String,String>):Resource<FilterLeadsResponse>

    suspend fun createCustomerLead( bodyCreateVisit: BodyCreateLead):Resource<CommonMessageResponse>
    suspend fun getUserApprovedLeads( hashMap: HashMap<String, String>):Resource<UserLeadResponse>
    suspend fun filterVisits( hashMap: HashMap<String,String>):Resource<UserVisitResponse>

    suspend fun createLoanUser(signUpLoanUrl:String, hashMap: HashMap<String, String>):Resource<LoanUserLoginResponse>

    suspend fun checkLoginLoanUser( signUpLoanUrl:String,  hashMap: HashMap<String, String>):Resource<LoanUserLoginResponse>


    suspend fun checkUniqueLead( map:HashMap<String,String>):Resource<CommonMessageResponse>


    suspend fun submitKYCRequest(
        applyKycUrl:String,

        aadharFront: MultipartBody.Part,
                                 adharBack:MultipartBody.Part,
                                 panCard:MultipartBody.Part,
                                 selfie:MultipartBody.Part,
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
                                 eSign:MultipartBody.Part,
                                 bankStatement:MultipartBody.Part,


                                 ):Resource<CommonMessageResponse>
    suspend fun createCustomerVisit( bodyCreateVisit: BodyCreateVisit):Resource<CommonMessageResponse>
    suspend fun uploadImage(
         image: MultipartBody.Part
    ): Resource<CommonMessageResponse>

}