package com.revolut.converter.core.di.rates

import androidx.lifecycle.ViewModelProvider
import com.revolut.converter.core.mvi.DefaultStore
import com.revolut.converter.core.mvi.MiddleWare
import com.revolut.converter.core.mvi.Reducer
import com.revolut.converter.core.mvi.Store
import com.revolut.converter.data.repository.ConverterRepositoryImpl
import com.revolut.converter.data.source.converter.ConverterLocalSource
import com.revolut.converter.data.source.converter.ConverterRemoteSource
import com.revolut.converter.data.source.converter.ConverterLocalSourceImpl
import com.revolut.converter.data.source.converter.ConverterRemoteSourceImpl
import com.revolut.converter.domain.interactor.RatesMiddleware
import com.revolut.converter.domain.repository.ConverterRepository
import com.revolut.converter.ui.error.ConverterErrorHandler
import com.revolut.converter.ui.error.ErrorHandler
import com.revolut.converter.ui.rates.mvi.RatesAction
import com.revolut.converter.ui.rates.mvi.RatesReducer
import com.revolut.converter.ui.rates.mvi.RatesViewModelFactory
import com.revolut.converter.ui.rates.mvi.RatesViewState
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RatesModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun baseConverterState(): RatesViewState {
            return RatesViewState.createEmpty()
        }
    }

    @Binds
    @RatesScope
    abstract fun ratesVMFactory(ratesViewModelFactory: RatesViewModelFactory): ViewModelProvider.Factory

    @Binds
    @RatesScope
    abstract fun converterRepository(
        converterRepositoryImpl: ConverterRepositoryImpl
    ) : ConverterRepository

    @Binds
    @RatesScope
    abstract fun converterRemoteSource(
        converterRemoteSourceImpl: ConverterRemoteSourceImpl
    ): ConverterRemoteSource

    @Binds
    @RatesScope
    abstract fun converterLocalSource(
        converterLocalSourceImpl: ConverterLocalSourceImpl
    ): ConverterLocalSource

    @Binds
    @RatesScope
    abstract fun errorHandler(converterErrorHandler: ConverterErrorHandler): ErrorHandler

    @Binds
    @RatesScope
    abstract fun converterMviStore(
        defaultStore: DefaultStore<RatesAction, RatesViewState>
    ): Store<RatesAction, RatesViewState>

    @Binds
    abstract fun converterMiddleware(middleWare: RatesMiddleware): MiddleWare<RatesAction, RatesViewState>

    @Binds
    abstract fun converterReducer(ratesReducer: RatesReducer): Reducer<RatesAction, RatesViewState>

}