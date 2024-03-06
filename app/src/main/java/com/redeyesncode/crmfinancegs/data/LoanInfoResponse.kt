package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class LoanInfoResponse(@SerializedName("status"              ) var status            : Boolean? = null,
                            @SerializedName("loan_status"         ) var loanStatus        : String?  = null,
                            @SerializedName("loan_current_status" ) var loanCurrentStatus : String?  = null,
                            @SerializedName("data"                ) var data              : Data?    = Data()
){
    data class Data (

        @SerializedName("loan_amount"    ) var loanAmount    : String? = null,
        @SerializedName("loan_id"    ) var loan_id    : String? = null,
        @SerializedName("tenure"         ) var tenure        : String? = null,
        @SerializedName("ttb"            ) var ttb           : String?    = null,
        @SerializedName("total_fee"      ) var totalFee      : String?    = null,
        @SerializedName("total_interest" ) var totalInterest : String?    = null,
        @SerializedName("insurance"      ) var insurance     : String?    = null,
        @SerializedName("fee_charge"     ) var feeCharge     : String?    = null,
        @SerializedName("processing_fee" ) var processingFee : String? = null,
        @SerializedName("loan_document"  ) var loanDocument  : String?    = null,
        @SerializedName("csr"            ) var csr           : String?    = null,
        @SerializedName("gst"            ) var gst           : String?    = null,
        @SerializedName("day_month"      ) var dayMonth      : String? = null

    )
}
