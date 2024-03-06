package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ApplyLoanResponse( @SerializedName("status"  ) var status  : Boolean? = null,
                              @SerializedName("message" ) var message : String?  = null,
                              @SerializedName("loan_id" ) var loanId  : LoanId?  = LoanId()
){
    data class LoanId (

        @SerializedName("sub_admin_id"    ) var subAdminId     : String? = null,
        @SerializedName("user_id"         ) var userId         : String? = null,
        @SerializedName("type"            ) var type           : String? = null,
        @SerializedName("package_id"      ) var packageId      : String? = null,
        @SerializedName("emandate"        ) var emandate       : String? = null,
        @SerializedName("emandate_txn_id" ) var emandateTxnId  : String? = null,
        @SerializedName("status"          ) var status         : String? = null,
        @SerializedName("current_status"  ) var currentStatus  : String? = null,
        @SerializedName("amount"          ) var amount         : String? = null,
        @SerializedName("loan_document"   ) var loanDocument   : Int?    = null,
        @SerializedName("tenure"          ) var tenure         : String? = null,
        @SerializedName("insurance"       ) var insurance      : Int?    = null,
        @SerializedName("processing_fees" ) var processingFees : String? = null,
        @SerializedName("gst"             ) var gst            : String? = null,
        @SerializedName("interest"        ) var interest       : String? = null,
        @SerializedName("emi"             ) var emi            : String? = null,
        @SerializedName("emd_amt"         ) var emdAmt         : String? = null,
        @SerializedName("emd_month"       ) var emdMonth       : String? = null,
        @SerializedName("created_at"      ) var createdAt      : String? = null

    )
}
