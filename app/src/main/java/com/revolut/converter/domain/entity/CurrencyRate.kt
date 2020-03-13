package com.revolut.converter.domain.entity

import java.math.BigDecimal

open class Currency(val name: String, val image: String)
class BaseCurrency(name: String, image: String): Currency(name, image)
class ExchangeCurrency(
    name: String,
    image: String,
    val rate: BigDecimal,
    var finalAmount: BigDecimal
) : Currency(name, image)