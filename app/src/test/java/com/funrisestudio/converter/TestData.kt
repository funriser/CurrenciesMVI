package com.funrisestudio.converter

import com.funrisestudio.converter.data.dto.CurrencyRatesDto
import com.funrisestudio.converter.domain.entity.Currency
import com.funrisestudio.converter.domain.entity.CurrencyRate
import com.funrisestudio.converter.domain.entity.ExchangeRates
import java.math.BigDecimal

object TestData {

    fun mockedRates(baseCurrencyCode: String = "1"): ExchangeRates {
        val baseCurrency = Currency(
            code = baseCurrencyCode,
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

    fun ratesDtoToEntity(): Pair<CurrencyRatesDto, ExchangeRates> {
        val ratesDto = CurrencyRatesDto(
            base = "EUR",
            rates = mapOf(
                "USD" to "1.1581",
                "RUB" to "79.214"
            )
        )
        val ratesEntity = ExchangeRates(
            baseCurrency = Currency("EUR", "Euro", R.drawable.flag_eur),
            rates = listOf(
                CurrencyRate(
                    currency = Currency(
                        "USD", "United States Dollar", R.drawable.flag_usd
                    ),
                    rate = BigDecimal("1.1581")
                ),
                CurrencyRate(
                    currency = Currency(
                        "RUB", "Russia Ruble", R.drawable.flag_rub
                    ),
                    rate = BigDecimal("79.214")
                )
            )
        )
        return ratesDto to ratesEntity
    }

}