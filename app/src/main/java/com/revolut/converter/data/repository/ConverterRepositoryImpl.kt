package com.revolut.converter.data.repository

import com.revolut.converter.data.source.ConverterApi
import com.revolut.converter.data.source.CurrencyHolder
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.repository.ConverterRepository
import io.reactivex.Single
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterApi: ConverterApi,
    private val converterMapper: ConverterMapper,
    private val currencyHolder: CurrencyHolder
) : ConverterRepository {

    override fun getLatestRates(baseCurrency: String, forceReload: Boolean): Single<List<Currency>> {
        return if (!forceReload && currencyHolder.isValid(baseCurrency)) {
            getLocalLatestRates()
        } else {
            getRemoteLatestRates(baseCurrency)
                .doOnSuccess {
                    currencyHolder.put(baseCurrency, it)
                }
        }
    }

    private fun getLocalLatestRates(): Single<List<Currency>> {
        return Single.just(currencyHolder.currencyRates)
    }

    private fun getRemoteLatestRates(baseCurrency: String): Single<List<Currency>> {
        return converterApi.getCurrencyRates(baseCurrency)
            .map {
                converterMapper.getExchangeCurrencies(it)
            }
    }

}