package com.revolut.converter.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.revolut.converter.domain.entity.BaseCurrency

class BaseCurrencyTextWatcher(
    private val editText: EditText,
    private val viewModel: ConverterViewModel,
    private val baseCurrency: BaseCurrency
): TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        if (!editText.isFocused) {
            return
        }
        viewModel.onNewExchangeAmount(baseCurrency, s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}