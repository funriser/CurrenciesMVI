package com.revolut.converter.core.di

import com.revolut.converter.core.di.converter.ConverterComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AndroidModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun converterComponent(): ConverterComponent
}