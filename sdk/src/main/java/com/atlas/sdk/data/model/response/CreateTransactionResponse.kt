package com.atlas.sdk.data.model.response

import com.atlas.sdk.data.model.CreateResponse
import com.atlas.sdk.data.model.Error
import com.google.gson.annotations.SerializedName

data class CreateTransactionResponse(

        @SerializedName("response")
        val createResponse: CreateResponse,

        @SerializedName("error")
        val error: Error
)