package com.atlas.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.atlas.R
import com.atlas.model.TokenPayment


fun showProgressDialog(context: Context, message: String): Dialog {

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_progress)
    dialog.window!!.setGravity(Gravity.CENTER)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val progressTextTextView = dialog.findViewById(R.id.progressTextTextView) as TextView
    progressTextTextView.text = message
    dialog.show()
    return dialog
}

fun showInputDialog(context: Context, message: String, onClickOk: (String) -> Unit): Dialog{

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_input)
    dialog.window!!.setGravity(Gravity.CENTER)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val messageTextView = dialog.findViewById(R.id.messageTextView) as TextView
    messageTextView.text = message

    val transactionNumberEditText = dialog.findViewById(R.id.transactionNumberEditText) as EditText

    val okButton = dialog.findViewById(R.id.okTextView) as TextView
    okButton.setOnClickListener {

        if (transactionNumberEditText.text.isNotEmpty()){
            onClickOk(transactionNumberEditText.text.toString())
        }
        dialog.dismiss()
    }
    dialog.show()
    return dialog
}

fun showInputNumberDialog(context: Context, message: String, onClickOk: (String) -> Unit): Dialog{

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_input_number)
    dialog.window!!.setGravity(Gravity.CENTER)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val messageTextView = dialog.findViewById(R.id.messageTextView) as TextView
    messageTextView.text = message

    val numberEditText = dialog.findViewById(R.id.numberEditText) as EditText

    val okButton = dialog.findViewById(R.id.okTextView) as TextView
    okButton.setOnClickListener {

        if (numberEditText.text.isNotEmpty()){
            onClickOk(numberEditText.text.toString())
        }
        dialog.dismiss()
    }
    dialog.show()
    return dialog
}

fun showDoubleInputDialog(context: Context, message: String, onClickOk: (TokenPayment) -> Unit): Dialog{

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_double_input)
    dialog.window!!.setGravity(Gravity.CENTER)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val messageTextView = dialog.findViewById(R.id.messageTextView) as TextView
    messageTextView.text = message

    val summEditText          = dialog.findViewById(R.id.summEditText) as EditText
    val tokenEditText         = dialog.findViewById(R.id.tokenEditText) as EditText

    val okButton = dialog.findViewById(R.id.okTextView) as TextView
    okButton.setOnClickListener {

        if (summEditText.text.isNotEmpty() && tokenEditText.text.isNotEmpty()){

            onClickOk(TokenPayment(
                    summEditText.text.toString(),
                    tokenEditText.text.toString(),
                    ""
            ))
        }
        dialog.dismiss()
    }
    dialog.show()
    return dialog
}

fun showInputCardDialog(context: Context, message: String, onClickOk: (TokenPayment) -> Unit): Dialog{

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_input_card)
    dialog.window!!.setGravity(Gravity.CENTER)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val messageTextView = dialog.findViewById(R.id.messageTextView) as TextView
    messageTextView.text = message

    val tokenEditText        = dialog.findViewById(R.id.tokenEditText) as EditText
    val cvvEditText          = dialog.findViewById(R.id.cvvEditText) as EditText

    val okButton = dialog.findViewById(R.id.okTextView) as TextView
    okButton.setOnClickListener {

        if (tokenEditText.text.isNotEmpty() && cvvEditText.text.isNotEmpty() ){

            onClickOk(TokenPayment(
                    "",
                    tokenEditText.text.toString(),
                    cvvEditText.text.toString()
            ))
        }
        dialog.dismiss()
    }
    dialog.show()
    return dialog
}