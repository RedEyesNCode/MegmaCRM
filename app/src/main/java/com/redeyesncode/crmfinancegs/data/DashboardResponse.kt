package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class DashboardResponse(@SerializedName("status"             ) var status            : Boolean?            = null,
                             @SerializedName("user"               ) var user              : User?               = User(),
                             @SerializedName("banner"             ) var banner            : ArrayList<Banner>   = arrayListOf(),
                             @SerializedName("packages"           ) var packages          : ArrayList<Packages> = arrayListOf(),
                             @SerializedName("assigned_package"   ) var assignedPackage   : Int?                = null,
                             @SerializedName("assigned_custom"    ) var assignedCustom    : Int?                = null,
                             @SerializedName("loan_info"          ) var loanInfo          : LoanInfo?           = LoanInfo(),
                             @SerializedName("kyc"                ) var kyc               : String?             = null,
                             @SerializedName("kyc_reason"         ) var kycReason         : String?             = null,
                             @SerializedName("notification_count" ) var notificationCount : Int?                = null,
                             @SerializedName("custom_packages"    ) var customPackages    : ArrayList<CustomPackages> = arrayListOf(),

                             @SerializedName("enc_id"             ) var encId             : String?             = null) {


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
    data class CustomPackages (

        @SerializedName("id"                  ) var id                 : Int?    = null,
        @SerializedName("type"                ) var type               : String? = null,
        @SerializedName("day_month"           ) var dayMonth           : String? = null,
        @SerializedName("user_id"             ) var userId             : Int?    = null,
        @SerializedName("package_name"        ) var packageName        : String? = null,
        @SerializedName("package_description" ) var packageDescription : String? = null,
        @SerializedName("amount"              ) var amount             : String? = null,
        @SerializedName("loan_document"       ) var loanDocument       : Int?    = null,
        @SerializedName("tenure"              ) var tenure             : String? = null,
        @SerializedName("insurance"           ) var insurance          : Int?    = null,
        @SerializedName("processing_fees"     ) var processingFees     : String? = null,
        @SerializedName("gst"                 ) var gst                : String? = null,
        @SerializedName("interest"            ) var interest           : String? = null,
        @SerializedName("emi"                 ) var emi                : String? = null,
        @SerializedName("emd_amt"             ) var emdAmt             : String? = null,
        @SerializedName("emd_month"           ) var emdMonth           : String? = null,
        @SerializedName("seen"                ) var seen               : String? = null,
        @SerializedName("created_at"          ) var createdAt          : String? = null,
        @SerializedName("updated_at"          ) var updatedAt          : String? = null

    )
    data class Banner (

        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("image"      ) var image     : String? = null,
        @SerializedName("created_at" ) var createdAt : String? = null,
        @SerializedName("updated_at" ) var updatedAt : String? = null

    )
    data class Packages (

        @SerializedName("id"                  ) var id                 : Int?    = null,
        @SerializedName("type"                ) var type               : String? = null,
        @SerializedName("package_name"        ) var packageName        : String? = null,
        @SerializedName("package_description" ) var packageDescription : String? = null,
        @SerializedName("amount"              ) var amount             : String? = null,
        @SerializedName("loan_document"       ) var loanDocument       : Int?    = null,
        @SerializedName("tenure"              ) var tenure             : String? = null,
        @SerializedName("insurance"           ) var insurance          : Int?    = null,
        @SerializedName("processing_fees"     ) var processingFees     : String? = null,
        @SerializedName("gst"                 ) var gst                : String? = null,
        @SerializedName("interest"            ) var interest           : String? = null,
        @SerializedName("emi"                 ) var emi                : String? = null,
        @SerializedName("emd_month"           ) var emdMonth           : String? = null,
        @SerializedName("emd_amt"             ) var emdAmt             : String? = null,
        @SerializedName("created_at"          ) var createdAt          : String? = null,
        @SerializedName("updated_at"          ) var updatedAt          : String? = null,
        @SerializedName("package_duration"    ) var packageDuration    : String? = null,
        @SerializedName("processing_fees_per" ) var processingFeesPer  : String? = null,
        @SerializedName("loan_documents_per"  ) var loanDocumentsPer   : String? = null

    )

    data class LoanInfo (

        @SerializedName("loan_id"        ) var loanId        : Int?    = null,
        @SerializedName("loan_type"      ) var loanType      : String? = null,
        @SerializedName("status"         ) var status        : String? = null,
        @SerializedName("tenure"         ) var tenure        : String? = null,
        @SerializedName("emi_due"        ) var emiDue        : String? = null,
        @SerializedName("current_status" ) var currentStatus : String? = null,
        @SerializedName("enach"          ) var enach         : String? = null

    )
}