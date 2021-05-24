package com.atlas.sdk.data.model.request

import com.google.gson.annotations.SerializedName

internal data class ConfirmTransaction(
        @SerializedName("point_id")
        val pointId: String,

        @SerializedName("payment_id")
        val paymentId: String,

        @SerializedName("locale")
        val locale: String,

        @SerializedName("card_number")
        val cardNumber: String?,

        @SerializedName("expiry_date")
        val expiryDate: String?,

        @SerializedName("cvv")
        val cvv: String,

        @SerializedName("card_token")
        val cardToken: String?
)