package com.funrisestudio.converter.ui.exchange.mvi

import com.funrisestudio.converter.core.mvi.Store
import com.funrisestudio.converter.core.ui.MVIViewModel
import javax.inject.Inject

class ExchangeViewModel @Inject constructor(
    store: Store<ExchangeAction, ExchangeViewState>
): MVIViewModel<ExchangeAction, ExchangeSingleAction, ExchangeViewState>(store) {

    fun onPerformExchange() {
        postAction(ExchangeAction.PerformExchange)
    }

}