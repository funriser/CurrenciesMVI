package com.funrisestudio.converter.rates

import com.funrisestudio.converter.R
import com.funrisestudio.converter.data.dto.CurrencyRatesDto
import com.funrisestudio.converter.data.repository.RatesMapper
import com.funrisestudio.converter.domain.entity.Currency
import com.funrisestudio.converter.domain.entity.CurrencyRate
import com.funrisestudio.converter.domain.entity.ExchangeRates
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class RatesMapperTest {

    private val mapper = RatesMapper()

    @Test
    fun `should map dto to entity correctly`() {
        val ratesDto = CurrencyRatesDto(
            base = "EUR",
            rates = mapOf(
                "USD" to "1.1581",
                "RUB" to "79.214"
            )
        )
        val expectedResult = ExchangeRates(
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
        val result = mapper.getExchangeRates(ratesDto)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should map possibly unexpected dto to entity`() {
        val ratesDto = CurrencyRatesDto(
            base = "AAA",
            rates = mapOf("BBB" to "0.00")
        )
        val expectedResult = ExchangeRates(
            baseCurrency = Currency("AAA", "", -1),
            rates = listOf(
                CurrencyRate(
                    currency = Currency("BBB", "", -1),
                    rate = BigDecimal("0.00")
                )
            )
        )
        val result = mapper.getExchangeRates(ratesDto)
        assertEquals(expectedResult, result)
    }

}