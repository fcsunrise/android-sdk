package com.atlas.sdk.data.model

import com.google.gson.annotations.SerializedName

data class Error(

        @SerializedName("error")
        val error: String,

        @SerializedName("error_code")
        val errorCode: Int,

        @SerializedName("message_error")
        val message_error: String
)