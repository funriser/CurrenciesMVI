package com.revolut.converter.ui.exchange.mvi

import com.revolut.converter.core.mvi.Action
import com.revolut.converter.core.mvi.SingleAction
import com.revolut.converter.core.mvi.ViewState
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.delegate.CurrencyItem
import com.revolut.converter.ui.exchange.ExchangeDecorItem
import com.revolut.converter.ui.exchange.ExchangeInput

sealed class ExchangeAction: Action {
    object PerformExchange: ExchangeAction()
    object CurrenciesExchanged: ExchangeAction()
    object ExchangeLoading: ExchangeAction()
}

sealed class ExchangeSingleAction: SingleAction {
    abstract class ExchangeNavAction: ExchangeSingleAction()
}

data class ExchangeViewState(
    val items: List<CurrencyItem>,
    val isLoading: Boolean,
    val isExchanged: Boolean
): ViewState {

    companion object {

        fun createInitialState(
            exchangeFrom: ExchangeInput,
            exchangeTo: ExchangeInput
        ): ExchangeViewState {
            val currencyFrom = ConvertedCurrency(
                currency = exchangeFrom.currency,
                amount = exchangeFrom.amount
            )
            val decorItem = ExchangeDecorItem
            val currencyTo = ConvertedCurrency(
                currency = exchangeTo.currency,
                amount = exchangeTo.amount
            )
            val delegateItems = listOf(
                currencyFrom,
                decorItem,
                currencyTo
            )
            return ExchangeViewState(
                items = delegateItems,
                isLoading = false,
                isExchanged = false
            )
        }

        fun createLoadingState(currentItems: List<CurrencyItem>): ExchangeViewState {
            return ExchangeViewState(
                items = currentItems,
                isLoading = true,
                isExchanged = false
            )
        }

        fun createExchangesState(currentItems: List<CurrencyItem>): ExchangeViewState {
            return ExchangeViewState(
                items = currentItems,
                isLoading = false,
                isExchanged = true
            )
        }

    }

}