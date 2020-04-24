package com.revolut.converter.domain.interactor

import com.revolut.converter.core.mvi.MiddleWare
import com.revolut.converter.ui.rates.mvi.RatesAction
import com.revolut.converter.ui.rates.mvi.RatesViewState
import io.reactivex.Observable
import javax.inject.Inject

class RatesMiddleware @Inject constructor(
    private val getConvertedCurrencies: GetConvertedCurrencies
): MiddleWare<RatesAction, RatesViewState>() {

    override fun bind(actionStream: Observable<RatesAction>): Observable<RatesAction> {
        return actionStream
            .filter(::isHandled)
            .switchMap {
                when (it) {
                    is RatesAction.ObserveCurrency -> {
                        val params = GetConvertedCurrencies.Params(it.baseCurrency, it.amount)
                        getConvertedCurrencies.buildObservable(params)
                            .onErrorReturn { t -> RatesAction.CurrenciesError(t) }
                            .startWith(RatesAction.Loading)
                    }
                    else -> throw IllegalStateException("Action cannot be processed")
                }
            }
    }

    private fun isHandled(action: RatesAction): Boolean {
        return action is RatesAction.ObserveCurrency
    }

}