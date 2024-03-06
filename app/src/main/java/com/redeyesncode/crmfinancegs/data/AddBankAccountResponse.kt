package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class AddBankAccountResponse( @SerializedName("status" ) var status : Boolean? = null,
                                   @SerializedName("data"   ) var data   : Data?    = Data()) {
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
}