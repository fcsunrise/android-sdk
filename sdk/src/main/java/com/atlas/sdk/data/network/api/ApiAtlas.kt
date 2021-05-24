package com.atlas.sdk.data.network.api

import com.atlas.sdk.data.model.request.ConfirmTransaction
import com.atlas.sdk.data.model.request.CreateTransaction
import com.atlas.sdk.data.model.request.FindTransaction
import com.atlas.sdk.data.model.request.ValidateTransaction
import com.atlas.sdk.data.model.response.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

internal interface ApiAtlas {

    @POST("transaction/validate")
    suspend fun validateTransaction(@Body params: ValidateTransaction): ValidateTransactionResponse

    @POST("transaction/create")
    suspend fun createTransaction(@Body params: CreateTransaction): CreateTransactionResponse

    @POST("transaction/find")
    suspend fun findTransaction(@Body params: FindTransaction): FindTransactionResponse

    @POST()
    suspend fun confirmH2HTransaction(@Url fullUrl: String, @Body params: ConfirmTransaction): ConfirmH2HResponse

    @POST()
    suspend fun confirmMotoTransaction(@Url fullUrl: String, @Body params: ConfirmTransaction): ConfirmMotoResponse
}