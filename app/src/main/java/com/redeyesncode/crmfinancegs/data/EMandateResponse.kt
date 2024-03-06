package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class EMandateResponse(@SerializedName("StatusCode" ) var StatusCode : String? = null,
                            @SerializedName("data"       ) var data       : Data?   = Data(),
                            @SerializedName("StatusDesc" ) var StatusDesc : String? = null
){
    data class BankAccounts (

        @SerializedName("account_holder_name" ) var accountHolderName : String? = null,
        @SerializedName("bank_account_no"     ) var bankAccountNo     : String? = null,
        @SerializedName("auth_type"           ) var authType          : String? = null,
        @SerializedName("account_type"        ) var accountType       : String? = null,
        @SerializedName("pancard"             ) var pancard           : String? = null,
        @SerializedName("ifsc_code"           ) var ifscCode          : String? = null,
        @SerializedName("company_ifsc_code"   ) var companyIfscCode   : String? = null

    )
    data class Customer (

        @SerializedName("id"                ) var id              : String?                 = null,
        @SerializedName("colltn_amt"        ) var colltnAmt       : Int?                    = null,
        @SerializedName("submitted_on"      ) var submittedOn     : String?                 = null,
        @SerializedName("accptd"            ) var accptd          : String?                 = null,
        @SerializedName("colltn_until_cncl" ) var colltnUntilCncl : Boolean?                = null,
        @SerializedName("debit_type"        ) var debitType       : String?                 = null,
        @SerializedName("name"              ) var name            : String?                 = null,
        @SerializedName("mobile_no"         ) var mobileNo        : String?                 = null,
        @SerializedName("tel_no"            ) var telNo           : String?                 = null,
        @SerializedName("email"             ) var email           : String?                 = null,
        @SerializedName("seq_tp"            ) var seqTp           : String?                 = null,
        @SerializedName("frqcy"             ) var frqcy           : String?                 = null,
        @SerializedName("frst_colltn_dt"    ) var frstColltnDt    : String?                 = null,
        @SerializedName("fnl_colltn_dt"     ) var fnlColltnDt     : String?                 = null,
        @SerializedName("expires_at"        ) var expiresAt       : String?                 = null,
        @SerializedName("bank_accounts"     ) var bankAccounts    : ArrayList<BankAccounts> = arrayListOf(),
        @SerializedName("loan_no"           ) var loanNo          : String?                 = null,
        @SerializedName("addnl2"            ) var addnl2          : String?                 = null,
        @SerializedName("addnl3"            ) var addnl3          : String?                 = null,
        @SerializedName("addnl4"            ) var addnl4          : String?                 = null,
        @SerializedName("addnl5"            ) var addnl5          : String?                 = null,
        @SerializedName("nupay_ref_no"      ) var nupayRefNo      : String?                 = null

    )
    data class Data (

        @SerializedName("customer" ) var customer : Customer? = Customer(),
        @SerializedName("url"      ) var url      : String?   = null,
        @SerializedName("notice"   ) var notice   : String?   = null

    )
}
