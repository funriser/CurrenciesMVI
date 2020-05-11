package com.funrisestudio.converter.core.di.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.funrisestudio.converter.core.mvi.*
import com.funrisestudio.converter.domain.interactor.ExchangeMiddleware
import com.funrisestudio.converter.ui.exchange.mvi.ExchangeAction
import com.funrisestudio.converter.ui.exchange.mvi.ExchangeReducer
import com.funrisestudio.converter.ui.exchange.mvi.ExchangeViewModel
import com.funrisestudio.converter.ui.exchange.mvi.ExchangeViewState
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ExchangeModule {

    @Binds
    @ExchangeScope
    abstract fun exchangeVMFactory(
        defaultViewModelFactory: DefaultViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeViewModel::class)
    @ExchangeScope
    abstract fun exchangeViewModel(exchangeViewModel: ExchangeViewModel): ViewModel

    @Binds
    @ExchangeScope
    abstract fun exchangeMviStore(
        store: DefaultStore<ExchangeAction, ExchangeViewState>
    ): Store<ExchangeAction, ExchangeViewState>

    @Binds
    @ExchangeScope
    abstract fun exchangeMiddleware(
        exchangeMiddleware: ExchangeMiddleware
    ): MiddleWare<ExchangeAction, ExchangeViewState>

    @Binds
    abstract fun exchangeReducer(
        exchangeReducer: ExchangeReducer
    ): Reducer<ExchangeAction, ExchangeViewState>

}