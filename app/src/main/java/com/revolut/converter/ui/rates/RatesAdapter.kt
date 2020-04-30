package com.revolut.converter.ui.rates

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.revolut.converter.ui.delegate.CurrencyDelegate
import com.revolut.converter.ui.delegate.CurrencyItem
import com.revolut.converter.ui.rates.mvi.RatesViewModel

class RatesAdapter(
    viewModel: RatesViewModel
) : AsyncListDifferDelegationAdapter<CurrencyItem>(diffCallback) {

    init {
        delegatesManager
            .addDelegate(CurrencyDelegate(viewModel))
    }

    companion object {

        private val diffCallback: DiffUtil.ItemCallback<CurrencyItem>
            get() = RatesDiffCallback()

    }

}