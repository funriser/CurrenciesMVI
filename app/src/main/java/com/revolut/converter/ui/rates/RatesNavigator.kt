package com.revolut.converter.ui.rates

import androidx.navigation.NavController
import com.revolut.converter.core.di.rates.RatesScope
import com.revolut.converter.core.navigation.Navigator
import com.revolut.converter.core.navigation.ToExchange
import com.revolut.converter.ui.rates.mvi.RatesSingleAction
import javax.inject.Inject

@RatesScope
class RatesNavigator @Inject constructor() : Navigator<RatesSingleAction.RatesNavAction>() {
    override fun handleAction(controller: NavController, action: RatesSingleAction.RatesNavAction) {
        when (action) {
            is ToExchange -> {
                val direction = RatesFragmentDirections.toExchange(action.args)
                controller.navigate(direction)
            }
        }
    }
}