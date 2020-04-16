package com.revolut.converter.ui.mvi

import com.revolut.converter.core.mvi.Reducer
import com.revolut.converter.ui.error.ErrorHandler
import javax.inject.Inject

class ConverterReducer @Inject constructor(
    private val errorHandler: ErrorHandler
): Reducer<ConverterAction, ConverterViewState> {

    override fun reduce(
        viewState: ConverterViewState,
        action: ConverterAction
    ): ConverterViewState {
        return when(action) {
            is ConverterAction.CurrenciesError -> {
                val errorStr = errorHandler.getErrorMessage(action.throwable)
                ConverterViewState.createErrorState(errorStr)
            }
            is ConverterAction.CurrenciesLoaded -> {
                ConverterViewState.createCurrenciesReceived(action.items)
            }
            is ConverterAction.Loading -> {
                ConverterViewState.createLoadingState()
            }
            is ConverterAction.ObserveCurrency -> viewState
        }
    }

}