package com.atlas.sdk.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.atlas.sdk.R
import com.atlas.sdk.data.model.CardInputData
import com.atlas.sdk.util.MaskWatcher
import com.atlas.sdk.util.openScan
import com.atlas.sdk.util.showToast
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.card_input_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CardInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var cardInputListener: CardInputListener? = null

    init {
        View.inflate(context, R.layout.card_input_view, this)
        initUI()
    }

    fun setCardInputListener(cardListener: CardInputListener){
        cardInputListener = cardListener
    }

    private fun initUI() {

        addCardNumberEditText.addTextChangedListener(MaskWatcher("#### #### #### ####"))
        addCardDateEditText.addTextChangedListener(MaskWatcher("##/##"))

        addCardNumberEditText.addTextChangedListener { text ->
            if (text?.length ?: 0 == 19) {
                addCardDateEditText.requestFocus()
            }
        }

        addCardDateEditText.addTextChangedListener { text ->

            if (text?.length ?: 0 == 5) {
                addCardCvvEditText.requestFocus()
            }

            if (text?.length ?: 0 == 0) {
                addCardNumberEditText.requestFocus()
            }
        }

        addCardCvvEditText.addTextChangedListener { text ->

            if (text?.length ?: 0 == 0) {
                addCardDateEditText.requestFocus()
            }
        }

        payButton.setOnClickListener {

            if (validate()){
                val number = addCardNumberEditText?.text?.toString()!!
                val cvv = addCardCvvEditText?.text?.toString()!!
                val date = addCardDateEditText?.text?.toString()!!
                val summ = transactionSummTextView?.text?.toString()!!

                cardInputListener?.onInputCompleted(CardInputData(
                        summ,
                        number.replace(" ", ""),
                        date,
                        cvv)
                )

            }else context?.showToast(context.getString(R.string.card_input_enter_all_fields))
        }

        scanCardImageView.setOnClickListener {
            openScan(101, context as Activity)
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        val number = addCardNumberEditText?.text?.toString()
        val cvv = addCardCvvEditText?.text?.toString()
        val date = addCardDateEditText?.text?.toString()

        if (!number.isNullOrBlank()) {
            if (number.replace(" ", "").length != 16)
                isValid = false
        } else isValid = false

        if (!cvv.isNullOrBlank()) {
            if (cvv.length != 3)
                isValid = false
        } else isValid = false

        if ((SimpleDateFormat("MM/yy", Locale.ENGLISH).parse(if (!date.isNullOrBlank()) date else "01/01") ?: Date()).before(
                Date()
            ))
            isValid = false

        return isValid
    }

    fun setTransactionTitle(title: String){
        transactionTitleTextView.text = title
    }

    fun setTransactionNumber(number: String){
        transactionNumberTextView.text = number
    }

    fun setTransactionSumm(summ: String){
        transactionSummTextView.text = summ
    }

    fun setTransactionLogo(resId: Int){
        logoImageView.setImageResource(resId)
    }

    fun clearData(){
        addCardNumberEditText.text.clear()
        addCardDateEditText.text.clear()
        addCardCvvEditText.text.clear()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null) {
            when (requestCode) {
                101 -> {
                    val scanResult: CreditCard? =
                        data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
                    scanResult?.let {
                        addCardNumberEditText.setText(it.cardNumber)
                    } ?: context.showToast(context.getString(R.string.incorrect_data_from_scan))
                }

            }
        }
    }

    interface CardInputListener {
        fun onInputCompleted(cardInputData: CardInputData)
    }
}