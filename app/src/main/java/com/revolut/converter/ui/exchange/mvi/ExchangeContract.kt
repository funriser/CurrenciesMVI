package com.revolut.converter.ui.exchange.mvi

import com.revolut.converter.core.mvi.Action
import com.revolut.converter.core.mvi.SingleAction
import com.revolut.converter.core.mvi.ViewState
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.delegate.CurrencyItem
import com.revolut.converter.ui.exchange.ExchangeDecorItem
import com.revolut.converter.ui.exchange.ExchangeInput

sealed class ExchangeAction: Action

sealed class ExchangeSingleAction: SingleAction {
    abstract class ExchangeNavAction: ExchangeSingleAction()
}

data class ExchangeViewState(
    val items: List<CurrencyItem>
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
            val delegateItems =  listOf(
                currencyFrom,
                decorItem,
                currencyTo
            )
            return ExchangeViewState(delegateItems)
        }

    }

}