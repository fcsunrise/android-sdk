package com.atlas.sdk.data.model.request

import com.atlas.sdk.data.model.Authorization
import com.google.gson.annotations.SerializedName

internal data class FindTransaction(

        @SerializedName("auth")
        val auth: Authorization,

        @SerializedName("locale")
        val locale: String,

        @SerializedName("payment_id")
        val paymentId: String?,

        @SerializedName("external_transaction_id")
        val externalTransactionId: String?,

        @SerializedName("oltp_id")
        val oltp_id: Int?
)