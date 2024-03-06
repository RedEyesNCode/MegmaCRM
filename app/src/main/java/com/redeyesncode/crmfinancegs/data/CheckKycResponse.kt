package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class CheckKycResponse(@SerializedName("status"  ) var status  : String? = null,
                            @SerializedName("message" ) var message : String? = null,
                            @SerializedName("data"    ) var data    : Data?   = Data()
)
{
    data class Data (

        @SerializedName("user_id"      ) var userId      : Int?    = null,
        @SerializedName("kyc_approval" ) var kycApproval : String? = null,
        @SerializedName("kyc_reason"   ) var kycReason   : String? = null

    )
}
