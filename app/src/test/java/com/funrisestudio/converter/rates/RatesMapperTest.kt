package com.funrisestudio.converter.rates

import com.funrisestudio.converter.TestData
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
        val mockedRates = TestData.ratesDtoToEntity()
        val result = mapper.getExchangeRates(mockedRates.first)
        assertEquals(mockedRates.second, result)
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