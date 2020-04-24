package com.revolut.converter.core.di.rates

import com.revolut.converter.ui.rates.RatesFragment
import com.revolut.converter.ui.rates.RatesState
import dagger.BindsInstance
import dagger.Subcomponent

@RatesScope
@Subcomponent(modules = [RatesModule::class])
interface RatesComponent {

    fun inject(ratesFragment: RatesFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun ratesState(ratesState: RatesState): Builder

        fun build(): RatesComponent

    }

}