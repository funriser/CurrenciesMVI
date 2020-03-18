package com.revolut.converter.core.di.converter

import com.revolut.converter.ui.ConverterActivity
import dagger.Subcomponent

@ConverterScope
@Subcomponent(modules = [ConverterModule::class, ConverterViewModelModule::class])
interface ConverterComponent {
    fun inject(converterActivity: ConverterActivity)
}