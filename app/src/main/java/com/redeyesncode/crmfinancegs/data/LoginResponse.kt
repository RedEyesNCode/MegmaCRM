package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("status" ) var status : Boolean? = null,
                         @SerializedName("user"   ) var user   : User?    = User(),
                         @SerializedName("otp"    ) var otp    : Int?     = null
){
    data class User (

        @SerializedName("id"                    ) var id                    : Int?    = null,
        @SerializedName("name"                  ) var name                  : String? = null,
        @SerializedName("email"                 ) var email                 : String? = null,
        @SerializedName("sub_admin_id"          ) var subAdminId            : String? = null,
        @SerializedName("mobile"                ) var mobile                : String? = null,
        @SerializedName("dob"                   ) var dob                   : String? = null,
        @SerializedName("gender"                ) var gender                : String? = null,
        @SerializedName("pincode"               ) var pincode               : String? = null,
        @SerializedName("user_type"             ) var userType              : String? = null,
        @SerializedName("officeaddress"         ) var officeaddress         : String? = null,
        @SerializedName("currentaddress"        ) var currentaddress        : String? = null,
        @SerializedName("relativeNumberName"    ) var relativeNumberName    : String? = null,
        @SerializedName("relativenumber"        ) var relativenumber        : String? = null,
        @SerializedName("relativeNumberTwoName" ) var relativeNumberTwoName : String? = null,
        @SerializedName("relativenumbertwo"     ) var relativenumbertwo     : String? = null,
        @SerializedName("monthlysalary"         ) var monthlysalary         : Int?    = null,
        @SerializedName("referal_code"          ) var referalCode           : String? = null,
        @SerializedName("fcm_token"             ) var fcmToken              : String? = null,
        @SerializedName("pan"                   ) var pan                   : String? = null,
        @SerializedName("pan_name"              ) var panName               : String? = null,
        @SerializedName("pan_image"             ) var panImage              : String? = null,
        @SerializedName("adhar"                 ) var adhar                 : String? = null,
        @SerializedName("adhar_name"            ) var adharName             : String? = null,
        @SerializedName("adhar_front_image"     ) var adharFrontImage       : String? = null,
        @SerializedName("adhar_back_image"      ) var adharBackImage        : String? = null,
        @SerializedName("selfie_image"          ) var selfieImage           : String? = null,
        @SerializedName("sign_image"            ) var signImage             : String? = null,
        @SerializedName("bank_statement"        ) var bankStatement         : String? = null,
        @SerializedName("kyc_approval"          ) var kycApproval           : String? = null,
        @SerializedName("kyc_reason"            ) var kycReason             : String? = null,
        @SerializedName("login_otp"             ) var loginOtp              : String? = null,
        @SerializedName("created_at"            ) var createdAt             : String? = null,
        @SerializedName("updated_at"            ) var updatedAt             : String? = null,
        @SerializedName("contacts"              ) var contacts              : String? = null

    )
}
