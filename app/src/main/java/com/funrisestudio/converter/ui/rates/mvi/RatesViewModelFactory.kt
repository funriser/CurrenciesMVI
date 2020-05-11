package com.funrisestudio.converter.ui.rates.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.funrisestudio.converter.core.mvi.Store
import com.funrisestudio.converter.ui.rates.RatesState
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class RatesViewModelFactory @Inject constructor(
    private val store: Store<RatesAction, RatesViewState>,
    private val initialState: RatesState
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm = RatesViewModel(store, initialState)
        return try {
            vm as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}