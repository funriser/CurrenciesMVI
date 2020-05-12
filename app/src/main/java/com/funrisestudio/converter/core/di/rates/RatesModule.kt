package com.funrisestudio.converter.core.di.rates

import androidx.lifecycle.ViewModelProvider
import com.funrisestudio.converter.core.mvi.DefaultStore
import com.funrisestudio.converter.core.mvi.MiddleWare
import com.funrisestudio.converter.core.mvi.Reducer
import com.funrisestudio.converter.core.mvi.Store
import com.funrisestudio.converter.data.repository.RatesRepositoryImpl
import com.funrisestudio.converter.data.source.converter.RatesLocalSource
import com.funrisestudio.converter.data.source.converter.RatesRemoteSource
import com.funrisestudio.converter.data.source.converter.RatesLocalSourceImpl
import com.funrisestudio.converter.data.source.converter.RatesRemoteSourceImpl
import com.funrisestudio.converter.domain.interactor.RatesMiddleware
import com.funrisestudio.converter.domain.repository.RatesRepository
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
    abstract fun ratesRepository(
        ratesRepositoryImpl: RatesRepositoryImpl
    ) : RatesRepository

    @Binds
    @RatesScope
    abstract fun ratesRemoteSource(
        converterRemoteSourceImpl: RatesRemoteSourceImpl
    ): RatesRemoteSource

    @Binds
    @RatesScope
    abstract fun ratesLocalSource(
        converterLocalSourceImpl: RatesLocalSourceImpl
    ): RatesLocalSource

    @Binds
    @RatesScope
    abstract fun errorHandler(converterErrorHandler: ConverterErrorHandler): ErrorHandler

    @Binds
    @RatesScope
    abstract fun ratesMviStore(
        defaultStore: DefaultStore<RatesAction, RatesViewState>
    ): Store<RatesAction, RatesViewState>

    @Binds
    abstract fun ratesMiddleware(middleWare: RatesMiddleware): MiddleWare<RatesAction, RatesViewState>

    @Binds
    abstract fun ratesReducer(ratesReducer: RatesReducer): Reducer<RatesAction, RatesViewState>

}