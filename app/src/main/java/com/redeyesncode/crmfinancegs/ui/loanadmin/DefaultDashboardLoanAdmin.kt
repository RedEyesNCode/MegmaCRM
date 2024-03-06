package com.redeyesncode.crmfinancegs.ui.loanadmin

import com.redeyesncode.androidtechnical.base.Resource
import com.redeyesncode.androidtechnical.base.safeCall
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
import com.redeyesncode.crmfinancegs.network.RetrofitInstanceLoanAdmin
import com.redeyesncode.crmfinancegs.data.ApplyLoanResponse
import com.redeyesncode.crmfinancegs.data.BodyUPIOrder
import com.redeyesncode.crmfinancegs.data.LoanInfo1Response
import com.redeyesncode.crmfinancegs.data.ResponseUPIOrder
import com.redeyesncode.crmfinancegs.data.ResponseUPIOrderStatus
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class  DefaultDashboardLoanAdmin :DashboardLoanAdmin {

    override suspend fun forceCloseLoan(emiInfoMap: HashMap<String, String>): Resource<CommonMessageResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).forceCloseLoan(emiInfoMap)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun payEmi(emiPayMap: HashMap<String, String>): Resource<CommonMessageResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).payEmi(emiPayMap)
                Resource.Success(response.body()!!)
            }
        }




    }

    override suspend fun checkUpiOrderStatus(
        url: String,
        mapUpiOrderStatus: HashMap<String, String>
    ): Resource<ResponseUPIOrderStatus> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).checkUpiOrderStatus(url,mapUpiOrderStatus)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun createUpiOrder(
        url: String,
        bodyUPIOrder: BodyUPIOrder
    ): Resource<ResponseUPIOrder> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).createUpiOrder(url,bodyUPIOrder)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun launchENachStatus(urlAddBank: String, userIdMap: HashMap<String, String>): Resource<ResponseBody> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).launchENachStatus(urlAddBank,userIdMap)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun loanInfo1(loanInfoMap: HashMap<String, String>): Resource<LoanInfo1Response> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).loanInfo1(loanInfoMap)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun loanApply(applyLoanMap: HashMap<String, String>): Resource<ApplyLoanResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).loanApply(applyLoanMap)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun getAllPosts(): Resource<ArrayList<PostsResponse>> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).getAllPosts()
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun getPostComment(post_id: String): Resource<ArrayList<PostCommentResponse>> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).getPostComment(post_id)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun loginUser(loginMap: HashMap<String, String>): Resource<LoginResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).loginUser(loginMap)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun otpVerification(otpVerifyMap: HashMap<String, String>): Resource<OtpVerifyResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).otpVerification(otpVerifyMap)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun registerUser(registerMap: HashMap<String, String>): Resource<CommonMessageResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).registerUser(registerMap)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun dashboard(dashboardMap: HashMap<String, String>): Resource<DashboardResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).dashboard(dashboardMap)
                Resource.Success(response.body()!!)
            }
        }
    }



    override suspend fun submitKYCRequest(
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
        bankStatement: MultipartBody.Part,

        ): Resource<CommonMessageResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).submitKYCRequest(aadharFront, adharBack, panCard, selfie, userId, firstName, lastName,dob, gender, email, pincode, userType, monthlySalary, relativeNumberName, relativeNumberTwoName, relativeNumberTwo, currentAddress, pan_number, name, relativeNumber,adhar_number,state,eSign,bankStatement)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun checkKycStatus(userIdMap: HashMap<String, String>): Resource<CheckKycResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).checkKycStatus(userIdMap)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun assignDefaultPackage(userIdMap: HashMap<String, String>): Resource<UserRegularPackageResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).assignDefaultPackage(userIdMap)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun assignCustomPackage(userIdMap: HashMap<String, String>): Resource<UserCustomPackageResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).assignCustomPackage(userIdMap)
                Resource.Success(response.body()!!)
            }
        }




    }

    override suspend fun loanInfo(loanInfoMap: HashMap<String, String>): Resource<LoanInfoResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).loanInfo(loanInfoMap)
                Resource.Success(response.body()!!)
            }
        }
    }

    override suspend fun showBankAccount(
        urlAddBank: String,
        loanInfoMap: HashMap<String, String>
    ): Resource<ShowBankAccountResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).showBankAccount(urlAddBank,loanInfoMap)
                Resource.Success(response.body()!!)
            }
        }

    }

    override suspend fun addBankAccount(
        urlAddBank: String,
        loanInfoMap: HashMap<String, String>
    ): Resource<AddBankAccountResponse> {

        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).addBankAccount(urlAddBank,loanInfoMap)
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
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).launchENach(urlAddBank)
                Resource.Success(response.body()!!)
            }
        }


    }

    override suspend fun emiInfo(emiInfoMap: HashMap<String, String>): Resource<EmiResponse> {
        return safeCall {
            safeCall {
                val response =
                    RetrofitInstanceLoanAdmin().provideApiService(RetrofitInstanceLoanAdmin().provideRetrofit()).emiInfo(emiInfoMap)
                Resource.Success(response.body()!!)
            }
        }


    }
}