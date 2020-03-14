package com.revolut.converter.domain.entity

import java.math.BigDecimal

open class Currency(
    val code: String,
    val name: String,
    val image: Int,
    var amount: BigDecimal
)

class BaseCurrency(
    code: String,
    name: String,
    image: Int,
    amount: BigDecimal
): Currency(code, name, image, amount)

class ExchangeCurrency(
    code: String,
    name: String,
    image: Int,
    amount: BigDecimal,
    val rate: BigDecimal
) : Currency(code, name, image, amount)