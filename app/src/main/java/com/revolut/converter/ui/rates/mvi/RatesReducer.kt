package com.revolut.converter.ui.rates.mvi

import com.revolut.converter.core.mvi.Reducer
import com.revolut.converter.ui.error.ErrorHandler
import javax.inject.Inject

class RatesReducer @Inject constructor(
    private val errorHandler: ErrorHandler
): Reducer<RatesAction, RatesViewState> {

    override fun reduce(
        viewState: RatesViewState,
        action: RatesAction
    ): RatesViewState {
        return when(action) {
            is RatesAction.CurrenciesError -> {
                val errorStr = errorHandler.getErrorMessage(action.throwable)
                RatesViewState.createErrorState(viewState.items, errorStr)
            }
            is RatesAction.CurrenciesLoaded -> {
                RatesViewState.createCurrenciesReceived(action.items)
            }
            is RatesAction.Loading -> {
                RatesViewState.createLoadingState()
            }
            is RatesAction.ObserveCurrency -> viewState
        }
    }

}