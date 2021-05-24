package com.atlas.sdk.data.model.response

import com.atlas.sdk.data.model.Error
import com.atlas.sdk.data.model.FindResponse
import com.google.gson.annotations.SerializedName

data class FindTransactionResponse(

        @SerializedName("response")
        val findResponse: FindResponse,

        @SerializedName("error")
        val error: Error
)