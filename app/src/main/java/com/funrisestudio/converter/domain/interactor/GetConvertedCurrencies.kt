package com.funrisestudio.converter.domain.interactor

import com.funrisestudio.converter.domain.UseCase
import com.funrisestudio.converter.domain.defScale
import com.funrisestudio.converter.domain.entity.BaseConvertedCurrency
import com.funrisestudio.converter.domain.entity.ConvertedCurrency
import com.funrisestudio.converter.domain.entity.ExchangeRates
import com.funrisestudio.converter.domain.executor.Executor
import com.funrisestudio.converter.domain.repository.RatesRepository
import com.funrisestudio.converter.ui.rates.mvi.RatesAction
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Gets currency rates from repository and performs exchange
 *
 *  Exchange operation:
 *      Take base currency and amount we want to sell
 *      Get latest rates for base currency
 *      Calculate purchase amount for each currency using its rate
 *      Schedule updates to keep data actualized
 */
class GetConvertedCurrencies @Inject constructor(
    executor: Executor,
    private val repository: RatesRepository
) : UseCase<RatesAction, GetConvertedCurrencies.Params>(executor) {

    override fun buildObservable(params: Params): Observable<RatesAction> {
        return Observable.concat(
                listOf(
                    getConvertedCurrencies(params, false).toObservable(),
                    scheduleRateUpdates(params)
                )
            )
            .subscribeOn(executor.workScheduler)
    }

    /**
     * Performs currency exchange
     *
     * @param params holds base currency and amount to sell
     * @param exchangeRates latest currency rates
     *
     * @return currencies with calculated purchase price
     * First element in list describes basic currency
     * Other elements are for currencies we can purchase
     */
    private fun exchangeCurrencies(
        params: Params,
        exchangeRates: ExchangeRates
    ): RatesAction {
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
        currencies.apply {
            add(baseCurrency)
            addAll(convertedCurrencies)
        }
        return RatesAction.CurrenciesLoaded(currencies)
    }

    private fun exchangeValue(amount: BigDecimal, rate: BigDecimal): BigDecimal {
        return (amount * rate).defScale()
    }

    private fun getConvertedCurrencies(
        params: Params,
        forceReload: Boolean
    ): Single<RatesAction> {
        return repository.getLatestRates(params.baseCurrency, forceReload).map { rates ->
            exchangeCurrencies(params, rates)
        }
    }

    private fun scheduleRateUpdates(params: Params): Observable<RatesAction> {
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