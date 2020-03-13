package com.revolut.converter.di.converter

import com.revolut.converter.MainActivity
import dagger.Subcomponent

@ConverterScope
@Subcomponent(modules = [ConverterModule::class, ConverterViewModelModule::class])
interface ConverterComponent {
    fun inject(mainActivity: MainActivity)
}