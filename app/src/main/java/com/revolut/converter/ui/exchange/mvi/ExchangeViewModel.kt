package com.revolut.converter.ui.exchange.mvi

import com.revolut.converter.core.mvi.Store
import com.revolut.converter.core.ui.MVIViewModel
import javax.inject.Inject

class ExchangeViewModel @Inject constructor(
    store: Store<ExchangeAction, ExchangeViewState>
): MVIViewModel<ExchangeAction, ExchangeViewState>(store)