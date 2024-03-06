package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class BodyCreateLead(@SerializedName("firstname"      ) var firstname      : String? = null,
                          @SerializedName("lastname"       ) var lastname       : String? = null,
                          @SerializedName("middlename"     ) var middlename     : String? = null,
                          @SerializedName("dob"            ) var dob            : String? = null,
                          @SerializedName("gender"         ) var gender         : String? = null,
                          @SerializedName("pincode"        ) var pincode        : String? = null,
                          @SerializedName("userType"       ) var userType       : String? = null,
                          @SerializedName("monthlySalary"  ) var monthlySalary  : String? = null,
                          @SerializedName("relativeName"   ) var relativeName   : String? = null,
                          @SerializedName("relativeNumber" ) var relativeNumber : String? = null,
                          @SerializedName("currentAddress" ) var currentAddress : String? = null,
                          @SerializedName("additionalDocument" ) var additionalDocument : String? = null,
                          @SerializedName("state"          ) var state          : String? = null,
                          @SerializedName("pancard"        ) var pancard        : String? = null,
                          @SerializedName("email"        ) var email        : String? = null,
                          @SerializedName("mobileNumber"        ) var mobileNumber        : String? = null,
                          @SerializedName("customerLoanAmount"        ) var customerLoanAmount        : String? = null,
                          @SerializedName("empApproveAmount"        ) var empApproveAmount        : String? = null,
                          @SerializedName("occupation"        ) var occupation        : String? = null,
                          @SerializedName("pancard_img"        ) var pancard_img        : String? = null,
                          @SerializedName("aadhar"        ) var aadhar        : String? = null,
                          @SerializedName("aadhar_back"    ) var aadharBack     : String? = null,
                          @SerializedName("aadhar_front"   ) var aadharFront    : String? = null,
                          @SerializedName("selfie"         ) var selfie         : String? = null,
                          @SerializedName("userId"         ) var userId         : String? = null)
