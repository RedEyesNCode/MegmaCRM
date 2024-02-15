package com.redeyesncode.crmfinancegs.repository

import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.androidtechnical.base.safeCall
import com.redeyesncode.crmfinancegs.data.BodyCreateVisit
import com.redeyesncode.crmfinancegs.data.CommonMessageResponse
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.data.UserVisitResponse
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.moneyview.repository.DashboardRepo
import okhttp3.MultipartBody
import retrofit2.Response

class DefaultDashboardRepo : DashboardRepo {


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
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun loginUser(loginUserMap: HashMap<String, String>): Resource<LoginUserResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstance().provideApiService(RetrofitInstance().provideRetrofit()).loginUser(loginUserMap)
                Resource.Success(response.body()!!)
            }
        }
    }
}