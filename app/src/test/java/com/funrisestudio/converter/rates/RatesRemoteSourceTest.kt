package com.funrisestudio.converter.rates

import com.funrisestudio.converter.TestData
import com.funrisestudio.converter.data.repository.RatesMapper
import com.funrisestudio.converter.data.source.ApiHandler
import com.funrisestudio.converter.data.source.ConverterApi
import com.funrisestudio.converter.data.source.converter.RatesRemoteSource
import com.funrisestudio.converter.data.source.converter.RatesRemoteSourceImpl
import com.funrisestudio.converter.testutils.test
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import retrofit2.Response


class RatesRemoteSourceTest {

    private lateinit var ratesRemoteSource: RatesRemoteSource

    private val converterApi: ConverterApi = mock()
    private val apiHandler = ApiHandler()
    private val ratesMapper = RatesMapper()

    @Before
    fun setUp() {
        ratesRemoteSource = RatesRemoteSourceImpl(converterApi, apiHandler, ratesMapper)
    }

    @Test
    fun `should get latest rates correctly`() {
        val baseCurrency = "EUR"
        val mockedRates = TestData.ratesDtoToEntity()
        whenever(converterApi.getCurrencyRates(baseCurrency))
            .thenReturn(Single.just(Response.success(mockedRates.first)))

        ratesRemoteSource.getLatestRates(baseCurrency).test {
            assertValueCount(1)
            assertNoErrors()
            assertComplete()
            assertTerminated()
            assertValue(mockedRates.second)
        }

        verify(converterApi).getCurrencyRates("EUR")
        verifyNoMoreInteractions(converterApi)
    }

    @Test
    fun `should proceed with error`() {
        whenever(converterApi.getCurrencyRates(any()))
            .thenReturn(Single.error(IllegalStateException()))
        ratesRemoteSource.getLatestRates("EUR").test {
            assertNoValues()
            assertError(IllegalStateException::class.java)
            assertNotComplete()
            assertTerminated()
        }
        verify(converterApi).getCurrencyRates(any())
        verifyNoMoreInteractions(converterApi)
    }

}