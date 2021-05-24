package com.atlas.sdk.util

import android.text.Editable
import android.text.TextWatcher

internal class MaskWatcher(private val mask: String) : TextWatcher {

    private var isRunning = false

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        if (isRunning) {
            return
        }
        isRunning = true

        var position = 0
        var result = ""

        var text = editable.toString()
        for (char in mask) {
            if (char != '#')
                text = text.replace(char.toString(), "")
        }

        if (mask.contains("?#")) {
            result += mask.substringBefore("?#")
            result += text
            result += mask.substringAfter("?#")
        } else {

            for (char in mask) {
                if (char == '#') {
                    if (position < text.length) {
                        result += text[position]
                        position++
                    } else
                        break
                } else {
                    if (position < text.length)
                        result += char
                    else
                        break
                }
            }
        }

        editable.replace(0, editable.length, result)

        isRunning = false
    }
}