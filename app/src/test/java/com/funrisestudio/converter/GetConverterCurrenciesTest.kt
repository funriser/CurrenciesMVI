package com.funrisestudio.converter

import com.nhaarman.mockito_kotlin.*
import com.funrisestudio.converter.data.exceptions.NetworkException
import com.funrisestudio.converter.domain.entity.Currency
import com.funrisestudio.converter.domain.interactor.GetConvertedCurrencies
import com.funrisestudio.converter.domain.repository.ConverterRepository
import com.funrisestudio.converter.ui.rates.mvi.RatesAction
import com.funrisestudio.converter.utils.test
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetConverterCurrenciesTest {

    private lateinit var interactor: GetConvertedCurrencies

    private val executor = TestExecutor()
    private val repository: ConverterRepository = mock()

    @Before
    fun setUp() {
        interactor = GetConvertedCurrencies(executor, repository)
    }

    @Test
    fun `should get latest currencies and schedule updates`() {
        val currency = "EUR"
        val amount = "100".toBigDecimal()

        whenever(repository.getLatestRates(currency, false))
            .thenReturn(Single.just(TestData.mockedRates()))
        whenever(repository.getLatestRates(currency, true))
            .thenReturn(Single.just(TestData.mockedRates()))

        val params = GetConvertedCurrencies.Params(currency, amount)
        interactor.buildObservable(params).test(3) {
            assertValueCount(3)
            assertNoErrors()
            assertNotComplete()
            assertNotTerminated()
        }

        verify(repository).getLatestRates(currency, false)
        verify(repository, atLeast(2)).getLatestRates(currency, true)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `should not schedule updates if first fetch failed`() {
        val currency = "EUR"
        val amount = "100".toBigDecimal()

        whenever(repository.getLatestRates(currency, false))
            .thenReturn(Single.error(NetworkException()))

        val params = GetConvertedCurrencies.Params(currency, amount)
        interactor.buildObservable(params).test {
            assertNoValues()
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(repository).getLatestRates(currency, false)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `should stop updates in case of error`() {
        val currency = "EUR"
        val amount = "100".toBigDecimal()

        whenever(repository.getLatestRates(currency, false))
            .thenReturn(Single.just(TestData.mockedRates()))
        whenever(repository.getLatestRates(currency, true))
            .thenReturn(Single.error(NetworkException()))

        val params = GetConvertedCurrencies.Params(currency, amount)
        interactor.buildObservable(params).test {
            assertValueCount(1)
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(repository).getLatestRates(currency, false)
        verify(repository).getLatestRates(currency, true)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `should not schedule updates if exchange amount is 0`() {
        val currency = "EUR"
        val amount = "0".toBigDecimal()

        whenever(repository.getLatestRates(currency, false))
            .thenReturn(Single.just(TestData.mockedRates()))

        val params = GetConvertedCurrencies.Params(currency, amount)
        interactor.buildObservable(params).test {
            assertValueCount(1)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(repository).getLatestRates(currency, false)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `should calculate exchange correctly`() {
        val currency = "EUR"
        val amount = "100".toBigDecimal()

        whenever(repository.getLatestRates(currency, false))
            .thenReturn(Single.just(TestData.mockedRates()))
        whenever(repository.getLatestRates(currency, true))
            .thenReturn(Single.just(TestData.mockedRates()))

        val params = GetConvertedCurrencies.Params(currency, amount)
        interactor.buildObservable(params).test(3) {
            assertValueCount(3)
            assertNoErrors()
            assertNotComplete()
            assertNotTerminated()
            assertValueAt(0) { isExchangedCorrectly(it) }
            assertValueAt(1) { isExchangedCorrectly(it) }
            assertValueAt(2) { isExchangedCorrectly(it) }
        }

        verify(repository).getLatestRates(currency, false)
        verify(repository, atLeast(2)).getLatestRates(currency, true)
        verifyNoMoreInteractions(repository)
    }

    private fun isExchangedCorrectly(ratesAction: RatesAction): Boolean {
        if (ratesAction !is RatesAction.CurrenciesLoaded) {
            return false
        }
        val result = ratesAction.items
        val rates = TestData.mockedRates()
        if (result.size != 4) {
            return false
        }
        val expectedBaseCurrency = Currency(
            code = "1",
            name = "name",
            image = 0
        )
        //base currency should be filled correctly
        if (result[0].currency != expectedBaseCurrency) {
            return false
        }
        result.forEachIndexed { i, currency ->
            if (i == 0) return@forEachIndexed
            //currency should be equal
            if (currency.currency != rates.rates[i - 1].currency) {
                return false
            }
        }
        //base currency stays the same
        if (result[0].amount != "100".toBigDecimal()) {
            return false
        }
        if (result[1].amount != "100.00".toBigDecimal()) {
            return false
        }
        if (result[2].amount != "200.00".toBigDecimal()) {
            return false
        }
        if (result[3].amount != "300.00".toBigDecimal()) {
            return false
        }
        return true
    }

}