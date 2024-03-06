package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class CommonMessageResponse(@SerializedName("status"  ) var status  : Boolean? = null,
                                 @SerializedName("code" ) var code : String?  = null,
                                 @SerializedName("message" ) var message : String?  = null)
