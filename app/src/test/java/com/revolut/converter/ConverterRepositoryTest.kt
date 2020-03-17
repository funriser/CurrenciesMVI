package com.revolut.converter

import com.nhaarman.mockito_kotlin.*
import com.revolut.converter.data.exceptions.NetworkException
import com.revolut.converter.data.repository.ConverterRepositoryImpl
import com.revolut.converter.data.source.converter.ConverterLocalSource
import com.revolut.converter.data.source.converter.ConverterRemoteSource
import com.revolut.converter.domain.repository.ConverterRepository
import com.revolut.converter.utils.test
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class ConverterRepositoryTest {

    private lateinit var repository: ConverterRepository

    private val converterRemoteSource: ConverterRemoteSource = mock()
    private val converterLocalSource: ConverterLocalSource = mock()

    @Before
    fun setUp() {
        repository = ConverterRepositoryImpl(
            converterRemoteSource, converterLocalSource
        )
    }

    @Test
    fun `should fetch data from remote source if local storage has not data`() {
        val currency = "EUR"
        val rates = TestData.mockedRates()
        whenever(converterLocalSource.hasActualData(currency)).thenReturn(false)
        whenever(converterRemoteSource.getLatestRates(currency))
            .thenReturn(Single.just(rates))

        repository.getLatestRates(currency, false).test {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(converterLocalSource).hasActualData(currency)
        verify(converterRemoteSource).getLatestRates(currency)
        verify(converterLocalSource).saveRates(rates)
        verifyNoMoreInteractions(converterLocalSource)
        verifyNoMoreInteractions(converterRemoteSource)
    }

    @Test
    fun `should fetch data from remote source if force reload requested`() {
        val currency = "EUR"
        val rates = TestData.mockedRates()
        whenever(converterRemoteSource.getLatestRates(currency))
            .thenReturn(Single.just(rates))

        repository.getLatestRates(currency, true).test {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(converterRemoteSource).getLatestRates(currency)
        verify(converterLocalSource).saveRates(rates)
        verifyNoMoreInteractions(converterLocalSource)
        verifyNoMoreInteractions(converterRemoteSource)
    }

    @Test
    fun `should fetch data from local source if local data is valid`() {
        val currency = "EUR"
        val rates = TestData.mockedRates()
        whenever(converterLocalSource.hasActualData(currency)).thenReturn(true)
        whenever(converterLocalSource.getLatestRates(currency))
            .thenReturn(Single.just(rates))

        repository.getLatestRates(currency, false).test {
            assertValue(rates)
            assertNoErrors()
            assertComplete()
            assertTerminated()
        }

        verify(converterLocalSource).hasActualData(currency)
        verify(converterLocalSource).getLatestRates(currency)
        verifyNoMoreInteractions(converterLocalSource)
        verifyZeroInteractions(converterRemoteSource)
    }

    @Test
    fun `should propagate remote source error`() {
        val currency = "EUR"
        whenever(converterRemoteSource.getLatestRates(currency))
            .thenReturn(Single.error(NetworkException()))

        repository.getLatestRates(currency, true).test {
            assertNoValues()
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(converterRemoteSource).getLatestRates(currency)
        verifyNoMoreInteractions(converterRemoteSource)
        verifyZeroInteractions(converterLocalSource)
    }

    @Test
    fun `should propagate local source error`() {
        val currency = "EUR"
        whenever(converterLocalSource.hasActualData(currency)).thenReturn(true)
        whenever(converterLocalSource.getLatestRates(currency))
            .thenReturn(Single.error(IllegalStateException()))

        repository.getLatestRates(currency, false).test {
            assertNoValues()
            assertError(IllegalStateException::class.java)
            assertNotComplete()
            assertTerminated()
        }

        verify(converterLocalSource).hasActualData(currency)
        verify(converterLocalSource).getLatestRates(currency)
        verifyNoMoreInteractions(converterLocalSource)
        verifyZeroInteractions(converterRemoteSource)
    }

}