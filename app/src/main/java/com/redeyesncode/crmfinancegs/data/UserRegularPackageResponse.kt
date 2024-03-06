package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class UserRegularPackageResponse(@SerializedName("status"  ) var status  : Boolean? = null,
                                      @SerializedName("message" ) var message : String?  = null,
                                          @SerializedName("package" ) var userPackage : Package? = Package()
) {

    data class PackageData (

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
    data class Package (

        @SerializedName("user_id"      ) var userId      : String?                = null,
        @SerializedName("package"      ) var userPackage     : String?                = null,
        @SerializedName("hgfds"        ) var hgfds       : Int?                   = null,
        @SerializedName("package_data" ) var package_data : ArrayList<PackageData> = arrayListOf()

    )
}