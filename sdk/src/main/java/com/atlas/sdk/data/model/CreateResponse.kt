package com.atlas.sdk.data.model

import com.google.gson.annotations.SerializedName

data class CreateResponse(

        @SerializedName("status")
        val status: Int,

        @SerializedName("payment_id")
        val paymentId: String,

        @SerializedName("service_id")
        val serviceId: Int,

        @SerializedName("point_id")
        val pointId: String,

        @SerializedName("amount")
        val amount: Int,

        @SerializedName("result")
        val result: CreateResult?
){
    class CreateResult(

            @SerializedName("pay_url")
            val payUrl: String
    )
}

