package com.redeyesncode.moneyview.repository



import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Url

interface DashboardRepo {

    suspend fun loginUser( loginUserMap:HashMap<String,String>) :Resource<LoginUserResponse>

}