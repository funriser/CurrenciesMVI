package com.revolut.converter.domain.entity

import java.math.BigDecimal

data class Currency(
    val code: String,
    val name: String,
    val image: Int
)

data class CurrencyRate(
    val currency: Currency,
    val rate: BigDecimal
)

data class ExchangeRates(
    val baseCurrency: Currency,
    val rates: List<CurrencyRate>
)


open class ConvertedCurrency(
    val currency: Currency,
    val amount: BigDecimal
)
class BaseConvertedCurrency(
    currency: Currency,
    amount: BigDecimal
): ConvertedCurrency(currency, amount)