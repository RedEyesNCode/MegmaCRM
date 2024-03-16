package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class UserLeadResponse(@SerializedName("status"  ) var status  : String?         = null,
                            @SerializedName("code"    ) var code    : Int?            = null,
                            @SerializedName("message" ) var message : String?         = null,
                            @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
){
    data class Data (

        @SerializedName("leadId"         ) var leadId         : Int?    = null,
        @SerializedName("firstname"      ) var firstname      : String? = null,
        @SerializedName("lastname"       ) var lastname       : String? = null,
        @SerializedName("middlename"     ) var middlename     : String? = null,
        @SerializedName("dob"            ) var dob            : String? = null,
        @SerializedName("gender"         ) var gender         : String? = null,
        @SerializedName("mobileNumber"         ) var mobileNumber         : String? = null,
        @SerializedName("pincode"        ) var pincode        : String? = null,
        @SerializedName("userType"       ) var userType       : String? = null,
        @SerializedName("monthlySalary"  ) var monthlySalary  : Int?    = null,
        @SerializedName("relativeName"   ) var relativeName   : String? = null,
        @SerializedName("relativeNumber" ) var relativeNumber : String? = null,
        @SerializedName("currentAddress" ) var currentAddress : String? = null,
        @SerializedName("state"          ) var state          : String? = null,
        @SerializedName("aadhar_front"   ) var aadharFront    : String? = null,
        @SerializedName("aadhar_back"    ) var aadharBack     : String? = null,
        @SerializedName("pancard"        ) var pancard        : String? = null,
        @SerializedName("pancard_img"        ) var pancard_img        : String? = null,
        @SerializedName("processingFees"   ) var processingFees    : String? = null,
        @SerializedName("feesAmount"   ) var feesAmount    : String? = null,
        @SerializedName("selfie"         ) var selfie         : String? = null,
        @SerializedName("leadAmount"         ) var leadAmount         : String? = null,
        @SerializedName("createdAt"         ) var createdAt         : String? = null,
        @SerializedName("leadStatus"     ) var leadStatus     : String? = null

    )
}
