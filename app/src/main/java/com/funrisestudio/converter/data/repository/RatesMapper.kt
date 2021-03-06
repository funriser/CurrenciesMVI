package com.funrisestudio.converter.data.repository

import com.mynameismidori.currencypicker.ExtendedCurrency
import com.funrisestudio.converter.data.dto.CurrencyRatesDto
import com.funrisestudio.converter.domain.entity.*
import javax.inject.Inject

/**
 * Maps DTOs from data sources to business entities
 */
class RatesMapper @Inject constructor() {

    fun getExchangeRates(ratesDto: CurrencyRatesDto): ExchangeRates {
        val baseCurrency = getCurrency(ratesDto.base)
        val currencyRates = ratesDto.rates.map {
            getCurrencyRate(it)
        }
        return ExchangeRates(baseCurrency, currencyRates)
    }

    private fun getCurrency(currencyCode: String): Currency {
        val currencyData = ExtendedCurrency.CURRENCIES.find {
            currencyCode == it.code
        }
        currencyData.let {
            return Currency(
                code = currencyCode,
                name = it?.name.orEmpty(),
                image = it?.flag ?: -1
            )
        }
    }

    private fun getCurrencyRate(rateEntry: Map.Entry<String, String>): CurrencyRate {
        val currency = getCurrency(rateEntry.key)
        return CurrencyRate(currency, rateEntry.value.toBigDecimal())
    }

}