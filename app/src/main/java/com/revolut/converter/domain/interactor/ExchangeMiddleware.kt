package com.revolut.converter.domain.interactor

import com.revolut.converter.core.mvi.MiddleWare
import com.revolut.converter.ui.exchange.mvi.ExchangeAction
import com.revolut.converter.ui.exchange.mvi.ExchangeViewState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExchangeMiddleware @Inject constructor(): MiddleWare<ExchangeAction, ExchangeViewState>() {

    override fun bind(actionStream: Observable<ExchangeAction>): Observable<ExchangeAction> {
        return actionStream
            .filter(::isHandled)
            .switchMap {
                when(it) {
                    is ExchangeAction.PerformExchange -> {
                        performExchangeObservable()
                    }
                    else -> throw IllegalStateException("Action cannot be processed")
                }
            }
    }

    private fun performExchangeObservable(): Observable<ExchangeAction> {
        val delayMillis = 1000L
        return Observable
            .just(ExchangeAction.CurrenciesExchanged as ExchangeAction)
            .delay(delayMillis, TimeUnit.MILLISECONDS)
            .startWith(ExchangeAction.ExchangeLoading)
    }

    private fun isHandled(action: ExchangeAction): Boolean {
        return action is ExchangeAction.PerformExchange
    }

}