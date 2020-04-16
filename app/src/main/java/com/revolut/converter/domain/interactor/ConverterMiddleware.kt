package com.revolut.converter.domain.interactor

import com.revolut.converter.core.mvi.MiddleWare
import com.revolut.converter.ui.mvi.ConverterAction
import com.revolut.converter.ui.mvi.ConverterViewState
import io.reactivex.Observable
import javax.inject.Inject

class ConverterMiddleware @Inject constructor(
    private val getConvertedCurrencies: GetConvertedCurrencies
): MiddleWare<ConverterAction, ConverterViewState>() {

    override fun bind(actionStream: Observable<ConverterAction>): Observable<ConverterAction> {
        return actionStream
            .filter(::isHandled)
            .switchMap {
                when (it) {
                    is ConverterAction.ObserveCurrency -> {
                        val params = GetConvertedCurrencies.Params(it.baseCurrency, it.amount)
                        getConvertedCurrencies.buildObservable(params)
                            .onErrorReturn { t -> ConverterAction.CurrenciesError(t) }
                            .startWith(ConverterAction.Loading)
                    }
                    else -> throw IllegalStateException("Action cannot be processed")
                }
            }
    }

    private fun isHandled(action: ConverterAction): Boolean {
        return action is ConverterAction.ObserveCurrency
    }

}