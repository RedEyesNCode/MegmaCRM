package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class UserVisitResponse(@SerializedName("status"  ) var status  : String?         = null,
                             @SerializedName("code"    ) var code    : Int?            = null,
                             @SerializedName("message" ) var message : String?         = null,
                             @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()){
    data class Data (

        @SerializedName("visitId"      ) var visitId      : Int?    = null,
        @SerializedName("customerName" ) var customerName : String? = null,
        @SerializedName("address"      ) var address      : String? = null,
        @SerializedName("photo"        ) var photo        : String? = null,
        @SerializedName("latitude"     ) var latitude     : Double? = null,
        @SerializedName("longitude"    ) var longitude    : Double? = null

    )
}
