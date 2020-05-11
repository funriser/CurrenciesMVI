package com.funrisestudio.converter.data.source.converter

import com.funrisestudio.converter.data.repository.ConverterMapper
import com.funrisestudio.converter.data.source.ApiHandler
import com.funrisestudio.converter.data.source.ConverterApi
import com.funrisestudio.converter.domain.entity.ExchangeRates
import io.reactivex.Single
import javax.inject.Inject

/**
 * Responsible for network interactions and handling api response
 */
class ConverterRemoteSourceImpl @Inject constructor(
    private val converterApi: ConverterApi,
    private val apiHandler: ApiHandler,
    private val currencyMapper: ConverterMapper
): ConverterRemoteSource {

    override fun getLatestRates(baseCurrency: String): Single<ExchangeRates> {
        val request = converterApi.getCurrencyRates(baseCurrency)
        return apiHandler.handleRequestSingle(request).map {
            currencyMapper.getExchangeRates(it)
        }
    }

}