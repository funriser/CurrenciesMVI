package com.revolut.converter.di.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revolut.converter.di.ViewModelFactory
import com.revolut.converter.di.ViewModelKey
import com.revolut.converter.ui.ConverterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ConverterViewModelModule {

    @Binds
    @ConverterScope
    abstract fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ConverterViewModel::class)
    abstract fun converterViewModel(converterViewModel: ConverterViewModel): ViewModel

}