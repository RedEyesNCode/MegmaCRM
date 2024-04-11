package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ResponseCreateCollection( @SerializedName("status"  ) var status  : String? = null,
                                     @SerializedName("code"    ) var code    : Int?    = null,
                                     @SerializedName("message" ) var message : String? = null,
                                     @SerializedName("data"    ) var data    : Data?   = Data()
){
    data class User (

        @SerializedName("userId"          ) var userId          : Int?    = null,
        @SerializedName("fullName"        ) var fullName        : String? = null,
        @SerializedName("telephoneNumber" ) var telephoneNumber : String? = null,
        @SerializedName("employeeId"      ) var employeeId      : String? = null,
        @SerializedName("createdAt"       ) var createdAt       : String? = null,
        @SerializedName("updatedAt"       ) var updatedAt       : String? = null,
        @SerializedName("mpass"           ) var mpass           : String? = null

    )
    data class Data (

        @SerializedName("collectionId"        ) var collectionId       : Int?    = null,
        @SerializedName("fullName"            ) var fullName           : String? = null,
        @SerializedName("gs_loan_number"      ) var gsLoanNumber       : String? = null,
        @SerializedName("gs_loan_password"    ) var gsLoanPassword     : String? = null,
        @SerializedName("gs_loan_userid"      ) var gsLoanUserid       : String? = null,
        @SerializedName("collection_amount"   ) var collectionAmount   : String? = null,
        @SerializedName("collection_location" ) var collectionLocation : String? = null,
        @SerializedName("collection_address"  ) var collectionAddress  : String? = null,
        @SerializedName("createdAt"           ) var createdAt          : String? = null,
        @SerializedName("updatedAt"           ) var updatedAt          : String? = null,
        @SerializedName("collectionStatus"    ) var collectionStatus   : String? = null,
        @SerializedName("user"                ) var user               : User?   = User()

    )
}
