package com.atlas.sdk.data.model

import android.os.Parcelable
import com.atlas.sdk.util.OperationTypes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidationData(
        val pointId: String,
        val locale: String,
        val serviceId: Int,
        val pointToken: String,
        val amount: Int,
        val operationType: OperationTypes,
        val fields: Map<String, String>,
        val ref: Ref?
) : Parcelable
