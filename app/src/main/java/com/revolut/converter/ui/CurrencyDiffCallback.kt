package com.revolut.converter.ui

import androidx.recyclerview.widget.DiffUtil
import com.revolut.converter.domain.entity.BaseCurrency
import com.revolut.converter.domain.entity.Currency

class CurrencyDiffCallback(
    private val oldList: List<Currency>,
    private val newList: List<Currency>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is BaseCurrency && newItem is BaseCurrency) {
            true
        } else {
            oldItem.code == newItem.code &&
                    oldItem.amount.toString() == newItem.amount.toString()
        }
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return CurrencyAdapter.PAYLOAD_CURRENCY
    }

}