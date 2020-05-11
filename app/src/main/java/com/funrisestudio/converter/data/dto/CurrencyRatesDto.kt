package com.funrisestudio.converter.data.dto


data class CurrencyRatesDto(
    val baseCurrency: String,
    val rates: Map<String, String>
)