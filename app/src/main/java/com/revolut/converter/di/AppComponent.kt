package com.revolut.converter.di

import com.revolut.converter.MainActivity
import com.revolut.converter.di.converter.ConverterComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AndroidModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun converterComponent(): ConverterComponent
}