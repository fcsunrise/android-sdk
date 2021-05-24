package com.atlas.sdk.core

import com.atlas.sdk.data.model.Authorization
import com.atlas.sdk.data.model.Ref
import com.atlas.sdk.data.model.request.ConfirmTransaction
import com.atlas.sdk.data.model.request.CreateTransaction
import com.atlas.sdk.data.model.request.FindTransaction
import com.atlas.sdk.data.model.request.ValidateTransaction
import com.atlas.sdk.data.model.response.*
import com.atlas.sdk.data.network.security.AuthEncryption
import com.atlas.sdk.manager.TransactionManager
import com.google.gson.Gson
import kotlin.math.roundToInt

class AtlasSdk {

    private val transactionManager = TransactionManager()

    suspend fun validateTransaction(pointId: String, locale: String, serviceId: Int, pointToken: String, amount: Int, operationType: Int, fields: Map<String, String>, ref: Ref?
    ): ValidateTransactionResponse {

        val preAuth = Authorization(pointId, "")
        val preValidation = ValidateTransaction(preAuth, locale, serviceId, amount, operationType, fields, ref)
        val gson = Gson().toJson(preValidation)
        val hash = AuthEncryption.createHash(gson, pointToken)

        return transactionManager.validateTransaction(ValidateTransaction(Authorization(pointId, hash), locale, serviceId, amount, operationType, fields, ref))
    }

    suspend fun createTransaction(pointId: String, locale: String, serviceId: Int, pointToken: String, amount: Int, operationType: Int, fields: Map<String, String>, ref: Ref?
    ): CreateTransactionResponse {

        val preAuth = Authorization(pointId, "")
        val preValidation = ValidateTransaction(preAuth, locale, serviceId, amount, operationType, fields, ref)
        val gson = Gson().toJson(preValidation)
        val hash = AuthEncryption.createHash(gson, pointToken)

        return transactionManager.createTransaction(CreateTransaction(Authorization(pointId, hash), locale, serviceId, amount, operationType, fields, ref))
    }

    suspend fun findTransaction(pointId: String, locale: String, pointToken: String, paymentId: String?, externalTransactionId: String?, oltp_id: Int?
    ): FindTransactionResponse {

        val preAuth = Authorization(pointId, "")
        val preFindValidation = FindTransaction(preAuth, locale, paymentId, externalTransactionId, oltp_id)
        val gson = Gson().toJson(preFindValidation)
        val hash = AuthEncryption.createHash(gson, pointToken)

        return transactionManager.findTransaction(FindTransaction(Authorization(pointId, hash), locale, paymentId, externalTransactionId, oltp_id))
    }

    suspend fun confirmH2HTransaction(fullUrl: String, pointId: String, paymentId: String, locale: String, cardNumber: String?, expiryDate: String?, cvv: String, cardToken: String?
    ): ConfirmH2HResponse {

        val params = ConfirmTransaction(pointId, paymentId, locale, cardNumber, expiryDate, cvv, cardToken)

        return transactionManager.confirmH2HTransaction(fullUrl, params)
    }

    suspend fun confirmMotoTransaction(fullUrl: String, pointId: String, paymentId: String, locale: String, cardNumber: String, expiryDate: String, cvv: String
    ): ConfirmMotoResponse {

        val params = ConfirmTransaction(pointId, paymentId, locale, cardNumber, expiryDate, cvv, null)

        return transactionManager.confirmMotoTransaction(fullUrl, params)
    }

    fun calculateFinalAmount(amount: Int, additionalFixValue: Int, maxSum: Int, minSum: Int, value: Double): Int{

        var percentage = (amount / 100) * (value)
        val comission = percentage.roundToInt() + additionalFixValue

        if (minSum != 0 && (comission < minSum)){
            return amount + minSum
        }else if(maxSum != 0 && (comission > maxSum)){
            return amount + maxSum
        }else {
            return amount + comission
        }
    }
}