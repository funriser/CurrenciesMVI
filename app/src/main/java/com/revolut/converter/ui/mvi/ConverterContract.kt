package com.revolut.converter.ui.mvi

import com.revolut.converter.core.mvi.Action
import com.revolut.converter.core.mvi.ViewState
import com.revolut.converter.domain.entity.ConvertedCurrency
import java.math.BigDecimal

sealed class ConverterAction: Action {
    class ObserveCurrency(val baseCurrency: String, val amount: BigDecimal): ConverterAction()
    class CurrenciesLoaded(val items: List<ConvertedCurrency>): ConverterAction()
    class CurrenciesError(val throwable: Throwable): ConverterAction()
    object Loading: ConverterAction()
}

data class ConverterViewState(
    val items: List<ConvertedCurrency>,
    val isLoading: Boolean,
    val error: String
): ViewState {

    companion object {

        fun createEmpty(): ConverterViewState {
            return ConverterViewState(
                items = emptyList(),
                isLoading = false,
                error = ""
            )
        }

        fun createLoadingState(): ConverterViewState {
            return ConverterViewState(
                items = emptyList(),
                isLoading = true,
                error = ""
            )
        }

        fun createCurrenciesReceived(items: List<ConvertedCurrency>): ConverterViewState {
            return ConverterViewState(
                items = items,
                isLoading = false,
                error = ""
            )
        }

        fun createErrorState(error: String): ConverterViewState {
            return ConverterViewState(
                items = emptyList(),
                isLoading = true,
                error = error
            )
        }

    }

}