package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class BodyUPIOrder(@SerializedName("key"             ) var key            : String? = null,
                        @SerializedName("client_txn_id"   ) var clientTxnId    : String? = null,
                        @SerializedName("amount"          ) var amount         : String? = null,
                        @SerializedName("p_info"          ) var pInfo          : String? = null,
                        @SerializedName("customer_name"   ) var customerName   : String? = null,
                        @SerializedName("customer_email"  ) var customerEmail  : String? = null,
                        @SerializedName("customer_mobile" ) var customerMobile : String? = null,
                        @SerializedName("redirect_url"    ) var redirectUrl    : String? = null,
                        @SerializedName("udf1"            ) var udf1           : String? = null,
                        @SerializedName("udf2"            ) var udf2           : String? = null,
                        @SerializedName("udf3"            ) var udf3           : String? = null)
