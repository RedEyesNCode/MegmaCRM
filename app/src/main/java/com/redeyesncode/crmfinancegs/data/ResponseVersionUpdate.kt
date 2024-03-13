package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ResponseVersionUpdate( @SerializedName("status"  ) var status  : String? = null,
                                  @SerializedName("code"    ) var code    : Int?    = null,
                                  @SerializedName("message" ) var message : String? = null,
                                  @SerializedName("data"    ) var data    : Data?   = Data()
){
    data class Data (

        @SerializedName("appVersionId"   ) var appVersionId   : Int?    = null,
        @SerializedName("appVersionName" ) var appVersionName : String? = null,
        @SerializedName("appVersionCode" ) var appVersionCode : String? = null,
        @SerializedName("createdAt"      ) var createdAt      : String? = null

    )
}
