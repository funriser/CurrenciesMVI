package com.revolut.converter.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.revolut.converter.domain.entity.BaseConvertedCurrency

class BaseCurrencyTextWatcher(
    private val editText: EditText,
    private val viewModel: ConverterViewModel,
    private val baseExchangeCurrency: BaseConvertedCurrency
): TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        if (!editText.isFocused) {
            return
        }
        viewModel.onNewExchangeAmount(baseExchangeCurrency, s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}