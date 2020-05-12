package com.funrisestudio.converter.data.source.converter

import com.funrisestudio.converter.data.source.CurrencyHolder
import com.funrisestudio.converter.domain.entity.ExchangeRates
import io.reactivex.Single
import javax.inject.Inject

/**
 * Responsible for accessing in-memory cache
 */
class RatesLocalSourceImpl @Inject constructor(
    private val currencyHolder: CurrencyHolder
): RatesLocalSource {

    override fun getLatestRates(baseCurrency: String): Single<ExchangeRates> {
        val rates = currencyHolder.currentRates
            ?:return Single.error(IllegalStateException("Local storage is empty"))
        return Single.just(rates)
    }

    override fun saveRates(exchangeRates: ExchangeRates) {
        currencyHolder.currentRates = exchangeRates
    }

    override fun hasActualData(actualBaseCurrency: String): Boolean {
        return currencyHolder.isActual(actualBaseCurrency)
    }

}