package com.funrisestudio.converter.core.di.rates

import androidx.lifecycle.ViewModelProvider
import com.funrisestudio.converter.core.mvi.DefaultStore
import com.funrisestudio.converter.core.mvi.MiddleWare
import com.funrisestudio.converter.core.mvi.Reducer
import com.funrisestudio.converter.core.mvi.Store
import com.funrisestudio.converter.data.repository.ConverterRepositoryImpl
import com.funrisestudio.converter.data.source.converter.ConverterLocalSource
import com.funrisestudio.converter.data.source.converter.ConverterRemoteSource
import com.funrisestudio.converter.data.source.converter.ConverterLocalSourceImpl
import com.funrisestudio.converter.data.source.converter.ConverterRemoteSourceImpl
import com.funrisestudio.converter.domain.interactor.RatesMiddleware
import com.funrisestudio.converter.domain.repository.ConverterRepository
import com.funrisestudio.converter.ui.error.ConverterErrorHandler
import com.funrisestudio.converter.ui.error.ErrorHandler
import com.funrisestudio.converter.ui.rates.mvi.RatesAction
import com.funrisestudio.converter.ui.rates.mvi.RatesReducer
import com.funrisestudio.converter.ui.rates.mvi.RatesViewModelFactory
import com.funrisestudio.converter.ui.rates.mvi.RatesViewState
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