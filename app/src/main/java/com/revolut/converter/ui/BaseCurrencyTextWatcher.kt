package com.revolut.converter.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.revolut.converter.domain.entity.ConvertedCurrency

class BaseCurrencyTextWatcher(
    private val editText: EditText,
    private val viewModel: ConverterViewModel,
    private val convertedCurrency: ConvertedCurrency
): TextWatcher {

    private var lastInspectedSequence: String? = null

    override fun afterTextChanged(s: Editable?) {
        if (!editText.isFocused) {
            return
        }
        viewModel.onNewExchangeAmount(convertedCurrency, s.toString())
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s == null || lastInspectedSequence == s.toString()) {
            return
        }
        //if grouping character was deleted
        val trimmedSource = if (before == 1 && count == 0 &&
            isGroupingChanged(start, lastInspectedSequence, s)) {
            s.removeRange(start - 1, start)
        } else {
            s
        }
        val fractionalInd = trimmedSource.indexOf(DecimalFormat.FRACTION_SIGN)
        //return if only fractional part changed
        if (fractionalInd != -1 && start > fractionalInd) {
            return
        }
        val formattedInput = DecimalFormat.toDecimalString(trimmedSource.toString())
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