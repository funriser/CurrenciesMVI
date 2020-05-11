package com.revolut.converter.ui.exchange

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolut.converter.App
import com.revolut.converter.R
import com.revolut.converter.core.navigation.ToExchangeSuccess
import com.revolut.converter.core.ui.BaseMVIFragment
import com.revolut.converter.core.ui.MVIViewModel
import com.revolut.converter.core.visibleIf
import com.revolut.converter.ui.exchange.mvi.ExchangeSingleAction
import com.revolut.converter.ui.exchange.mvi.ExchangeViewModel
import com.revolut.converter.ui.exchange.mvi.ExchangeViewState
import kotlinx.android.synthetic.main.fragment_exchange.*
import javax.inject.Inject

class ExchangeFragment : BaseMVIFragment<ExchangeViewState, ExchangeSingleAction>() {

    override var layoutId = R.layout.fragment_exchange

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var exchangeNavigator: ExchangeNavigator

    private val args: ExchangeFragmentArgs by navArgs()
    private lateinit var viewModel: ExchangeViewModel
    private lateinit var exchangeAdapter: ExchangeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialState = ExchangeViewState.createInitialState(
            exchangeFrom = args.exchangeState.from,
            exchangeTo = args.exchangeState.to
        )

        App.appComponent
            .exchangeComponentBuilder()
            .initialState(initialState)
            .build()
            .inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[ExchangeViewModel::class.java]
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        exchangeAdapter = ExchangeAdapter()
        rvExchangeItems.layoutManager = LinearLayoutManager(requireActivity())
        rvExchangeItems.adapter = exchangeAdapter
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        btnToExchange.setOnClickListener {
            viewModel.onPerformExchange()
        }
    }

    override fun renderUi(viewState: ExchangeViewState) {
        renderCurrencies(viewState)
        renderProgress(viewState)
        renderSuccess(viewState)
    }

    private fun renderCurrencies(viewState: ExchangeViewState) {
        exchangeAdapter.items = viewState.items
    }

    private fun renderProgress(viewState: ExchangeViewState) {
        vProgress.visibleIf(viewState.isLoading)
    }

    private fun renderSuccess(viewState: ExchangeViewState) {
        val controller = findNavController()
        val navAction = ToExchangeSuccess()
        val isShowingSuccess = exchangeNavigator.isCurrent(controller, navAction)
        if (viewState.isExchanged && !isShowingSuccess) {
            exchangeNavigator.handleAction(controller, ToExchangeSuccess())
        } else if(!viewState.isExchanged && isShowingSuccess) {
            controller.popBackStack()
        }
    }

    override fun getViewModel(): MVIViewModel<*, ExchangeSingleAction, ExchangeViewState> {
        return viewModel
    }

    override fun consumeSingleAction(action: ExchangeSingleAction) {

    }

}