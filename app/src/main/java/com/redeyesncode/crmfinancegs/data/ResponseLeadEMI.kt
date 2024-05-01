package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ResponseLeadEMI(@SerializedName("status"  ) var status  : String? = null,
                           @SerializedName("code"    ) var code    : Int?    = null,
                           @SerializedName("message" ) var message : String? = null,
                           @SerializedName("data"    ) var data    : Double? = null)
