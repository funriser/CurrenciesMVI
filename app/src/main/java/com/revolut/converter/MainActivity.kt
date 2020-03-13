package com.revolut.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolut.converter.core.observe
import com.revolut.converter.core.viewModel
import com.revolut.converter.di.ViewModelFactory
import com.revolut.converter.domain.entity.CurrencyRate
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
        currencyAdapter = CurrencyAdapter()
        rvCurrencyRates.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = currencyAdapter
        }
    }

    private fun renderCurrencyRates(rates: List<CurrencyRate>?) {
        currencyAdapter.items = rates.orEmpty()
    }

}
