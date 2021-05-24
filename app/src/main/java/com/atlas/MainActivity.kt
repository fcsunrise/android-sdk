package com.atlas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.atlas.CardInputActivity.Companion.CARD_ACTIVITY
import com.atlas.CardInputActivity.Companion.ID_TRANSACTION
import com.atlas.CardInputActivity.Companion.PAY_SUMM
import com.atlas.CardInputActivity.Companion.VALIDATION_DATA
import com.atlas.WebActivity.Companion.WEB_URL
import com.atlas.core.TransactionManager
import com.atlas.core.TransactionManager.FIELDS
import com.atlas.core.TransactionManager.PAY_POINT_ID
import com.atlas.core.TransactionManager.PAY_POINT_TOKEN
import com.atlas.core.TransactionManager.PAY_SERVICE_ID
import com.atlas.core.TransactionManager.TOKEN_POINT_ID
import com.atlas.core.TransactionManager.TOKEN_POINT_TOKEN
import com.atlas.core.TransactionManager.TOKEN_SERVICE_ID
import com.atlas.sdk.data.model.Ref
import com.atlas.sdk.data.model.ValidationData
import com.atlas.sdk.util.OperationTypes
import com.atlas.sdk.util.RU
import com.atlas.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName

    private val appSDK = TransactionManager.sdk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTransactions()
    }

    private fun initTransactions() {

        webTransactionButton.setOnClickListener {
            showInputNumberDialog(this, getString(R.string.dialog_transaction_summ_text)) { summ ->
                clearText()
                sendValidationTransaction(ValidationData(PAY_POINT_ID, RU,
                        PAY_SERVICE_ID, PAY_POINT_TOKEN,
                        summ.toInt()*100, OperationTypes.WEB, FIELDS, null))
            }
        }

        tokenCardButton.setOnClickListener {
            clearText()
            createCardTokenWeb()
        }

        webTransactionStatusButton.setOnClickListener {
            showInputDialog(this, getString(R.string.dialog_transaction_number_text)) { transactionNumber ->
                clearText()
                findTransaction(transactionNumber, null, null)
            }
        }

        tokenStatusButton.setOnClickListener {
            showInputDialog(this, getString(R.string.dialog_transaction_number_text)) { transactionNumber ->
                clearText()
                findTokenTransaction(transactionNumber, null, null)
            }
        }

        hostToHostButton.setOnClickListener {
            showInputNumberDialog(this, getString(R.string.dialog_transaction_summ_text)) { summ ->
                clearText()
                sendValidationTransaction(ValidationData(PAY_POINT_ID, RU,
                        PAY_SERVICE_ID, PAY_POINT_TOKEN,
                        summ.toInt()*100, OperationTypes.H2H, FIELDS, Ref(null, null, "https://www.google.com/")))
            }
        }

        motoTransactionButton.setOnClickListener {
            showInputNumberDialog(this, getString(R.string.dialog_transaction_summ_text)) { summ ->
                clearText()
                sendValidationTransaction(ValidationData(PAY_POINT_ID, RU,
                        PAY_SERVICE_ID, PAY_POINT_TOKEN,
                        summ.toInt()*100, OperationTypes.MOTO, FIELDS, null))
            }
        }

        webTokenTransactionButton.setOnClickListener {
            showDoubleInputDialog(this, getString(R.string.dialog_transaction_text)){ transition ->
                clearText()
                FIELDS.put("card_token", transition.token)
                sendValidationTransaction(ValidationData(PAY_POINT_ID, RU,
                        PAY_SERVICE_ID, PAY_POINT_TOKEN,
                        transition.summ.toInt()*100, OperationTypes.WEB, FIELDS, null))
            }
        }

        tokenHostToHostButton.setOnClickListener {

            showInputNumberDialog(this, getString(R.string.dialog_transaction_summ_text)) { summ ->
                clearText()
                sendValidationTransaction(ValidationData(PAY_POINT_ID, RU,
                        PAY_SERVICE_ID, PAY_POINT_TOKEN,
                        summ.toInt()*100, OperationTypes.PAY_BY_CARD_TOKEN, FIELDS, Ref(null, null, "https://www.google.com/")))
            }
        }

        noAuthHostToHostButton.setOnClickListener {
            showInputNumberDialog(this, getString(R.string.dialog_transaction_summ_text)) { transition ->
                clearText()
                sendValidationTransaction(ValidationData(PAY_POINT_ID, RU,
                        PAY_SERVICE_ID, PAY_POINT_TOKEN,
                        transition.toInt()*100, OperationTypes.PAY_WITHOUT_3DS, FIELDS, null))
            }
        }
    }

    private fun createCardTokenWeb(){

        val progress = showProgressDialog(this, getString(R.string.dialog_loading_text))

        CoroutineScope(Main).launch {
            try {
                val response = appSDK.createTransaction(TOKEN_POINT_ID, RU, TOKEN_SERVICE_ID, TOKEN_POINT_TOKEN, 0, OperationTypes.WEB.value, FIELDS, null)

                if(response.createResponse != null){

                    startWebActivity(response.createResponse.result!!.payUrl)
                    progress.dismiss()

                    idTransactionTextView.text = "${getString(R.string.last_transaction_id_text)} ${response.createResponse.paymentId}"
                }
                if (response.error != null){
                    Log.d(TAG, "ERROR code: ${response.error.errorCode}")
                    progress.dismiss()
                    showToast("ERROR ${response.error.errorCode}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                Log.d(TAG, "ERROR createTransaction $e")
                progress.dismiss()
                showToast("ERROR ${e.message}")
            }
        }
    }

    private fun findTransaction(paymentId: String?, externalTransactionId: String?, oltp_id: Int?){

        val progress = showProgressDialog(this, getString(R.string.dialog_loading_text))

        CoroutineScope(Main).launch {
            try {
                val response = appSDK.findTransaction(PAY_POINT_ID, RU, PAY_POINT_TOKEN, paymentId, externalTransactionId, oltp_id)

                if(response.findResponse.fields != null ){

                    progress.dismiss()
                    idTransactionTextView.text = "${getString(R.string.last_transaction_status_text)} ${response.findResponse.status}"
                    cardNumberTextView.text = response.findResponse.fields?.cardNumber
                    cardTokenTextView.text = response.findResponse.fields?.cardToken
                }
                if (response.error != null){
                    Log.d(TAG, "ERROR code: ${response.error.errorCode}")
                    progress.dismiss()
                    showToast("ERROR ${response.error.errorCode}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                progress.dismiss()
                showToast("ERROR ${e.message}")
            }
        }
    }

    private fun findTokenTransaction(paymentId: String?, externalTransactionId: String?, oltp_id: Int?){

        val progress = showProgressDialog(this, getString(R.string.dialog_loading_text))

        CoroutineScope(Main).launch {
            try {
                val response = appSDK.findTransaction(TOKEN_POINT_ID, RU, TOKEN_POINT_TOKEN, paymentId, externalTransactionId, oltp_id)

                if(response.findResponse.fields != null ){

                    progress.dismiss()
                    idTransactionTextView.text = "${getString(R.string.last_transaction_status_text)} ${response.findResponse.status}"
                    cardNumberTextView.text = response.findResponse.fields?.cardNumber
                    cardTokenTextView.text = response.findResponse.fields?.cardToken
                }
                if (response.error != null){
                    Log.d(TAG, "ERROR code: ${response.error.errorCode}")
                    progress.dismiss()
                    showToast("ERROR ${response.error.errorCode}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                progress.dismiss()
                showToast("ERROR ${e.message}")
            }
        }
    }

    private fun sendValidationTransaction(validationData: ValidationData){

        val progress = showProgressDialog(this, getString(R.string.dialog_validation_text))

        CoroutineScope(Main).launch {
            try {

                val response = appSDK.validateTransaction(
                        validationData.pointId,
                        validationData.locale,
                        validationData.serviceId,
                        validationData.pointToken,
                        validationData.amount,
                        validationData.operationType.value,
                        validationData.fields, validationData.ref)

                if(response.validateResponse != null){

                    val finalSumm = appSDK.calculateFinalAmount(validationData.amount,
                            response.validateResponse.commission.fields.get(0).additionalFixValue,
                            response.validateResponse.commission.fields.get(0).maxSum,
                            response.validateResponse.commission.fields.get(0).minSum,
                            response.validateResponse.commission.fields.get(0).value)

                    progress.dismiss()
                    createСonfirmedTransaction(finalSumm, validationData)
                }

                if (response.error != null){
                    Log.d(TAG, "VALIDATION FALSE code: ${response.error.errorCode}")
                    progress.dismiss()
                    showToast("ERROR ${response.error.errorCode}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                Log.d(TAG, "ERROR createTransaction $e")
                progress.dismiss()
                showToast("ERROR ${e.message}")
            }
        }
    }

    private fun createСonfirmedTransaction(finalSumm: Int, validationData: ValidationData){

        when(validationData.operationType.value){

            OperationTypes.WEB.value -> {
                Log.d(TAG, "TRANSACTION WEB")
                sendCreateWebTransaction(validationData)
            }

            OperationTypes.H2H.value -> {
                Log.d(TAG, "H2H")
                startCardInputActivity(finalSumm, validationData)
            }

            OperationTypes.MOTO.value -> {
                Log.d(TAG, "MOTO")
                startCardInputActivity(finalSumm, validationData)
            }

            OperationTypes.PAY_BY_CARD_TOKEN.value -> {
                Log.d(TAG, "TRANSACTION PAY_BY_CARD_TOKEN")

                val summ = "${getString(R.string.dialog_summ_text)} ${finalSumm/100.0}"

                showInputCardDialog(this, summ) { transition ->
                    sendCreateTokenH2HTransaction(validationData, transition.token, transition.cvv)
                }
            }

            OperationTypes.PAY_WITHOUT_3DS.value -> {
                Log.d(TAG, "PAY_WITHOUT_3DS")
                startCardInputActivity(finalSumm, validationData)
            }
        }
    }

    private fun sendCreateWebTransaction(validationData: ValidationData){

        val progress = showProgressDialog(this, getString(R.string.dialog_loading_text))

        CoroutineScope(Main).launch {
            try {
                val response = appSDK.createTransaction(
                        validationData.pointId,
                        validationData.locale,
                        validationData.serviceId,
                        validationData.pointToken,
                        validationData.amount,
                        validationData.operationType.value,
                        validationData.fields, null)

                if(response.createResponse != null){

                    progress.dismiss()
                    idTransactionTextView.text = "${getString(R.string.last_transaction_id_text)} ${response.createResponse.paymentId}"
                    startWebActivity(response.createResponse.result!!.payUrl)
                }
                if (response.error != null){
                    Log.d(TAG, "CREATE FALSE code: ${response.error.errorCode}")
                    progress.dismiss()
                    showToast("ERROR ${response.error.errorCode}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                Log.d(TAG, "ERROR createTransaction $e")
                progress.dismiss()
                showToast("ERROR ${e.message}")
            }
        }
    }

    private fun sendCreateTokenH2HTransaction(validationData: ValidationData, cardToken: String, cardCvv: String){

        val progress = showProgressDialog(this, getString(R.string.dialog_loading_text))

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = appSDK.createTransaction(
                        validationData.pointId,
                        validationData.locale,
                        validationData.serviceId,
                        validationData.pointToken,
                        validationData.amount,
                        validationData.operationType.value,
                        validationData.fields, validationData.ref)

                if(response.createResponse != null){

                    progress.dismiss()

                    createAuthTokenH2HTransaction(
                            response.createResponse.result!!.payUrl,
                            validationData.pointId,
                            response.createResponse.paymentId,
                            validationData.locale,
                            null,
                            null,
                            cardCvv,
                            cardToken)

                    idTransactionTextView.text = "${getString(R.string.last_transaction_id_text)} ${response.createResponse.paymentId}"
                }

                if (response.error != null){
                    Log.d(TAG, "CREATE FALSE code: ${response.error.errorCode}")
                    progress.dismiss()
                    showToast("ERROR ${response.error.errorCode}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                Log.d(TAG, "ERROR createTransaction $e")
                progress.dismiss()
                showToast("ERROR ${e.message}")
            }
        }
    }

    private fun createAuthTokenH2HTransaction(fullUrl: String, pointId: String, paymentId: String, locale: String, cardNumber: String?, expiryDate: String?, cvv: String, cardToken: String?){

        val progress = showProgressDialog(this, getString(R.string.dialog_loading_text))

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = appSDK.confirmH2HTransaction(fullUrl, pointId, paymentId, locale, cardNumber, expiryDate, cvv, cardToken)

                if(response.errorCode == 0){

                    progress.dismiss()
                    startWebActivity(response.result.url)
                    finish()

                }else{
                    Log.d(TAG, "Transaction Failed code: ${response.errorCode}")
                    progress.dismiss()
                    showToast("ERROR ${response.errorCode}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                Log.d(TAG, "ERROR createTransaction $e")
                progress.dismiss()
                showToast("ERROR ${e.message}")
            }
        }
    }

    private fun startWebActivity(payUrl: String){
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(WEB_URL , payUrl)
        startActivity(intent)
    }

    private fun startCardInputActivity(finalSumm: Int, validationData: ValidationData){
        val intent = Intent(this, CardInputActivity::class.java)
        intent.putExtra(PAY_SUMM, (finalSumm / 100.0).toString())
        intent.putExtra(VALIDATION_DATA, validationData)
        startActivityForResult(intent, CARD_ACTIVITY)
    }

    private fun clearText(){
        idTransactionTextView.text = ""
        cardNumberTextView.text = ""
        cardTokenTextView.text = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CARD_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val id: String? = data.getStringExtra(ID_TRANSACTION)
                idTransactionTextView.text = "${getString(R.string.last_transaction_id_text)} $id"
            }
        }
    }
}
