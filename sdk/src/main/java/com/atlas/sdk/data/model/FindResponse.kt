package com.atlas.sdk.data.model

import com.google.gson.annotations.SerializedName

data class FindResponse(

        @SerializedName("status")
        val status: Int,

        @SerializedName("fields")
        val fields: Fields?
){
    class Fields(

            @SerializedName("card_number")
            val cardNumber: String,

            @SerializedName("card_token")
            val cardToken: String
    )
}