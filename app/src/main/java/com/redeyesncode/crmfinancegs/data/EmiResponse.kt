package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class EmiResponse(
    @SerializedName("status" ) var status : Boolean?        = null,
    @SerializedName("data"   ) var data   : ArrayList<Data> = arrayListOf()){

    data class Data (

        @SerializedName("id"          ) var id         : Int?    = null,
        @SerializedName("amount"      ) var amount     : String? = null,
        @SerializedName("expiry_date" ) var expiryDate : String? = null,
        @SerializedName("status"      ) var status     : String? = null

    )
}
