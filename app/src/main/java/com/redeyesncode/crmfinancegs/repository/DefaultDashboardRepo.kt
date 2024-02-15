package com.redeyesncode.crmfinancegs.repository

import com.google.gson.Gson
import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.androidtechnical.base.safeCall
import com.redeyesncode.crmfinancegs.data.BodyCreateLead
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.moneyview.repository.DashboardRepo
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response

class DefaultDashboardRepo : DashboardRepo {


    override suspend fun createCustomerLead(bodyCreateVisit: BodyCreateLead): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).createCustomerLead(bodyCreateVisit)
                Resource.Success(response.body()!!)
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