package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ResponseUPIOrder(@SerializedName("status" ) var status : Boolean? = null,
                            @SerializedName("msg"    ) var msg    : String?  = null,
                            @SerializedName("data"   ) var data   : Data?    = Data()
){
    data class Data (

        @SerializedName("order_id"    ) var orderId    : Int?    = null,
        @SerializedName("payment_url" ) var paymentUrl : String? = null,
        @SerializedName("upi_id_hash" ) var upiIdHash  : String? = null

    )
}
