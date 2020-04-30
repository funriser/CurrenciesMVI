package com.revolut.converter.ui.delegate

import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.revolut.converter.R
import com.revolut.converter.core.inflate
import com.revolut.converter.domain.entity.BaseConvertedCurrency
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.DecimalFormat
import com.revolut.converter.ui.input.BaseCurrencyLengthFilter
import com.revolut.converter.ui.input.BaseCurrencyTextWatcher
import com.revolut.converter.ui.rates.mvi.RatesViewModel
import kotlinx.android.synthetic.main.item_currency.view.*
import java.math.BigDecimal

class CurrencyDelegate(
    private val viewModel: RatesViewModel
): AdapterDelegate<List<CurrencyItem>>() {

    companion object {
        const val PAYLOAD_CURRENCY = 16
        const val PAYLOAD_NEW_BASIC = 32
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            parent.inflate(
                R.layout.item_currency
            )
        )
    }

    override fun isForViewType(items: List<CurrencyItem>, position: Int): Boolean {
        return items[position] is ConvertedCurrency
    }

    override fun onBindViewHolder(
        items: List<CurrencyItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        require(holder is ViewHolder)
        val item = items[position] as ConvertedCurrency
        if (payloads.isNotEmpty()) {
            when (payloads[0]) {
                //if only currency amount changed
                PAYLOAD_CURRENCY -> {
                    holder.bindAmount(item.amount)
                }
                //if switched to the new basic currency
                PAYLOAD_NEW_BASIC -> {
                    holder.bindBaseCurrency(viewModel, item)
                }
            }
        } else {
            holder.bind(viewModel, item)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var textWatcher: TextWatcher? = null

        fun bind(viewModel: RatesViewModel, currency: ConvertedCurrency) {
            releaseBaseCurrencyInput()
            bindCurrency(currency)
            bindAmount(currency.amount)
            when (currency) {
                is BaseConvertedCurrency -> {
                    bindBaseCurrency(viewModel, currency)
                }
                else -> {
                    bindExchangeCurrency(viewModel, currency)
                }
            }
        }

        /**
         * Performs binding of the main currency info
         */
        private fun bindCurrency(convertedCurrency: ConvertedCurrency) {
            with(convertedCurrency.currency) {
                itemView.tvCurrencyTitle.text = code
                itemView.tvCurrencyCountry.text = name
                if (image != -1) {
                    itemView.ivCurrency.setImageResource(image)
                } else {
                    itemView.ivCurrency.setImageResource(android.R.color.transparent)
                }
            }
            itemView.edAmount.keyListener =
                DigitsKeyListener.getInstance(DecimalFormat.ACCEPTED_INPUT)
        }

        /**
         * Performs binding related only to base currency
         */
        fun bindBaseCurrency(
            viewModel: RatesViewModel,
            convertedCurrency: ConvertedCurrency
        ) {
            itemView.setOnClickListener(null)
            itemView.edAmount.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    setUpBaseCurrencyInput(viewModel, convertedCurrency)
                } else {
                    releaseBaseCurrencyInput()
                    (itemView.parent as ViewGroup).isFocusable = false
                }
            }
            itemView.layoutCurrency.setOnClickListener(null)
        }

        /**
         * Performs binding related only to currencies that show rates
         */
        private fun bindExchangeCurrency(viewModel: RatesViewModel,
                                         convertedCurrency: ConvertedCurrency) {
            itemView.setOnClickListener {
                (itemView.parent as View).clearFocus()
                itemView.edAmount.requestFocus()
                onNewExchangeAmount(viewModel, convertedCurrency)
            }
            itemView.edAmount.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    setUpBaseCurrencyInput(viewModel, convertedCurrency)
                    onNewExchangeAmount(viewModel, convertedCurrency)
                } else {
                    releaseBaseCurrencyInput()
                }
            }
            itemView.edAmount.filters = arrayOf()
            itemView.layoutCurrency.setOnClickListener {
                viewModel.onCurrencySelected(adapterPosition)
            }
        }

        /**
         * Just binds total amount for currency
         */
        fun bindAmount(newAmount: BigDecimal) {
            val amountStr =
                DecimalFormat.toDecimalString(
                    newAmount,
                    true
                )
            if (amountStr == "0") {
                setLightAmountColor()
            } else {
                setPrimaryAmountColor()
            }
            itemView.edAmount.setText(amountStr)
        }

        private fun onNewExchangeAmount(
            viewModel: RatesViewModel,
            convertedCurrency: ConvertedCurrency
        ) {
            val input = itemView.edAmount.text.toString()
            viewModel.onNewExchangeAmount(convertedCurrency, input)
        }

        private fun setUpBaseCurrencyInput(
            viewModel: RatesViewModel,
            currency: ConvertedCurrency
        ) {
            setPrimaryAmountColor()
            textWatcher = BaseCurrencyTextWatcher(itemView.edAmount) {
                onNewExchangeAmount(viewModel, currency)
            }
            itemView.edAmount.apply {
                addTextChangedListener(textWatcher)
                filters = arrayOf(BaseCurrencyLengthFilter())
            }
        }

        private fun releaseBaseCurrencyInput() {
            textWatcher?.let {
                itemView.edAmount.removeTextChangedListener(it)
            }
            itemView.edAmount.filters = arrayOf()
        }

        private fun setPrimaryAmountColor() {
            val textColor = ContextCompat.getColor(itemView.context, R.color.colorBlack)
            itemView.edAmount.setTextColor(textColor)
        }

        private fun setLightAmountColor() {
            val textColor = ContextCompat.getColor(itemView.context, R.color.colorGreyLight)
            itemView.edAmount.setTextColor(textColor)
        }

    }

}