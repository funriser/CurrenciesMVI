package com.revolut.converter.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.revolut.converter.R
import com.revolut.converter.core.inflate
import com.revolut.converter.domain.entity.BaseCurrency
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.entity.ExchangeCurrency
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrencyAdapter(
    private val viewModel: ConverterViewModel
): RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    var items = listOf<Currency>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_currency))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(currencies: List<Currency>) {
        val oldItems = items
        items = currencies
        if (oldItems.isEmpty()) {
            notifyDataSetChanged()
        } else if (oldItems.size > 1) {
            notifyItemRangeChanged(1, items.size - 1)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(viewModel: ConverterViewModel, currency: Currency) {
            with(itemView) {
                tvCurrencyTitle.text = currency.code
                tvCurrencyCountry.text = currency.name
                if (currency.image != -1) {
                    ivCurrency.setImageResource(currency.image)
                } else {
                    //TODO("Clear resource if there is no image")
                }
                when(currency) {
                    is BaseCurrency -> {
                        bindBaseCurrency(viewModel, currency)
                    }
                    is ExchangeCurrency -> {
                        bindExchangeCurrency(currency)
                    }
                }
            }
        }

        private fun bindBaseCurrency(viewModel: ConverterViewModel, baseCurrency: BaseCurrency) {
            itemView.edAmount.doAfterTextChanged { text ->
                text?.let {
                    viewModel.onNewExchangeAmount(baseCurrency, it.toString())
                }
            }
        }

        private fun bindExchangeCurrency(exchangeCurrency: ExchangeCurrency) {
            itemView.edAmount.setText(exchangeCurrency.finalAmount.toString())
        }

    }

}