package com.funrisestudio.converter.rates

import com.funrisestudio.converter.TestData
import com.funrisestudio.converter.data.source.CurrencyHolder
import com.funrisestudio.converter.data.source.converter.RatesLocalSource
import com.funrisestudio.converter.data.source.converter.RatesLocalSourceImpl
import com.funrisestudio.converter.testutils.test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RatesLocalSourceTest {

    private lateinit var ratesLocalSource: RatesLocalSource
    private lateinit var currencyHolder: CurrencyHolder

    @Before
    fun setUp() {
        currencyHolder = CurrencyHolder()
        ratesLocalSource = RatesLocalSourceImpl(currencyHolder)
    }

    @Test
    fun `should get latest rates correctly`() {
        val rates = TestData.mockedRates()
        currencyHolder.currentRates = rates
        ratesLocalSource.getLatestRates("").test {
            assertNoErrors()
            assertComplete()
            assertTerminated()
            assertValueCount(1)
            assertValue(rates)
        }
    }

    @Test
    fun `should proceed with error if there are no rates`() {
        currencyHolder.currentRates = null
        ratesLocalSource.getLatestRates("").test {
            assertNoValues()
            assertError(IllegalStateException::class.java)
            assertNotComplete()
            assertTerminated()
        }
    }

    @Test
    fun `should save rates correctly`() {
        val ratesToSave = TestData.mockedRates()
        ratesLocalSource.saveRates(ratesToSave)
        assertNotNull(currencyHolder.currentRates)
    }

    @Test
    fun `should check for actual data`() {
        val baseCurrency = "EUR"
        currencyHolder.currentRates = TestData.mockedRates(baseCurrency)
        assertTrue(ratesLocalSource.hasActualData(baseCurrency))
    }

            @Test
    fun `should check for not actual data`() {
        currencyHolder.currentRates = null
        assertFalse(ratesLocalSource.hasActualData(""))
    }

}