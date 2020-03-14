package com.revolut.converter.ui

import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.revolut.converter.R
import com.revolut.converter.core.inflate
import com.revolut.converter.domain.entity.BaseCurrency
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.entity.ExchangeCurrency
import kotlinx.android.synthetic.main.item_currency.view.*
import java.math.BigDecimal

class CurrencyAdapter(
    private val viewModel: ConverterViewModel
): RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    companion object {
        const val PAYLOAD_CURRENCY =  16
    }

    var items = listOf<Currency>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_currency))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == PAYLOAD_CURRENCY) {
            holder.bindAmount(items[position].amount)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(currencies: List<Currency>) {
        val diff = DiffUtil.calculateDiff(CurrencyDiffCallback(items, currencies))
        items = currencies
        diff.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var textWatcher: TextWatcher? = null

        fun bind(viewModel: ConverterViewModel, currency: Currency) {
            clearWatcher()
            with(itemView) {
                tvCurrencyTitle.text = currency.code
                tvCurrencyCountry.text = currency.name
                if (currency.image != -1) {
                    ivCurrency.setImageResource(currency.image)
                } else {
                    ivCurrency.setImageResource(android.R.color.transparent)
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

        fun bindAmount(newAmount: BigDecimal) {
            itemView.edAmount.setText(newAmount.toString())
        }

        private fun bindBaseCurrency(viewModel: ConverterViewModel, baseCurrency: BaseCurrency) {
            val textWatcher = BaseCurrencyTextWatcher(itemView.edAmount, viewModel, baseCurrency)
            itemView.edAmount.setText(baseCurrency.amount.toString())
            itemView.edAmount.addTextChangedListener(textWatcher)
        }

        private fun bindExchangeCurrency(exchangeCurrency: ExchangeCurrency) {
            itemView.edAmount.setText(exchangeCurrency.amount.toString())
        }

        private fun clearWatcher() {
            textWatcher?.let {
                itemView.edAmount.removeTextChangedListener(it)
            }
        }

    }

}