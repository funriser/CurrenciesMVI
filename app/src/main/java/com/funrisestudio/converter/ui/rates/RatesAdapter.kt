package com.funrisestudio.converter.ui.rates

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.funrisestudio.converter.ui.delegate.CurrencyDelegate
import com.funrisestudio.converter.ui.delegate.CurrencyItem

class RatesAdapter(
    callback: CurrencyDelegate.Callback
) : AsyncListDifferDelegationAdapter<CurrencyItem>(diffCallback) {

    init {
        delegatesManager
            .addDelegate(CurrencyDelegate(callback, isEditable = true))
    }

    companion object {

        private val diffCallback: DiffUtil.ItemCallback<CurrencyItem>
            get() = RatesDiffCallback()

    }

}