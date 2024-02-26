package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class KycDetails(@SerializedName("firstName") var firstName: String? = null,
                      @SerializedName("middleName") var middleName: String? = null,
                      @SerializedName("lastName") var lastName: String? = null,
                      @SerializedName("dob") var dob: String? = null,
                      @SerializedName("email") var email: String? = null,
                      @SerializedName("pincode") var pincode: String? = null,
                      @SerializedName("gender") var gender: String? = null,
                      @SerializedName("user_type") var user_type:String?=null,
                      @SerializedName("state") var state:String?=null,
                      @SerializedName("occupation") var occupation: String? = null,
                      @SerializedName("monthlySalary") var monthlySalary: String? = null,
                      @SerializedName("relativeName") var relativeName: String? = null,
                      @SerializedName("relativeNumber") var relativeNumber: String? = null,
                      @SerializedName("relativeNameTwo") var relativeNameTwo: String? = null,
                      @SerializedName("relativeNumber2") var relativeNumber2: String? = null,
                      @SerializedName("currentAddress") var currentAddress: String? = null,
                      @SerializedName("pan_image") var pan_image: String? = null,
                      @SerializedName("adhar_front") var adhar_front: String? = null,
                      @SerializedName("adhar_back") var adhar_back: String? = null,
                      @SerializedName("selfie") var selfie: String? = null,
                      @SerializedName("pan_number") var pan_number: String? = null,
                      @SerializedName("adhar_number") var adhar_number: String? = null,

                      )
