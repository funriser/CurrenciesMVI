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
import com.revolut.converter.core.ui.BaseMVIFragment
import com.revolut.converter.core.ui.MVIViewModel
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.DecimalFormat
import com.revolut.converter.ui.delegate.CurrencyDelegate
import com.revolut.converter.ui.rates.mvi.RatesSingleAction
import com.revolut.converter.ui.rates.mvi.RatesViewModel
import com.revolut.converter.ui.rates.mvi.RatesViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_currencies.*
import javax.inject.Inject

class RatesFragment : BaseMVIFragment<RatesViewState, RatesSingleAction>(),
    CurrencyDelegate.Callback {

    override var layoutId: Int = R.layout.fragment_currencies

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigator: RatesNavigator

    private lateinit var viewModel: RatesViewModel
    private lateinit var ratesAdapter: RatesAdapter

    private val currenciesDisposable = CompositeDisposable()

    private var errorBar: Snackbar? = null

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
        return restoredState ?: RatesViewModel.initialState
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        ratesAdapter = RatesAdapter(this)
        val scrollListener = getOnScrollListener()
        rvCurrencyRates.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            itemAnimator = DefaultItemAnimator()
            adapter = ratesAdapter
            addOnScrollListener(scrollListener)
        }
    }

    override fun observeViewState() {
        currenciesDisposable += viewModel.observeViewState()
            .subscribe(::renderUi)
    }

    override fun onNewExchangeAmount(currency: ConvertedCurrency, amount: String) {
        viewModel.onNewExchangeAmount(currency, amount)
    }

    override fun onCurrencySelected(position: Int) {
        viewModel.onCurrencySelected(position)
    }

    private fun stopObservingCurrencies() {
        currenciesDisposable.clear()
    }

    override fun renderUi(viewState: RatesViewState) {
        renderCurrencies(viewState)
        renderFailure(viewState)
    }

    private fun renderCurrencies(state: RatesViewState) {
        ratesAdapter.items = state.items
    }

    private fun renderFailure(state: RatesViewState) {
        if (state.error.isNotEmpty()) {
            errorBar?.dismiss()
            errorBar = Snackbar
                .make(rvCurrencyRates, state.error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_try_again) {
                    viewModel.receiveCurrencyUpdates()
                    //snackbar hides itself when action performed
                    errorBar = null
                }
            errorBar?.show()
        } else {
            errorBar?.dismiss()
            errorBar = null
        }
    }

    override fun consumeSingleAction(action: RatesSingleAction) {
        if (action is RatesSingleAction.RatesNavAction) {
            navigator.handleAction(findNavController(), action)
            return
        }
    }

    private fun getOnScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (rvCurrencyRates.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    stopObservingCurrencies()
                } else {
                    observeViewState()
                }
            }
        }
    }

    override fun getViewModel(): MVIViewModel<*, RatesSingleAction, RatesViewState> {
        return viewModel
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
