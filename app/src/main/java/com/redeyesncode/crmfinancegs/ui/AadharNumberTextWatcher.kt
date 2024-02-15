package com.redeyesncode.crmfinancegs.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class AadharNumberTextWatcher(private val editText: EditText) : TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        // Not needed for this example
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        // Not needed for this example
    }

    override fun afterTextChanged(editable: Editable) {
        editText.removeTextChangedListener(this)

        // Remove existing "-" to avoid duplication
        val textWithoutDashes = editable.toString().replace("-", "")

        // Add "-" after every 4 digits
        val formattedText = StringBuilder()
        for (i in 0 until textWithoutDashes.length) {
            formattedText.append(textWithoutDashes[i])
            if ((i + 1) % 4 == 0 && i != textWithoutDashes.length - 1) {
                formattedText.append("-")
            }
        }

        editText.setText(formattedText)
        editText.setSelection(formattedText.length)

        editText.addTextChangedListener(this)
    }
}
