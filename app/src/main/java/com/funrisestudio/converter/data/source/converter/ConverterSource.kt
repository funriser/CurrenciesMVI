package com.funrisestudio.converter.data.source.converter

import com.funrisestudio.converter.domain.entity.ExchangeRates
import io.reactivex.Single

interface ConverterSource {
    fun getLatestRates(baseCurrency: String): Single<ExchangeRates>
}

interface ConverterLocalSource: ConverterSource {
    fun saveRates(exchangeRates: ExchangeRates)
    fun hasActualData(actualBaseCurrency: String): Boolean
}

interface ConverterRemoteSource: ConverterSource {

}