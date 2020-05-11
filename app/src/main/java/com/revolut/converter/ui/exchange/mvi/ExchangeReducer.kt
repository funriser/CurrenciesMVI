package com.revolut.converter.ui.exchange.mvi

import com.revolut.converter.core.mvi.Reducer
import javax.inject.Inject

class ExchangeReducer @Inject constructor(): Reducer<ExchangeAction, ExchangeViewState> {

    override fun reduce(
        viewState: ExchangeViewState,
        action: ExchangeAction
    ): ExchangeViewState {
        return when (action) {
            is ExchangeAction.ExchangeLoading -> {
                ExchangeViewState.createLoadingState(viewState.items)
            }
            is ExchangeAction.CurrenciesExchanged -> {
                ExchangeViewState.createExchangesState(viewState.items)
            }
            else -> viewState
        }
    }

}