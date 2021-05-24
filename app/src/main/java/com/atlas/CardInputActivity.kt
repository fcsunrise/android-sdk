package com.atlas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.atlas.core.TransactionManager
import com.atlas.sdk.data.model.CardInputData
import com.atlas.sdk.data.model.ValidationData
import com.atlas.sdk.util.OperationTypes
import com.atlas.sdk.view.CardInputView
import com.atlas.util.showProgressDialog
import com.atlas.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardInputActivity : AppCompatActivity(), CardInputView.CardInputListener {

    private val TAG = CardInputActivity::class.simpleName

    private val appSDK = TransactionManager.sdk

    private lateinit var cardInputView: CardInputView
    private lateinit var transaction: ValidationData
    private lateinit var summ: String

    companion object {
        const val PAY_SUMM          = "paySumm"
        const val VALIDATION_DATA   = "validationData"
        const val ID_TRANSACTION    = "idTransaction"
        const val CARD_ACTIVITY     = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_input)

        if (intent != null){
            transaction = intent.getParcelableExtra(VALIDATION_DATA)!!
            summ = intent.getStringExtra(PAY_SUMM)!!
        }
        generateData()
    }

    private fun generateData(){

        cardInputView = findViewById(R.id.cardInputView)
        cardInputView.setTransactionLogo(R.drawable.sunrise_logo)
        cardInputView.setTransactionTitle("")
        cardInputView.setTransactionNumber("")
        cardInputView.setTransactionSumm("$summ")
        cardInputView.setCardInputListener(this)
    }

    override fun onInputCompleted(cardInputData: CardInputData) {
        createTransaction(cardInputData)
    }

    private fun createTransaction(cardInputData: CardInputData){

        when(transaction.operationType.value){
            OperationTypes.H2H.value -> {
                Log.d(TAG, "TRANSACTION H2H")
                sendCreateH2HTransaction(transaction, cardInputData)
            }

            OperationTypes.MOTO.value -> {
                Log.d(TAG, "TRANSACTION MOTO")
                sendCreateMotoTransaction(transaction, cardInputData)
            }

            OperationTypes.PAY_WITHOUT_3DS.value -> {
                Log.d(TAG, "TRANSACTION MOTO")
                sendCreateMotoTransaction(transaction, cardInputData)
            }
        }
    }

    private fun sendCreateH2HTransaction(validationData: ValidationData, cardInputData: CardInputData){

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

                    createAuthH2HTransaction(
                            response.createResponse.result!!.payUrl,
                            validationData.pointId,
                            response.createResponse.paymentId,
                            validationData.locale,
                            cardInputData.number,
                            cardInputData.date,
                            cardInputData.cvv,
                    null)

                    returnTransactionID(response.createResponse.paymentId)
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

    private fun sendCreateMotoTransaction(validationData: ValidationData, cardInputData: CardInputData){

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
                        validationData.fields, null)

                if(response.createResponse != null){

                    progress.dismiss()

                    createAuthMotoTransaction(
                            response.createResponse.result!!.payUrl,
                            validationData.pointId,
                            response.createResponse.paymentId,
                            validationData.locale,
                            cardInputData.number,
                            cardInputData.date,
                            cardInputData.cvv)

                    returnTransactionID(response.createResponse.paymentId)
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

    private fun createAuthH2HTransaction(fullUrl: String, pointId: String, paymentId: String, locale: String, cardNumber: String?, expiryDate: String?, cvv: String, cardToken: String?){

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

    private fun createAuthMotoTransaction(fullUrl: String, pointId: String, paymentId: String, locale: String, cardNumber: String, expiryDate: String, cvv: String){

        val progress = showProgressDialog(this, getString(R.string.dialog_loading_text))

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = appSDK.confirmMotoTransaction(fullUrl, pointId, paymentId, locale, cardNumber, expiryDate, cvv)

                if(response.errorCode == 0){

                    progress.dismiss()
                    finish()
                    showToast("Completed")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cardInputView.onActivityResult(requestCode, resultCode, data)
    }

    private fun returnTransactionID(id: String){
        val intent = Intent()
        intent.putExtra(ID_TRANSACTION, id)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun startWebActivity(payUrl: String){
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(WebActivity.WEB_URL, payUrl)
        startActivity(intent)
    }
}