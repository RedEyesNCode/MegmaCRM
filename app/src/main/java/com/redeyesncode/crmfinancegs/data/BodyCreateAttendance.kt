package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class BodyCreateAttendance(@SerializedName("userId"  ) var userId  : String? = null,
                                @SerializedName("photo"   ) var photo   : String? = null,
                                @SerializedName("address" ) var address : String? = null,
                                @SerializedName("status"  ) var status  : String? = null,
                                @SerializedName("remark"  ) var remark  : String? = null) {
}