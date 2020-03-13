package com.revolut.converter.domain.interactor

import android.util.Log
import com.revolut.converter.domain.UseCase
import com.revolut.converter.domain.defScale
import com.revolut.converter.domain.entity.CurrencyRate
import com.revolut.converter.domain.executor.Executor
import com.revolut.converter.domain.repository.ConverterRepository
import com.revolut.converter.domain.times
import com.revolut.converter.domain.toDecimal
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetCurrencyRates @Inject constructor(
    executor: Executor,
    private val repository: ConverterRepository
) : UseCase<List<CurrencyRate>, GetCurrencyRates.Params>(executor) {

    override fun buildObservable(params: Params): Observable<List<CurrencyRate>> {
        return Flowable.interval(1, TimeUnit.SECONDS)
            .onBackpressureDrop()
            .concatMapSingle {
                repository.getLatestRates(params.baseCurrency)
                    .map {
                        exchange(it, params.amount)
                    }
            }
            // throttle to avoid too fast emission after network slows down
            .throttleLatest(1, TimeUnit.SECONDS)
            .toObservable()
            .subscribeOn(executor.workScheduler)
    }

    private fun exchange(rates: Map<String, String>, amount: Double): List<CurrencyRate> {
        val toExchange = amount.toDecimal()
        return rates.map {
            CurrencyRate(
                name = it.key,
                image = "",
                rate = (toExchange * it.value.toDecimal()).defScale()
            )
        }
    }

    data class Params(
        val baseCurrency: String,
        val amount: Double
    )

}