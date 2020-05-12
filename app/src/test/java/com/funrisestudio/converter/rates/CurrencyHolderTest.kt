package com.funrisestudio.converter.rates

import com.funrisestudio.converter.R
import com.funrisestudio.converter.TestData
import com.funrisestudio.converter.data.source.CurrencyHolder
import com.funrisestudio.converter.domain.entity.Currency
import com.funrisestudio.converter.domain.entity.CurrencyRate
import com.funrisestudio.converter.domain.entity.ExchangeRates
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class CurrencyHolderTest {

    private lateinit var currencyHolder: CurrencyHolder

    @Before
    fun setUp() {
        currencyHolder = CurrencyHolder()
    }

    @Test
    fun `should hold exchange rates`() {
        val rates = TestData.mockedRates()
        currencyHolder.currentRates = rates
        assertEquals(rates, currencyHolder.currentRates)
    }

    @Test
    fun `data should be actual if information is complete and corresponds to base currency`() {
        currencyHolder.currentRates = ExchangeRates(
            baseCurrency = Currency("EUR", "Euro", R.drawable.flag_eur),
            rates = listOf(
                CurrencyRate(
                    currency = Currency(
                        "USD", "United States Dollar", R.drawable.flag_usd
                    ),
                    rate = BigDecimal("1.1581")
                )
            )
        )
        assertTrue(currencyHolder.isActual("EUR"))
    }

    @Test
    fun `data should not be actual if holds null`() {
        currencyHolder.currentRates = null
        assertFalse(currencyHolder.isActual(""))
    }

    @Test
    fun `data should not be actual if does not correspond to base currency`() {
        currencyHolder.currentRates = ExchangeRates(
            baseCurrency = Currency("EUR", "Euro", R.drawable.flag_eur),
            rates = listOf(
                CurrencyRate(
                    currency = Currency(
                        "USD", "United States Dollar", R.drawable.flag_usd
                    ),
                    rate = BigDecimal("1.1581")
                )
            )
        )
        //not valid because base currency is EUR
        assertFalse(currencyHolder.isActual("USD"))
    }

    @Test
    fun `data should not be actual if there are not rates for base currency`() {
        currencyHolder.currentRates = ExchangeRates(
            baseCurrency = Currency("EUR", "Euro", R.drawable.flag_eur),
            rates = listOf()
        )
        assertFalse(currencyHolder.isActual("EUR"))
    }

}