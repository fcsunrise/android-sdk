package com.atlas.sdk.data.model.request

import com.atlas.sdk.data.model.Authorization
import com.atlas.sdk.data.model.Ref
import com.google.gson.annotations.SerializedName

internal data class CreateTransaction(

        @SerializedName("auth")
        val auth: Authorization,

        @SerializedName("locale")
        val locale: String,

        @SerializedName("service_id")
        val serviceId: Int,

        @SerializedName("amount")
        val amount: Int,

        @SerializedName("operation_type")
        val operationType: Int,

        @SerializedName("fields")
        val fields: Map<String, String>,

        @SerializedName("ref")
        val ref: Ref?
)
