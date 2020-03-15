package com.revolut.converter.domain.interactor

import com.revolut.converter.domain.UseCase
import com.revolut.converter.domain.defScale
import com.revolut.converter.domain.entity.BaseConvertedCurrency
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.domain.entity.ExchangeRates
import com.revolut.converter.domain.executor.Executor
import com.revolut.converter.domain.repository.ConverterRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetConvertedCurrencies @Inject constructor(
    executor: Executor,
    private val repository: ConverterRepository
) : UseCase<List<ConvertedCurrency>, GetConvertedCurrencies.Params>(executor) {

    override fun buildObservable(params: Params): Observable<List<ConvertedCurrency>> {
        return Observable.concat(
                listOf(
                    getConvertedCurrencies(params, false).toObservable(),
                    scheduleRateUpdates(params)
                )
            )
            .subscribeOn(executor.workScheduler)
    }

    private fun exchangeCurrencies(
        params: Params,
        exchangeRates: ExchangeRates
    ): List<ConvertedCurrency> {
        val currencies = mutableListOf<ConvertedCurrency>()
        val baseCurrency = BaseConvertedCurrency(
            currency = exchangeRates.baseCurrency,
            amount = params.amount
        )
        val convertedCurrencies = exchangeRates.rates.map {
            ConvertedCurrency(
                currency = it.currency,
                amount = exchangeValue(params.amount, it.rate)
            )
        }
        return currencies.apply {
            add(baseCurrency)
            addAll(convertedCurrencies)
        }
    }

    private fun exchangeValue(amount: BigDecimal, rate: BigDecimal): BigDecimal {
        return (amount * rate).defScale()
    }

    private fun getConvertedCurrencies(
        params: Params,
        forceReload: Boolean
    ): Single<List<ConvertedCurrency>> {
        return repository.getLatestRates(params.baseCurrency, forceReload).map { rates ->
            exchangeCurrencies(params, rates)
        }
    }

    private fun scheduleRateUpdates(params: Params): Observable<List<ConvertedCurrency>> {
        return if (params.amount == BigDecimal.ZERO) {
            //no need to schedule update when input is null
            Observable.empty()
        } else {
            Flowable.interval(1, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .concatMapSingle {
                    getConvertedCurrencies(params, true)
                }
                // throttle to avoid too fast emission after network slows down
                .throttleLatest(1, TimeUnit.SECONDS)
                .toObservable()
        }
    }

    data class Params(
        val baseCurrency: String,
        val amount: BigDecimal
    )

}