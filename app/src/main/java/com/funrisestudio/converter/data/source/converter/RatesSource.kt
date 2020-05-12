package com.funrisestudio.converter.data.source.converter

import com.funrisestudio.converter.domain.entity.ExchangeRates
import io.reactivex.Single

interface RatesSource {
    fun getLatestRates(baseCurrency: String): Single<ExchangeRates>
}

interface RatesLocalSource: RatesSource {
    fun saveRates(exchangeRates: ExchangeRates)
    fun hasActualData(actualBaseCurrency: String): Boolean
}

interface RatesRemoteSource: RatesSource {

}