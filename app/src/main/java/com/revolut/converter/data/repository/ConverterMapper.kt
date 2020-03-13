package com.revolut.converter.data.repository

import com.revolut.converter.data.dto.CurrencyRatesDto
import com.revolut.converter.domain.ZERO
import com.revolut.converter.domain.entity.BaseCurrency
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.entity.ExchangeCurrency
import com.revolut.converter.domain.toDecimal
import javax.inject.Inject

class ConverterMapper @Inject constructor() {

    fun getExchangeCurrencies(ratesDto: CurrencyRatesDto): List<Currency> {
        val baseCurrency = BaseCurrency(ratesDto.baseCurrency,"")
        return listOf(baseCurrency) + ratesDto.rates.map {
            ExchangeCurrency(
                name = it.key,
                image = "",
                rate = it.value.toDecimal(),
                finalAmount = ZERO
            )
        }
    }

}