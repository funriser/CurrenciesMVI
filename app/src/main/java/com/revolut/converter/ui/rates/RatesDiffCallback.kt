package com.revolut.converter.ui.rates

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.revolut.converter.domain.entity.BaseConvertedCurrency
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.delegate.CurrencyDelegate
import com.revolut.converter.ui.delegate.CurrencyItem

class RatesDiffCallback: DiffUtil.ItemCallback<CurrencyItem>() {

    override fun areItemsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean {
        return if (oldItem is ConvertedCurrency && newItem is ConvertedCurrency) {
            oldItem.currency.code == newItem.currency.code
        } else {
            oldItem == newItem
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean {
        return if (oldItem is ConvertedCurrency && newItem is ConvertedCurrency) {
            if (oldItem is BaseConvertedCurrency && newItem is BaseConvertedCurrency) {
                true
            } else if (oldItem !is BaseConvertedCurrency && newItem !is BaseConvertedCurrency) {
                oldItem.currency.code == newItem.currency.code &&
                        oldItem.amount.toString() == newItem.amount.toString()
            } else {
                false
            }
        } else {
            oldItem == newItem
        }
    }

    override fun getChangePayload(oldItem: CurrencyItem, newItem: CurrencyItem): Any? {
        return if (oldItem is ConvertedCurrency && newItem is ConvertedCurrency) {
            if (oldItem !is BaseConvertedCurrency && newItem !is BaseConvertedCurrency) {
                CurrencyDelegate.PAYLOAD_CURRENCY
            } else if (oldItem !is BaseConvertedCurrency && newItem is BaseConvertedCurrency) {
                CurrencyDelegate.PAYLOAD_NEW_BASIC
            } else {
                null
            }
        } else {
            null
        }
    }

}