package com.revolut.converter.ui.rates.mvi

import com.revolut.converter.core.mvi.Action
import com.revolut.converter.core.mvi.SingleAction
import com.revolut.converter.core.mvi.ViewState
import com.revolut.converter.domain.entity.ConvertedCurrency
import java.math.BigDecimal

sealed class RatesAction: Action {
    class ObserveCurrency(val baseCurrency: String, val amount: BigDecimal): RatesAction()
    class CurrenciesLoaded(val items: List<ConvertedCurrency>): RatesAction()
    class CurrenciesError(val throwable: Throwable): RatesAction()
    object Loading: RatesAction()
}

sealed class RatesSingleAction: SingleAction {
    abstract class RatesNavAction: RatesSingleAction()
}

data class RatesViewState(
    val items: List<ConvertedCurrency>,
    val isLoading: Boolean,
    val error: String
): ViewState {

    companion object {

        fun createEmpty(): RatesViewState {
            return RatesViewState(
                items = emptyList(),
                isLoading = false,
                error = ""
            )
        }

        fun createLoadingState(): RatesViewState {
            return RatesViewState(
                items = emptyList(),
                isLoading = true,
                error = ""
            )
        }

        fun createCurrenciesReceived(items: List<ConvertedCurrency>): RatesViewState {
            return RatesViewState(
                items = items,
                isLoading = false,
                error = ""
            )
        }

        fun createErrorState(error: String): RatesViewState {
            return RatesViewState(
                items = emptyList(),
                isLoading = true,
                error = error
            )
        }

    }

}