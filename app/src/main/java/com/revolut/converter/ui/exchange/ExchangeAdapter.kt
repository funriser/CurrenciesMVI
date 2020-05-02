package com.revolut.converter.ui.exchange

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.delegate.CurrencyDelegate
import com.revolut.converter.ui.delegate.CurrencyItem

class ExchangeAdapter: ListDelegationAdapter<List<CurrencyItem>>() {

    private val currencyCallback = object : CurrencyDelegate.Callback {
        override fun onNewExchangeAmount(currency: ConvertedCurrency, amount: String) {
        }
        override fun onCurrencySelected(position: Int) {
        }
    }

    init {
        delegatesManager
            .addDelegate(CurrencyDelegate(currencyCallback))
            .addDelegate(ExchangeDecorDelegate())
    }

    override fun setItems(items: List<CurrencyItem>?) {
        super.setItems(items)
        notifyDataSetChanged()
    }

}