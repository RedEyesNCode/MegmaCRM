package com.redeyesncode.crmfinancegs.repository

import com.google.gson.Gson
import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.androidtechnical.base.safeCall
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
import com.redeyesncode.crmfinancegs.data.ResponseLoanDetails
import com.redeyesncode.crmfinancegs.data.ResponseUserAttendance
import com.redeyesncode.crmfinancegs.data.ResponseUserCollection
import com.redeyesncode.crmfinancegs.data.ResponseVersionUpdate
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.moneyview.repository.DashboardRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class DefaultDashboardRepo : DashboardRepo {

    override suspend fun getLoanDetails(url: String): Resource<ResponseLoanDetails> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).getLoanDetails(url)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun getUserCollection(hashMap: HashMap<String, String>): Resource<ResponseUserCollection> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).getUserCollection(hashMap)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun createUserCollection(bodyCreateCollection: BodyCreateCollection): Resource<ResponseCreateCollection> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).createUserCollection(bodyCreateCollection)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun launchENach(
        urlAddBank: String,
    ): Resource<ResponseBody> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).launchENach(urlAddBank)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun launchENachStatus(urlAddBank: String, userIdMap: HashMap<String, String>): Resource<ResponseBody> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).launchENachStatus(urlAddBank,userIdMap)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun checkVersionUpdate(): Resource<ResponseVersionUpdate> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).checkVersionUpdate()

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }

    }

    override suspend fun logEmp(map: BodyAdminLogin): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).logEmp(map)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }

    }

    override suspend fun addEmpAttendance(map: BodyCreateAttendance): Resource<CommonMessageResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).addEmpAttendance(map)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }


    }

    override suspend fun viewUserAttendance(map: HashMap<String, String>): Resource<ResponseUserAttendance> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).viewUserAttendance(map)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }
    }

    override suspend fun checkUniqueLead(map: HashMap<String, String>): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).checkUniqueLead(map)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }

    }

    override suspend fun updateMpass(updateMPass: HashMap<String, String>): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).updateMpass(updateMPass)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }
    }

    override suspend fun checkLoginLoanUser(
        signUpLoanUrl: String,
        hashMap: HashMap<String, String>
    ): Resource<LoanUserLoginResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).checkLoginLoanUser(signUpLoanUrl,hashMap)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }



    }

    override suspend fun submitKYCRequest(
        applyKycUrl: String,
        aadharFront: MultipartBody.Part,
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
        bankStatement: MultipartBody.Part
    ): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).submitKYCRequest(applyKycUrl,aadharFront, adharBack, panCard, selfie, userId, firstName, lastName,dob, gender, email, pincode, userType, monthlySalary, relativeNumberName, relativeNumberTwoName, relativeNumberTwo, currentAddress, pan_number, name, relativeNumber,adhar_number,state,eSign,bankStatement)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun createLoanUser(
        signUpLoanUrl: String,
        hashMap: HashMap<String, String>
    ): Resource<LoanUserLoginResponse> {


        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).createLoanUser(signUpLoanUrl,hashMap)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }


    }

    override suspend fun filterVisits(hashMap: HashMap<String, String>): Resource<UserVisitResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).filterVisits(hashMap)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }

    }

    override suspend fun getUserApprovedLeads(hashMap: HashMap<String, String>): Resource<UserLeadResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).getUserApprovedLeads(hashMap)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }

    }

    override suspend fun filterLeads(hashMap: HashMap<String, String>): Resource<FilterLeadsResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).filterLeads(hashMap)
                if(response.isSuccessful){

                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }


        }
    }

    override suspend fun createCustomerLead(bodyCreateVisit: BodyCreateLead): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).createCustomerLead(bodyCreateVisit)
                Resource.Success(response.body()!!)
                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }


    }

    override suspend fun uploadImage(image: MultipartBody.Part): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).uploadImage(image)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun createCustomerVisit(bodyCreateVisit: BodyCreateVisit): Resource<CommonMessageResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).createCustomerVisit(bodyCreateVisit)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun getUserVisits(loginUserMap: HashMap<String, String>): Resource<UserVisitResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).getUserVisits(loginUserMap)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun getUserLeads(loginUserMap: HashMap<String, String>): Resource<UserLeadResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).getUserLeads(loginUserMap)


                if(response.isSuccessful){
                    Resource.Success(response.body()!!)
                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }
    }

    override suspend fun loginUser(loginUserMap: HashMap<String, String>): Resource<LoginUserResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).loginUser(loginUserMap)

                if(response.isSuccessful){
                    Resource.Success(response.body()!!)

                }else{
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody!!)

                    val status = errorJson.getString("status")
                    val code = errorJson.getInt("code")
                    val message = errorJson.getString("message")

                    Resource.Error("Status: $status, Code: $code, Message: $message")

                }
            }
        }
    }
}