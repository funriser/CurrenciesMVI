package com.revolut.converter.ui

import androidx.recyclerview.widget.DiffUtil
import com.revolut.converter.domain.entity.BaseConvertedCurrency
import com.revolut.converter.domain.entity.ConvertedCurrency

class CurrencyDiffCallback(
    private val oldList: List<ConvertedCurrency>,
    private val newList: List<ConvertedCurrency>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.currency.code == newItem.currency.code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is BaseConvertedCurrency && newItem is BaseConvertedCurrency) {
            true
        } else {
            oldItem.currency.code == newItem.currency.code &&
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
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem !is BaseConvertedCurrency && newItem !is BaseConvertedCurrency) {
            CurrencyAdapter.PAYLOAD_CURRENCY
        } else {
            null
        }
    }

}