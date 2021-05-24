package com.atlas.sdk.data.model

import com.google.gson.annotations.SerializedName

data class ValidateResponse(

        @SerializedName("result")
        val result: Boolean,

        @SerializedName("commission")
        val commission: Commission
){
        class Commission(

                @SerializedName("value")
                val fields: List<Value>
        )

        class Value(

                @SerializedName("additional_fix_value")
                val additionalFixValue: Int,

                @SerializedName("max_sum")
                val maxSum: Int,

                @SerializedName("min_sum")
                val minSum: Int,

                @SerializedName("value")
                val value: Double
        )
}