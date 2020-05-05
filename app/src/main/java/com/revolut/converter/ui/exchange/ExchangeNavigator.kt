package com.revolut.converter.ui.exchange

import androidx.navigation.NavController
import com.revolut.converter.R
import com.revolut.converter.core.di.exchange.ExchangeScope
import com.revolut.converter.core.navigation.Navigator
import com.revolut.converter.core.navigation.ToExchangeSuccess
import com.revolut.converter.ui.exchange.mvi.ExchangeSingleAction
import javax.inject.Inject

@ExchangeScope
class ExchangeNavigator @Inject constructor() : Navigator<ExchangeSingleAction.ExchangeNavAction>() {

    override fun handleAction(
        controller: NavController,
        action: ExchangeSingleAction.ExchangeNavAction
    ) {
        when(action) {
            is ToExchangeSuccess -> {
                controller.navigate(R.id.toExchangeSuccess)
            }
        }
    }

}