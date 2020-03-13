package com.revolut.converter.data.source

import com.revolut.converter.di.converter.ConverterScope
import com.revolut.converter.domain.entity.Currency
import javax.inject.Inject

@ConverterScope
class CurrencyHolder @Inject constructor() {

    private var currentBaseRate: String? = null
    var currencyRates: List<Currency> = listOf()
        private set

    fun isValid(baseCurrency: String): Boolean {
        return baseCurrency == currentBaseRate && currencyRates.isNotEmpty()
    }

    fun put(baseRate: String, rates: List<Currency>) {
        currentBaseRate = baseRate
        currencyRates = rates
    }

}