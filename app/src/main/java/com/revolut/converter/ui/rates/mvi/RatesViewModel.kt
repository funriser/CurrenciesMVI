package com.revolut.converter.ui.rates.mvi

import com.revolut.converter.core.mvi.Store
import com.revolut.converter.core.navigation.ToExchange
import com.revolut.converter.core.ui.MVIViewModel
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.rates.RatesState
import com.revolut.converter.ui.DecimalFormat
import com.revolut.converter.ui.exchange.ExchangeInput
import com.revolut.converter.ui.exchange.ExchangeState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    store: Store<RatesAction, RatesViewState>,
    initialState: RatesState
) : MVIViewModel<RatesAction, RatesViewState>(store) {

    companion object {
        internal val initialState
            get() = RatesState("EUR", "100")
    }

    private val singleActions = PublishSubject.create<RatesSingleAction>()

    private var latestItems: List<ConvertedCurrency>? = null

    //holds params of the last requested exchange
    var converterState = initialState

    override fun observeViewState(): Observable<RatesViewState> {
        return super.observeViewState()
            .doOnNext {
                if (it.items.isNotEmpty()) {
                    latestItems = it.items
                }
            }
    }

    fun observeSingleActions(): Observable<RatesSingleAction> {
        return singleActions.hide()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun onAttach(isFirst: Boolean) {
        super.onAttach(isFirst)
        receiveCurrencyUpdates()
    }

    fun onNewExchangeAmount(currency: ConvertedCurrency, amount: String) {
        //get pure string to keep locale-independent value
        val pureDecimalAmount =
            DecimalFormat.getPureDecimalString(amount)
        val newState = RatesState(
            currency.currency.code,
            pureDecimalAmount
        )
        receiveCurrencyUpdates(newState)
    }

    fun receiveCurrencyUpdates(state: RatesState? = null) {
        if (state != null) {
            converterState = state
        }
        val action = RatesAction.ObserveCurrency(
            converterState.baseCurrency,
            converterState.amount.toBigDecimal()
        )
        postAction(action)
    }

    fun onCurrencySelected(position: Int) {
        val items = latestItems?:return
        val from = items.first()
        val to = items[position]
        val navAction = ToExchange(
            args = ExchangeState(
                from = ExchangeInput.fromConvertedCurrency(from),
                to = ExchangeInput.fromConvertedCurrency(to)
            )
        )
        postSingleAction(navAction)
    }

    private fun postSingleAction(action: RatesSingleAction) {
        singleActions.onNext(action)
    }

}