package com.revolut.converter.data.source

import com.revolut.converter.di.converter.ConverterScope
import com.revolut.converter.domain.entity.ExchangeRates
import javax.inject.Inject

@ConverterScope
class CurrencyHolder @Inject constructor() {

    var currentRates: ExchangeRates? = null

    fun isValid(baseCurrencyCode: String): Boolean {
        val rates = currentRates?:return false
        return baseCurrencyCode == rates.baseCurrency.code &&
                rates.rates.isNotEmpty()
    }

}