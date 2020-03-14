package com.revolut.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.revolut.converter.core.observe
import com.revolut.converter.core.viewModel
import com.revolut.converter.di.ViewModelFactory
import com.revolut.converter.domain.entity.Currency
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
            observe(rates, ::renderCurrencyRates)
        }

        initView()
    }

    private fun initView() {
        currencyAdapter = CurrencyAdapter(viewModel)
        rvCurrencyRates.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
            adapter = currencyAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                        if (viewModel.rates.hasObservers()) {
                            viewModel.rates.removeObservers(this@MainActivity)
                        }
                    } else {
                        observe(viewModel.rates, ::renderCurrencyRates)
                    }
                }
            })
        }
    }

    private fun renderCurrencyRates(rates: List<Currency>?) {
        currencyAdapter.setItems(rates.orEmpty())
    }

}
