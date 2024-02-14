package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class LoginUserResponse(@SerializedName("status"  ) var status  : String? = null,
                             @SerializedName("code"    ) var code    : Int?    = null,
                             @SerializedName("message" ) var message : String? = null,
                             @SerializedName("data"    ) var data    : Data?   = Data()) {
    data class Data (

        @SerializedName("userId"          ) var userId          : Int?    = null,
        @SerializedName("fullName"        ) var fullName        : String? = null,
        @SerializedName("telephoneNumber" ) var telephoneNumber : String? = null,
        @SerializedName("employeeId"      ) var employeeId      : String? = null,
        @SerializedName("createdAt"       ) var createdAt       : String? = null,
        @SerializedName("updatedAt"       ) var updatedAt       : String? = null,
        @SerializedName("mpass"           ) var mpass           : String? = null

    )
}