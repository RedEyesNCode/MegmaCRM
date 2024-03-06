package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class LoanInfo1Response(@SerializedName("status" ) var status : Boolean? = null,
                             @SerializedName("data"   ) var data   : Data?    = Data()
) {
    data class Data (

        @SerializedName("loan_id"        ) var loanId        : Int?    = null,
        @SerializedName("current_status" ) var currentStatus : String? = null,
        @SerializedName("status" ) var status : String? = null,
        @SerializedName("amount"         ) var amount        : String? = null

    )
}