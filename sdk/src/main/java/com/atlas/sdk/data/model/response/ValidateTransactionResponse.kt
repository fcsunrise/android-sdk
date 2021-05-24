package com.atlas.sdk.data.model.response

import com.atlas.sdk.data.model.Error
import com.atlas.sdk.data.model.ValidateResponse
import com.google.gson.annotations.SerializedName

data class ValidateTransactionResponse(

        @SerializedName("response")
        val validateResponse: ValidateResponse,

        @SerializedName("error")
        val error: Error
)