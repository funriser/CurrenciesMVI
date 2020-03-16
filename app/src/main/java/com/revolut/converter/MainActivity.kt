package com.revolut.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolut.converter.core.observe
import com.revolut.converter.core.viewModel
import com.revolut.converter.di.ViewModelFactory
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.ConverterState
import com.revolut.converter.ui.ConverterViewModel
import com.revolut.converter.ui.CurrencyAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ConverterViewModel
    private lateinit var currencyAdapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.appComponent
            .converterComponent()
            .inject(this)

        viewModel = viewModel(viewModelFactory) {
            observe(rates, ::renderCurrencies)
            observe(failure, ::renderFailure)
        }

        savedInstanceState?.getParcelable<ConverterState>(KEY_CONVERTER_STATE)?.let {
            viewModel.converterState = it
        }

        initView()
    }

    override fun onStart() {
        viewModel.receiveCurrencyUpdates()
        super.onStart()
    }

    override fun onStop() {
        viewModel.stopReceivingCurrencyUpdates()
        super.onStop()
    }

    private fun initView() {
        currencyAdapter = CurrencyAdapter(viewModel)
        rvCurrencyRates.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = currencyAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                        if (viewModel.rates.hasObservers()) {
                            viewModel.rates.removeObservers(this@MainActivity)
                        }
                    } else {
                        observe(viewModel.rates, ::renderCurrencies)
                    }
                }
            })
        }
    }

    private fun renderCurrencies(rates: List<ConvertedCurrency>?) {
        currencyAdapter.setItems(rates.orEmpty())
    }

    private fun renderFailure(msg: String?) {
        msg?:return
        Snackbar
            .make(rvCurrencyRates, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.error_try_again) {
                viewModel.receiveCurrencyUpdates()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CONVERTER_STATE, viewModel.converterState)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_CONVERTER_STATE = "key_converter_state"
    }

}
