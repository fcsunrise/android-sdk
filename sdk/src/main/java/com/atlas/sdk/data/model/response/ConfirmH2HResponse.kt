package com.atlas.sdk.data.model.response

import com.google.gson.annotations.SerializedName

data class ConfirmH2HResponse(

        @SerializedName("error_code")
        val errorCode: Int,

        @SerializedName("result")
        val result: Result
){
    class Result(
            @SerializedName("3dsUrl")
            val url: String
    )
}