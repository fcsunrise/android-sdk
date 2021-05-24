package com.atlas.sdk.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ref(

        @SerializedName("callback_url")
        val callbackUrl: String?,

        @SerializedName("fail_url")
        val failUrl: String?,

        @SerializedName("success_url")
        val successUrl: String?
) : Parcelable