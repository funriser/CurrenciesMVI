package com.revolut.converter.ui.exchange

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.revolut.converter.R
import com.revolut.converter.core.inflate
import com.revolut.converter.ui.delegate.CurrencyItem

class ExchangeDecorDelegate: AbsListItemAdapterDelegate<ExchangeDecorItem, CurrencyItem, ExchangeDecorDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            parent.inflate(
                R.layout.item_exchange_decor
            )
        )
    }

    override fun isForViewType(
        item: CurrencyItem,
        items: MutableList<CurrencyItem>,
        position: Int
    ): Boolean {
        return item is ExchangeDecorItem
    }

    override fun onBindViewHolder(
        item: ExchangeDecorItem,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}