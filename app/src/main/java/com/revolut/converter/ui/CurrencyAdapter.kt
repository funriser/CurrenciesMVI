package com.revolut.converter.ui

import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.revolut.converter.R
import com.revolut.converter.core.inflate
import com.revolut.converter.domain.entity.BaseConvertedCurrency
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.entity.ConvertedCurrency
import kotlinx.android.synthetic.main.item_currency.view.*
import java.math.BigDecimal

class CurrencyAdapter(
    private val viewModel: ConverterViewModel
): RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    companion object {
        const val PAYLOAD_CURRENCY =  16
    }

    var items = listOf<ConvertedCurrency>()
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

    fun setItems(currencies: List<ConvertedCurrency>) {
        val diff = DiffUtil.calculateDiff(CurrencyDiffCallback(items, currencies))
        items = currencies
        diff.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var textWatcher: TextWatcher? = null

        fun bind(viewModel: ConverterViewModel, currency: ConvertedCurrency) {
            clearWatcher()
            bindCurrency(currency.currency)
            when(currency) {
                is BaseConvertedCurrency -> {
                    bindBaseCurrency(viewModel, currency)
                }
                else -> {
                    bindExchangeCurrency(viewModel, currency)
                }
            }
        }

        fun bindAmount(newAmount: BigDecimal) {
            itemView.edAmount.setText(newAmount.toString())
        }

        private fun bindCurrency(currency: Currency) {
            with(currency) {
                itemView.tvCurrencyTitle.text = code
                itemView.tvCurrencyCountry.text = name
                if (image != -1) {
                    itemView.ivCurrency.setImageResource(image)
                } else {
                    itemView.ivCurrency.setImageResource(android.R.color.transparent)
                }
            }
        }

        private fun bindBaseCurrency(viewModel: ConverterViewModel, baseExchangeCurrency: BaseConvertedCurrency) {
            val textWatcher = BaseCurrencyTextWatcher(itemView.edAmount, viewModel, baseExchangeCurrency)
            itemView.edAmount.setText(baseExchangeCurrency.amount.toString())
            itemView.edAmount.addTextChangedListener(textWatcher)
            itemView.setOnClickListener(null)
        }

        private fun bindExchangeCurrency(viewModel: ConverterViewModel, convertedCurrency: ConvertedCurrency) {
            itemView.edAmount.setText(convertedCurrency.amount.toString())
            itemView.setOnClickListener {
                (itemView.parent as View).clearFocus()
                itemView.edAmount.requestFocus()
                viewModel.onNewExchangeAmount(convertedCurrency, itemView.edAmount.text.toString())
            }
        }

        private fun clearWatcher() {
            textWatcher?.let {
                itemView.edAmount.removeTextChangedListener(it)
            }
        }

    }

}