package com.funrisestudio.converter.data.source

import com.funrisestudio.converter.core.di.rates.RatesScope
import com.funrisestudio.converter.domain.entity.ExchangeRates
import javax.inject.Inject

@RatesScope
class CurrencyHolder @Inject constructor() {

    var currentRates: ExchangeRates? = null

    fun isActual(baseCurrencyCode: String): Boolean {
        val rates = currentRates?:return false
        return baseCurrencyCode == rates.baseCurrency.code &&
                rates.rates.isNotEmpty()
    }

}