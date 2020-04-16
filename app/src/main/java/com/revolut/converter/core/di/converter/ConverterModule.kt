package com.revolut.converter.core.di.converter

import com.revolut.converter.core.mvi.DefaultStore
import com.revolut.converter.core.mvi.MiddleWare
import com.revolut.converter.core.mvi.Reducer
import com.revolut.converter.core.mvi.Store
import com.revolut.converter.data.repository.ConverterRepositoryImpl
import com.revolut.converter.data.source.converter.ConverterLocalSource
import com.revolut.converter.data.source.converter.ConverterRemoteSource
import com.revolut.converter.data.source.converter.ConverterLocalSourceImpl
import com.revolut.converter.data.source.converter.ConverterRemoteSourceImpl
import com.revolut.converter.domain.interactor.ConverterMiddleware
import com.revolut.converter.domain.repository.ConverterRepository
import com.revolut.converter.ui.error.ConverterErrorHandler
import com.revolut.converter.ui.error.ErrorHandler
import com.revolut.converter.ui.mvi.ConverterAction
import com.revolut.converter.ui.mvi.ConverterReducer
import com.revolut.converter.ui.mvi.ConverterViewState
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ConverterModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun baseConverterState(): ConverterViewState {
            return ConverterViewState.createEmpty()
        }
    }

    @Binds
    @ConverterScope
    abstract fun converterRepository(
        converterRepositoryImpl: ConverterRepositoryImpl
    ) : ConverterRepository

    @Binds
    @ConverterScope
    abstract fun converterRemoteSource(
        converterRemoteSourceImpl: ConverterRemoteSourceImpl
    ): ConverterRemoteSource

    @Binds
    @ConverterScope
    abstract fun converterLocalSource(
        converterLocalSourceImpl: ConverterLocalSourceImpl
    ): ConverterLocalSource

    @Binds
    @ConverterScope
    abstract fun errorHandler(converterErrorHandler: ConverterErrorHandler): ErrorHandler

    @Binds
    @ConverterScope
    abstract fun converterMviStore(
        defaultStore: DefaultStore<ConverterAction, ConverterViewState>
    ): Store<ConverterAction, ConverterViewState>

    @Binds
    abstract fun converterMiddleware(middleWare: ConverterMiddleware): MiddleWare<ConverterAction, ConverterViewState>

    @Binds
    abstract fun converterReducer(converterReducer: ConverterReducer): Reducer<ConverterAction, ConverterViewState>

}