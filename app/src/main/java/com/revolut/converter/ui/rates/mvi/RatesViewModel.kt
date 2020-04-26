package com.revolut.converter.ui.rates.mvi

import androidx.lifecycle.ViewModel
import com.revolut.converter.core.mvi.Store
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.rates.RatesState
import com.revolut.converter.ui.DecimalFormat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val store: Store<RatesAction, RatesViewState>,
    initialState: RatesState
) : ViewModel() {

    companion object {
        internal val initialState
            get() = RatesState("EUR", "100")
    }

    private val uiActions = PublishSubject.create<RatesAction>()
    private var actionsDisposable: Disposable = Disposables.empty()
    private var wiringDisposable: Disposable = Disposables.empty()

    //holds params of the last requested exchange
    var converterState = initialState

    init {
        wiringDisposable = store.wire()
    }

    fun observeViewState(): Observable<RatesViewState> {
        return store.observeViewState()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun onAttach() {
        actionsDisposable = store.bind(uiActions.hide())
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

    private fun postAction(action: RatesAction) {
        uiActions.onNext(action)
    }

    fun onDetach() {
        actionsDisposable.dispose()
    }

    override fun onCleared() {
        wiringDisposable.dispose()
        super.onCleared()
    }

}