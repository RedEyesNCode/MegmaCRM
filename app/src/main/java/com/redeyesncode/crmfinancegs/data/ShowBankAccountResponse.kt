package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ShowBankAccountResponse(@SerializedName("status"    ) var status   : Boolean?  = null,
                                   @SerializedName("data"      ) var data     : Data?     = Data(),
                                   @SerializedName("loan_info" ) var loanInfo : LoanInfo? = LoanInfo()
){
    data class Data (

        @SerializedName("id"              ) var id            : Int?    = null,
        @SerializedName("user_id"         ) var userId        : Int?    = null,
        @SerializedName("acc_holder_name" ) var accHolderName : String? = null,
        @SerializedName("acc_no"          ) var accNo         : String? = null,
        @SerializedName("bank_name"       ) var bankName      : String? = null,
        @SerializedName("ifsc"            ) var ifsc          : String? = null,
        @SerializedName("created_at"      ) var createdAt     : String? = null,
        @SerializedName("updated_at"      ) var updatedAt     : String? = null

    )

    data class LoanInfo (

        @SerializedName("status"         ) var status        : String? = null,
        @SerializedName("current_status" ) var currentStatus : String? = null

    )
}
