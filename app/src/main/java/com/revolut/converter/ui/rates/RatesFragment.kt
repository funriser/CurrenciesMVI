package com.revolut.converter.ui.rates

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolut.converter.App
import com.revolut.converter.R
import com.revolut.converter.core.ui.BaseFragment
import com.revolut.converter.ui.DecimalFormat
import com.revolut.converter.ui.rates.mvi.RatesViewModel
import com.revolut.converter.ui.rates.mvi.RatesViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_currencies.*
import javax.inject.Inject

class RatesFragment : BaseFragment() {

    override var layoutId: Int = R.layout.fragment_currencies

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RatesViewModel
    private lateinit var ratesAdapter: RatesAdapter

    private val currenciesDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DecimalFormat.updateConfig()

        App.appComponent
            .ratesComponentBuilder()
            .ratesState(getRatesState(savedInstanceState))
            .build()
            .inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[RatesViewModel::class.java]
    }

    private fun getRatesState(savedInstanceState: Bundle?): RatesState {
        val restoredState = savedInstanceState?.getParcelable<RatesState>(KEY_CONVERTER_STATE)
        return restoredState?:RatesViewModel.initialState
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        ratesAdapter = RatesAdapter(viewModel)
        val scrollListener = getOnScrollListener()
        rvCurrencyRates.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            itemAnimator = DefaultItemAnimator()
            adapter = ratesAdapter
            addOnScrollListener(scrollListener)
        }
        btnToExchange.setOnClickListener {
            findNavController().navigate(R.id.toExchange)
        }
    }

    override fun onStart() {
        viewModel.onAttach()
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

    private fun renderUi(state: RatesViewState) {
        renderCurrencies(state)
        renderFailure(state)
    }

    private fun renderCurrencies(state: RatesViewState) {
        if (state.items.isNotEmpty()) {
            ratesAdapter.setItems(state.items)
        }
    }

    private fun renderFailure(state: RatesViewState) {
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

    override fun onDestroyView() {
        rvCurrencyRates.clearOnScrollListeners()
        super.onDestroyView()
    }

    companion object {
        private const val KEY_CONVERTER_STATE = "key_converter_state"
    }

}
