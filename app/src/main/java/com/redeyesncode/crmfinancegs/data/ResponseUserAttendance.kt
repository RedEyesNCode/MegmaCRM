package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class ResponseUserAttendance(@SerializedName("status"  ) var status  : String?         = null,
                                  @SerializedName("code"    ) var code    : Int?            = null,
                                  @SerializedName("message" ) var message : String?         = null,
                                  @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()){
    data class Data (

        @SerializedName("attendanceId" ) var attendanceId : Int?    = null,
        @SerializedName("photo"        ) var photo        : String? = null,
        @SerializedName("address"      ) var address      : String? = null,
        @SerializedName("status"       ) var status       : String? = null,
        @SerializedName("remark"       ) var remark       : String? = null,
        @SerializedName("createdAt"    ) var createdAt    : String? = null

    )
}
