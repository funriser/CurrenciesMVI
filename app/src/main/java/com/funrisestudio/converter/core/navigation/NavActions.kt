package com.funrisestudio.converter.core.navigation

import com.funrisestudio.converter.ui.exchange.ExchangeState
import com.funrisestudio.converter.ui.exchange.mvi.ExchangeSingleAction
import com.funrisestudio.converter.ui.rates.mvi.RatesSingleAction

class ToExchange(val args: ExchangeState): RatesSingleAction.RatesNavAction()
class ToExchangeSuccess: ExchangeSingleAction.ExchangeNavAction()