package com.redeyesncode.crmfinancegs.repository

import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.androidtechnical.base.safeCall
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.moneyview.repository.DashboardRepo

class DefaultDashboardRepo : DashboardRepo {


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