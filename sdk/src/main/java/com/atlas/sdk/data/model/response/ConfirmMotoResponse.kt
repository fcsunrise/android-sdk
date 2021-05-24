package com.atlas.sdk.data.model.response

import com.google.gson.annotations.SerializedName

data class ConfirmMotoResponse(

        @SerializedName("error_code")
        val errorCode: Int,

        @SerializedName("result")
        val result: Boolean
)