package com.redeyesncode.crmfinancegs.repository

import com.google.gson.Gson
import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.androidtechnical.base.safeCall
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.FilterLeadsResponse
import com.redeyesncode.crmfinancegs.data.LoanUserLoginResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.moneyview.repository.DashboardRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

class DefaultDashboardRepo : DashboardRepo {

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