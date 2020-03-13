package com.revolut.converter.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolut.converter.R
import com.revolut.converter.core.inflate
import com.revolut.converter.domain.entity.CurrencyRate
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrencyAdapter: RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    var items = listOf<CurrencyRate>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_currency))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(currencyRate: CurrencyRate) {
            with(itemView) {
                tvCurrencyTitle.text = currencyRate.name
                tvCurrencyCountry.text = currencyRate.name
                edAmount.setText(currencyRate.rate.toString())
            }
        }

    }

}