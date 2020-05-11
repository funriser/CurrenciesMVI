package com.funrisestudio.converter.core.di.exchange

import com.funrisestudio.converter.ui.exchange.ExchangeFragment
import com.funrisestudio.converter.ui.exchange.mvi.ExchangeViewState
import dagger.BindsInstance
import dagger.Subcomponent

@ExchangeScope
@Subcomponent(modules = [ExchangeModule::class])
interface ExchangeComponent {

    fun inject(exchangeFragment: ExchangeFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun initialState(exchangeViewState: ExchangeViewState): Builder

        fun build(): ExchangeComponent

    }

}