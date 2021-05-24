package com.atlas.sdk.data.model

import com.google.gson.annotations.SerializedName

internal data class Authorization(

        @SerializedName("point")
        val point: String,

        @SerializedName("hash")
        val hash: String
)