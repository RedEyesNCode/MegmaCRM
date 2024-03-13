package com.redeyesncode.crmfinancegs.data


import com.google.gson.annotations.SerializedName

data class BodyAdminLogin(@SerializedName("manufacturer"    ) var manufacturer    : String?           = null,
                          @SerializedName("model" ) var model : String?           = null,
                          @SerializedName("release"  ) var release  : String? = null,
                          @SerializedName("sdkInt"  ) var sdkInt  : String? = null,
                          @SerializedName("hardware"  ) var hardware  : String? = null,
                          @SerializedName("brand"  ) var brand  : String? = null,
                          @SerializedName("board"  ) var board  : String? = null,
                          @SerializedName("adminKey"  ) var adminKey  : String? = null,
                          @SerializedName("product"  ) var product  : String? = null)
