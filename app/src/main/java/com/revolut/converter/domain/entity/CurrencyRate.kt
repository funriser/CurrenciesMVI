package com.revolut.converter.domain.entity

import java.math.BigDecimal

data class CurrencyRate(
    val name: String,
    val image: String,
    val rate: BigDecimal
)