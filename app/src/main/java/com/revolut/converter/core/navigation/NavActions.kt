package com.revolut.converter.core.navigation

import com.revolut.converter.ui.exchange.ExchangeState
import com.revolut.converter.ui.exchange.mvi.ExchangeSingleAction
import com.revolut.converter.ui.rates.mvi.RatesSingleAction

class ToExchange(val args: ExchangeState): RatesSingleAction.RatesNavAction()
class ToExchangeSuccess: ExchangeSingleAction.ExchangeNavAction()