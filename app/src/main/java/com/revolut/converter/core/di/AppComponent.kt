package com.revolut.converter.core.di

import com.revolut.converter.core.di.exchange.ExchangeComponent
import com.revolut.converter.core.di.rates.RatesComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AndroidModule::class, DataModule::class])
@Singleton
interface AppComponent {

    fun ratesComponentBuilder(): RatesComponent.Builder

    fun exchangeComponentBuilder(): ExchangeComponent.Builder

}