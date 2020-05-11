package com.funrisestudio.converter.ui.exchange

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.funrisestudio.converter.domain.entity.ConvertedCurrency
import com.funrisestudio.converter.ui.delegate.CurrencyDelegate
import com.funrisestudio.converter.ui.delegate.CurrencyItem

class ExchangeAdapter: ListDelegationAdapter<List<CurrencyItem>>() {

    private val currencyCallback = object : CurrencyDelegate.Callback {
        override fun onNewExchangeAmount(currency: ConvertedCurrency, amount: String) {
        }
        override fun onCurrencySelected(position: Int) {
        }
    }

    init {
        delegatesManager
            .addDelegate(CurrencyDelegate(currencyCallback, isEditable = false))
            .addDelegate(ExchangeDecorDelegate())
    }

    override fun setItems(items: List<CurrencyItem>?) {
        if (items == this.items) {
            return
        }
        super.setItems(items)
        notifyDataSetChanged()
    }

}