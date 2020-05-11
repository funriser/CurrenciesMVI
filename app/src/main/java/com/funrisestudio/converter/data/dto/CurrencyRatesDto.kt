package com.funrisestudio.converter.data.dto


data class CurrencyRatesDto(
    val base: String,
    val rates: Map<String, String>
)