package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class EMandateHTMLResponse(@SerializedName("StatusCode") val statusCode: String,
                                @SerializedName("data") val customerData: CustomerData,
                                @SerializedName("status") val status: Boolean){
    data class CustomerData(
        @SerializedName("id") val id: String,
        @SerializedName("colltn_amt") val collectionAmount: Int,
        @SerializedName("submitted_on") val submittedOn: String,
        @SerializedName("accptd") val acceptanceStatus: String,
        // Add more fields as needed
    )

    data class BankAccount(
        @SerializedName("account_holder_name") val accountHolderName: String,
        @SerializedName("bank_account_no") val bankAccountNumber: String,
        // Add more fields as needed
    )
}
