package com.revolut.converter.ui.input

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.revolut.converter.ui.DecimalFormat

class BaseCurrencyTextWatcher(
    private val editText: EditText,
    private val afterTextChanged: (String) -> Unit
) : TextWatcher {

    private var lastInspectedSequence: String? = null

    override fun afterTextChanged(s: Editable?) {
        if (!editText.isFocused) {
            return
        }
        val source = s.toString()
        //should not propagate callback if there was no changes
        if (lastInspectedSequence == source || source == "0") {
            afterTextChanged.invoke(source)
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s == null || lastInspectedSequence == s.toString()) {
            return
        }
        //if grouping character was deleted
        val trimmedSource = if (before == 1 && count == 0 &&
            isGroupingChanged(start, lastInspectedSequence, s)
        ) {
            s.removeRange(start - 1, start)
        } else {
            s
        }
        val fractionalInd = trimmedSource.indexOf(DecimalFormat.FRACTION_SIGN)
        //return if only fractional part was changed
        if (fractionalInd != -1 && start > fractionalInd) {
            lastInspectedSequence = s.toString()
            return
        }
        val formattedInput =
            DecimalFormat.toDecimalString(trimmedSource.toString())
        lastInspectedSequence = formattedInput
        //if formatted input differs from source
        if (s.toString() != formattedInput) {
            val selection = editText.selectionStart
            val lengthDiff = formattedInput.length - s.length
            editText.setText(formattedInput)
            val newSelection = selection + lengthDiff
            if (newSelection >= 0 && newSelection <= formattedInput.length) {
                editText.setSelection(newSelection)
            }
        }
    }

    private fun isGroupingChanged(
        start: Int,
        oldSequence: CharSequence?,
        newSequence: CharSequence?
    ): Boolean {
        val oldSeqGroupCount = oldSequence?.count { it == DecimalFormat.GROUP_SIGN }
        val newSeqGroupCount = newSequence?.count { it == DecimalFormat.GROUP_SIGN }
        return oldSeqGroupCount != newSeqGroupCount &&
                oldSequence?.get(start) == DecimalFormat.GROUP_SIGN
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

}