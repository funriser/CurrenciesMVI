package com.revolut.converter.data.repository

import com.mynameismidori.currencypicker.ExtendedCurrency
import com.revolut.converter.data.dto.CurrencyRatesDto
import com.revolut.converter.domain.ZERO
import com.revolut.converter.domain.entity.BaseCurrency
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.entity.ExchangeCurrency
import com.revolut.converter.domain.toDecimal
import javax.inject.Inject

class ConverterMapper @Inject constructor() {

    fun getExchangeCurrencies(ratesDto: CurrencyRatesDto): List<Currency> {
        val baseCurrency = getBaseCurrency(ratesDto)
        return listOf(baseCurrency) + ratesDto.rates.map {
            getExchangeCurrency(it)
        }
    }

    private fun getBaseCurrency(ratesDto: CurrencyRatesDto): BaseCurrency {
        val currencyData = ExtendedCurrency.CURRENCIES.find {
            ratesDto.baseCurrency == it.code
        }
        return BaseCurrency(
            code = ratesDto.baseCurrency,
            name = currencyData?.name.orEmpty(),
            image = currencyData?.flag?:-1
        )
    }

    private fun getExchangeCurrency(rate: Map.Entry<String, String>): ExchangeCurrency {
        val currencyData = ExtendedCurrency.CURRENCIES.find {
            rate.key == it.code
        }
        return ExchangeCurrency(
            code = rate.key,
            name = currencyData?.name.orEmpty(),
            image = currencyData?.flag?:-1,
            rate = rate.value.toDecimal(),
            finalAmount = ZERO
        )
    }

}