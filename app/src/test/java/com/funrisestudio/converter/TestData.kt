package com.funrisestudio.converter

import com.funrisestudio.converter.domain.entity.Currency
import com.funrisestudio.converter.domain.entity.CurrencyRate
import com.funrisestudio.converter.domain.entity.ExchangeRates

object TestData {

    fun mockedRates(): ExchangeRates {
        val baseCurrency = Currency(
            code = "1",
            name = "name",
            image = 0
        )
        return ExchangeRates(
            baseCurrency = baseCurrency,
            rates = listOf(
                CurrencyRate(
                    currency = Currency("2", "name2", 0),
                    rate = 1.0.toBigDecimal()
                ),
                CurrencyRate(
                    currency = Currency("3", "name3", 0),
                    rate = 2.0.toBigDecimal()
                ),
                CurrencyRate(
                    currency = Currency("4", "name4", 0),
                    rate = 3.0.toBigDecimal()
                )
            )
        )
    }

}