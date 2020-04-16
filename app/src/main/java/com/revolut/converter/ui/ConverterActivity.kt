package com.revolut.converter.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolut.converter.App
import com.revolut.converter.R
import com.revolut.converter.core.di.ViewModelFactory
import com.revolut.converter.core.ui.BaseActivity
import com.revolut.converter.ui.mvi.ConverterViewModel
import com.revolut.converter.ui.mvi.ConverterViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ConverterActivity : BaseActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ConverterViewModel
    private lateinit var currencyAdapter: CurrencyAdapter

    private val currenciesDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DecimalFormat.updateConfig()

        App.appComponent
            .converterComponent()
            .inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[ConverterViewModel::class.java]

        savedInstanceState?.getParcelable<ConverterState>(KEY_CONVERTER_STATE)?.let {
            viewModel.converterState = it
        }

        initView()
    }

    private fun initView() {
        currencyAdapter = CurrencyAdapter(viewModel)
        val scrollListener = getOnScrollListener()
        rvCurrencyRates.apply {
            layoutManager = LinearLayoutManager(this@ConverterActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = currencyAdapter
            addOnScrollListener(scrollListener)
        }
    }

    override fun onStart() {
        viewModel.onAttach(firstStart)
        observeCurrencies()
        super.onStart()
    }

    private fun observeCurrencies() {
        currenciesDisposable += viewModel.observeViewState()
            .subscribe(::renderUi)
    }

    private fun stopObservingCurrencies() {
        currenciesDisposable.clear()
    }

    private fun renderUi(state: ConverterViewState) {
        renderCurrencies(state)
        renderFailure(state)
    }

    private fun renderCurrencies(state: ConverterViewState) {
        if (state.items.isNotEmpty()) {
            currencyAdapter.setItems(state.items)
        }
    }

    private fun renderFailure(state: ConverterViewState) {
        if (state.error.isNotEmpty()) {
            Snackbar
                .make(rvCurrencyRates, state.error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_try_again) {
                    viewModel.receiveCurrencyUpdates()
                }
                .show()
        }
    }

    private fun getOnScrollListener(): RecyclerView.OnScrollListener {
        return object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (rvCurrencyRates.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    stopObservingCurrencies()
                } else {
                    observeCurrencies()
                }
            }
        }
    }

    override fun onStop() {
        viewModel.onDetach()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CONVERTER_STATE, viewModel.converterState)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_CONVERTER_STATE = "key_converter_state"
    }

}
