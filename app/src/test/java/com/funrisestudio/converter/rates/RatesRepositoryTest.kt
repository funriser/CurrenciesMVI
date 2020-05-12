package com.funrisestudio.converter.rates

import com.funrisestudio.converter.TestData
import com.nhaarman.mockito_kotlin.*
import com.funrisestudio.converter.data.exceptions.NetworkException
import com.funrisestudio.converter.data.repository.RatesRepositoryImpl
import com.funrisestudio.converter.data.source.converter.RatesLocalSource
import com.funrisestudio.converter.data.source.converter.RatesRemoteSource
import com.funrisestudio.converter.domain.repository.RatesRepository
import com.funrisestudio.converter.testutils.test
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class RatesRepositoryTest {

    private lateinit var repository: RatesRepository

    private val ratesRemoteSource: RatesRemoteSource = mock()
    private val ratesLocalSource: RatesLocalSource = mock()

    @Before
    fun setUp() {
        repository = RatesRepositoryImpl(
            ratesRemoteSource, ratesLocalSource
        )
    }

    @Test
    fun `should fetch data from remote source if local storage has not data`() {
        val currency = "EUR"
        val rates = TestData.mockedRates()
        whenever(ratesLocalSource.hasActualData(currency)).thenReturn(false)
        whenever(ratesRemoteSource.getLatestRates(currency))
            .thenReturn(Single.just(rates))

        repository.getLatestRates(currency, false).test {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(ratesLocalSource).hasActualData(currency)
        verify(ratesRemoteSource).getLatestRates(currency)
        verify(ratesLocalSource).saveRates(rates)
        verifyNoMoreInteractions(ratesLocalSource)
        verifyNoMoreInteractions(ratesRemoteSource)
    }

    @Test
    fun `should fetch data from remote source if force reload requested`() {
        val currency = "EUR"
        val rates = TestData.mockedRates()
        whenever(ratesRemoteSource.getLatestRates(currency))
            .thenReturn(Single.just(rates))

        repository.getLatestRates(currency, true).test {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(ratesRemoteSource).getLatestRates(currency)
        verify(ratesLocalSource).saveRates(rates)
        verifyNoMoreInteractions(ratesLocalSource)
        verifyNoMoreInteractions(ratesRemoteSource)
    }

    @Test
    fun `should fetch data from local source if local data is valid`() {
        val currency = "EUR"
        val rates = TestData.mockedRates()
        whenever(ratesLocalSource.hasActualData(currency)).thenReturn(true)
        whenever(ratesLocalSource.getLatestRates(currency))
            .thenReturn(Single.just(rates))

        repository.getLatestRates(currency, false).test {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(ratesLocalSource).hasActualData(currency)
        verify(ratesLocalSource).getLatestRates(currency)
        verifyNoMoreInteractions(ratesLocalSource)
        verifyZeroInteractions(ratesRemoteSource)
    }

    @Test
    fun `should propagate remote source error`() {
        val currency = "EUR"
        whenever(ratesRemoteSource.getLatestRates(currency))
            .thenReturn(Single.error(NetworkException()))

        repository.getLatestRates(currency, true).test {
            assertNoValues()
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(ratesRemoteSource).getLatestRates(currency)
        verifyNoMoreInteractions(ratesRemoteSource)
        verifyZeroInteractions(ratesLocalSource)
    }

    @Test
    fun `should propagate local source error`() {
        val currency = "EUR"
        whenever(ratesLocalSource.hasActualData(currency)).thenReturn(true)
        whenever(ratesLocalSource.getLatestRates(currency))
            .thenReturn(Single.error(IllegalStateException()))

        repository.getLatestRates(currency, false).test {
            assertNoValues()
            assertError(IllegalStateException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(ratesLocalSource).hasActualData(currency)
        verify(ratesLocalSource).getLatestRates(currency)
        verifyNoMoreInteractions(ratesLocalSource)
        verifyZeroInteractions(ratesRemoteSource)
    }

}