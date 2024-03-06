package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class BodyCreateVisit(@SerializedName("customerName" ) var customerName : String? = null,
                           @SerializedName("address"      ) var address      : String? = null,
                           @SerializedName("photo"        ) var photo        : String? = null,
                           @SerializedName("latitude"     ) var latitude     : Double? = null,
                           @SerializedName("customerNumber"     ) var customerNumber     : String? = null,
                           @SerializedName("remark"     ) var remark     : String? = null,
                           @SerializedName("longitude"    ) var longitude    : Double? = null,
                           @SerializedName("userId"       ) var userId       : String? = null)
