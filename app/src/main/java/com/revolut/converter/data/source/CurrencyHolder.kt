package com.revolut.converter.data.source

import com.revolut.converter.core.di.rates.RatesScope
import com.revolut.converter.domain.entity.ExchangeRates
import javax.inject.Inject

@RatesScope
class CurrencyHolder @Inject constructor() {

    var currentRates: ExchangeRates? = null

    fun isValid(baseCurrencyCode: String): Boolean {
        val rates = currentRates?:return false
        return baseCurrencyCode == rates.baseCurrency.code &&
                rates.rates.isNotEmpty()
    }

}