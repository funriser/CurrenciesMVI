package com.funrisestudio.converter.ui.exchange

import androidx.navigation.NavController
import com.funrisestudio.converter.R
import com.funrisestudio.converter.core.di.exchange.ExchangeScope
import com.funrisestudio.converter.core.navigation.Navigator
import com.funrisestudio.converter.core.navigation.ToExchangeSuccess
import com.funrisestudio.converter.ui.exchange.mvi.ExchangeSingleAction
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

    fun isCurrent(
        controller: NavController,
        action: ExchangeSingleAction.ExchangeNavAction
    ): Boolean {
        val current = controller.currentDestination
        return when (action) {
            is ToExchangeSuccess -> current?.id == R.id.exchangeSuccessDialog
            else -> false
        }
    }

}