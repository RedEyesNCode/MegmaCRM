package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ResponseLoanDetails(@SerializedName("status"  ) var status  : Boolean? = null,
                               @SerializedName("Name"    ) var Name    : String?  = null,
                               @SerializedName("Mobile"  ) var Mobile  : String?  = null,
                               @SerializedName("amount"  ) var amount  : String?  = null,
                               @SerializedName("Penalty" ) var Penalty : String?  = null,
                               @SerializedName("emi_id"  ) var emiId   : Int?     = null
)
