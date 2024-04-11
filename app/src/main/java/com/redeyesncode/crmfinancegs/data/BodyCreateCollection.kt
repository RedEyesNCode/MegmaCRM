package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class BodyCreateCollection(@SerializedName("user_id"             ) var userId             : String? = null,
                                @SerializedName("fullName"            ) var fullName           : String? = null,
                                @SerializedName("gs_loan_number"      ) var gsLoanNumber       : String? = null,
                                @SerializedName("gs_loan_password"    ) var gsLoanPassword     : String? = null,
                                @SerializedName("gs_loan_userid"      ) var gsLoanUserid       : String? = null,
                                @SerializedName("collection_amount"   ) var collectionAmount   : String? = null,
                                @SerializedName("collection_location" ) var collectionLocation : String? = null,
                                @SerializedName("collection_address"  ) var collectionAddress  : String? = null)
