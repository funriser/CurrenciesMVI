package com.revolut.converter.domain.entity

import java.math.BigDecimal

open class Currency(val code: String, val name: String, val image: Int)

class BaseCurrency(
    code: String,
    name: String,
    image: Int
): Currency(code, name, image)

class ExchangeCurrency(
    code: String,
    name: String,
    image: Int,
    val rate: BigDecimal,
    var finalAmount: BigDecimal
) : Currency(code, name, image)