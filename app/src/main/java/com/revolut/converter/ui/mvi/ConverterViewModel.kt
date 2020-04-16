package com.revolut.converter.ui.mvi

import androidx.lifecycle.ViewModel
import com.revolut.converter.core.mvi.Store
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.ui.ConverterState
import com.revolut.converter.ui.DecimalFormat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val store: Store<ConverterAction, ConverterViewState>
) : ViewModel() {

    private val uiActions = PublishSubject.create<ConverterAction>()
    private var actionsDisposable: Disposable = Disposables.empty()
    private var wiringDisposable: Disposable = Disposables.empty()

    //holds params of the last requested exchange
    var converterState = ConverterState("EUR", "100")

    init {
        wiringDisposable = store.wire()
    }

    fun observeViewState(): Observable<ConverterViewState> {
        return store.observeViewState()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun onAttach(firstStart: Boolean) {
        actionsDisposable = store.bind(uiActions.hide())
        receiveCurrencyUpdates()
    }

    fun onNewExchangeAmount(currency: ConvertedCurrency, amount: String) {
        //get pure string to keep locale-independent value
        val pureDecimalAmount =
            DecimalFormat.getPureDecimalString(amount)
        val newState = ConverterState(
            currency.currency.code,
            pureDecimalAmount
        )
        receiveCurrencyUpdates(newState)
    }

    fun receiveCurrencyUpdates(state: ConverterState? = null) {
        if (state != null) {
            converterState = state
        }
        val action = ConverterAction.ObserveCurrency(
            converterState.baseCurrency,
            converterState.amount.toBigDecimal()
        )
        postAction(action)
    }

    private fun postAction(action: ConverterAction) {
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