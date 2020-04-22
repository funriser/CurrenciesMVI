package com.revolut.converter.core.di.converter

import com.revolut.converter.ui.ConverterFragment
import dagger.Subcomponent

@ConverterScope
@Subcomponent(modules = [ConverterModule::class, ConverterViewModelModule::class])
interface ConverterComponent {
    fun inject(converterFragment: ConverterFragment)
}