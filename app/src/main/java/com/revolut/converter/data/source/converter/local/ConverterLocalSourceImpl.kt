package com.revolut.converter.data.source.converter.local

import com.revolut.converter.data.source.CurrencyHolder
import com.revolut.converter.data.source.converter.ConverterLocalSource
import com.revolut.converter.domain.entity.ExchangeRates
import io.reactivex.Single
import javax.inject.Inject

class ConverterLocalSourceImpl @Inject constructor(
    private val currencyHolder: CurrencyHolder
): ConverterLocalSource {

    override fun getLatestRates(baseCurrency: String): Single<ExchangeRates> {
        val rates = currencyHolder.currentRates
            ?:return Single.error(IllegalStateException("Local storage is empty"))
        return Single.just(rates)
    }

    override fun saveRates(exchangeRates: ExchangeRates) {
        currencyHolder.currentRates = exchangeRates
    }

    override fun hasActualData(actualBaseCurrency: String): Boolean {
        return currencyHolder.isValid(actualBaseCurrency)
    }

}