package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ResponseUPIOrderStatus(@SerializedName("status" ) var status : Boolean? = null,
                                  @SerializedName("msg"    ) var msg    : String?  = null,
                                  @SerializedName("data"   ) var data   : Data?    = Data()
) {

    data class Merchant (
        @SerializedName("name"   ) var name  : String? = null,
        @SerializedName("upi_id" ) var upiId : String? = null
    )

    data class Data (

        @SerializedName("id"              ) var id             : Int?      = null,
        @SerializedName("customer_vpa"    ) var customerVpa    : String?   = null,
        @SerializedName("amount"          ) var amount         : Int?      = null,
        @SerializedName("client_txn_id"   ) var clientTxnId    : String?   = null,
        @SerializedName("customer_name"   ) var customerName   : String?   = null,
        @SerializedName("customer_email"  ) var customerEmail  : String?   = null,
        @SerializedName("customer_mobile" ) var customerMobile : String?   = null,
        @SerializedName("p_info"          ) var pInfo          : String?   = null,
        @SerializedName("upi_txn_id"      ) var upiTxnId       : String?   = null,
        @SerializedName("status"          ) var status         : String?   = null,
        @SerializedName("remark"          ) var remark         : String?   = null,
        @SerializedName("udf1"            ) var udf1           : String?   = null,
        @SerializedName("udf2"            ) var udf2           : String?   = null,
        @SerializedName("udf3"            ) var udf3           : String?   = null,
        @SerializedName("redirect_url"    ) var redirectUrl    : String?   = null,
        @SerializedName("txnAt"           ) var txnAt          : String?   = null,
        @SerializedName("createdAt"       ) var createdAt      : String?   = null,
        @SerializedName("Merchant"        ) var Merchant       : Merchant? = Merchant()

    )
}