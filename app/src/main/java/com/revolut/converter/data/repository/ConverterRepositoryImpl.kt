package com.revolut.converter.data.repository

import com.revolut.converter.data.source.ConverterApi
import com.revolut.converter.data.source.CurrencyHolder
import com.revolut.converter.domain.entity.ExchangeRates
import com.revolut.converter.domain.repository.ConverterRepository
import io.reactivex.Single
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterApi: ConverterApi,
    private val converterMapper: ConverterMapper,
    private val currencyHolder: CurrencyHolder
) : ConverterRepository {

    override fun getLatestRates(baseCurrency: String, forceReload: Boolean): Single<ExchangeRates> {
        return if (!forceReload && currencyHolder.isValid(baseCurrency)) {
            getLocalLatestRates()
        } else {
            getRemoteLatestRates(baseCurrency)
                .doOnSuccess {
                    currencyHolder.currentRates = it
                }
        }
    }

    private fun getLocalLatestRates(): Single<ExchangeRates> {
        val rates = currencyHolder.currentRates
            ?:return Single.error(IllegalStateException("Local storage is empty"))
        return Single.just(rates)
    }

    private fun getRemoteLatestRates(baseCurrency: String): Single<ExchangeRates> {
        return converterApi.getCurrencyRates(baseCurrency)
            .map {
                converterMapper.getExchangeRates(it)
            }
    }

}