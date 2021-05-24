package com.atlas.sdk.manager

import com.atlas.sdk.data.model.request.ConfirmTransaction
import com.atlas.sdk.data.model.request.CreateTransaction
import com.atlas.sdk.data.model.request.FindTransaction
import com.atlas.sdk.data.model.request.ValidateTransaction
import com.atlas.sdk.data.network.api.ApiAtlas
import com.atlas.sdk.data.network.api.RetrofitBuilder

internal class TransactionManager {

    private var apiHelper: ApiAtlas = RetrofitBuilder.apiService

    suspend fun validateTransaction(params: ValidateTransaction) = apiHelper.validateTransaction(params)
    suspend fun createTransaction(params: CreateTransaction) = apiHelper.createTransaction(params)
    suspend fun findTransaction(params: FindTransaction) = apiHelper.findTransaction(params)
    suspend fun confirmH2HTransaction(fullUrl: String, params: ConfirmTransaction) = apiHelper.confirmH2HTransaction(fullUrl, params)
    suspend fun confirmMotoTransaction(fullUrl: String, params: ConfirmTransaction) = apiHelper.confirmMotoTransaction(fullUrl, params)
}