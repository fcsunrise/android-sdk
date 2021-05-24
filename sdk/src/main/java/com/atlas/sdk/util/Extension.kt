package com.atlas.sdk.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import io.card.payment.CardIOActivity

internal fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

internal fun openScan(scanId: Int, activity: Activity) {

    Intent(activity, CardIOActivity::class.java).apply {
        putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true) // default: false
        putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true) // default: false
        putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true) // default: false
        ActivityCompat.startActivityForResult(activity, this, scanId, null)
    }
}