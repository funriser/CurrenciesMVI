package com.revolut.converter.domain.interactor

import com.revolut.converter.domain.UseCase
import com.revolut.converter.domain.defScale
import com.revolut.converter.domain.entity.BaseCurrency
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.entity.ExchangeCurrency
import com.revolut.converter.domain.executor.Executor
import com.revolut.converter.domain.repository.ConverterRepository
import com.revolut.converter.domain.times
import com.revolut.converter.domain.toDecimal
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetCurrencies @Inject constructor(
    executor: Executor,
    private val repository: ConverterRepository
) : UseCase<List<Currency>, GetCurrencies.Params>(executor) {

    override fun buildObservable(params: Params): Observable<List<Currency>> {
        return Observable.concat(
                mutableListOf(
                    getExchangedRates(params, false).toObservable(),
                    scheduleRateUpdates(params)
                )
            )
            .subscribeOn(executor.workScheduler)
    }

    private fun applyExchange(currencies: List<Currency>, amount: Double) {
        val amountToExchange = amount.toDecimal()
        currencies.forEach {
            when(it) {
                is ExchangeCurrency -> {
                    it.amount = (it.rate * amountToExchange).defScale()
                }
                is BaseCurrency -> {
                    it.amount = amountToExchange
                }
            }
        }
    }

    private fun getExchangedRates(params: Params, forceReload: Boolean): Single<List<Currency>> {
        return repository.getLatestRates(params.baseCurrency, forceReload).map { rates ->
            rates.also {
                applyExchange(it, params.amount)
            }
        }
    }

    private fun scheduleRateUpdates(params: Params): Observable<List<Currency>> {
        return Flowable.interval(1, TimeUnit.SECONDS)
            .onBackpressureDrop()
            .concatMapSingle {
                getExchangedRates(params, true)
            }
            // throttle to avoid too fast emission after network slows down
            .throttleLatest(1, TimeUnit.SECONDS)
            .toObservable()
    }

    data class Params(
        val baseCurrency: String,
        val amount: Double
    )

}