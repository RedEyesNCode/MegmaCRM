package com.redeyesncode.crmfinancegs.data

import com.google.gson.annotations.SerializedName

data class PostCommentResponse(
    @SerializedName("postId" ) var postId : Int?    = null,
    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("email"  ) var email  : String? = null,
    @SerializedName("body"   ) var body   : String? = null)
