package com.revolut.converter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.revolut.converter.core.plusAssign
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.domain.interactor.GetConvertedCurrencies
import com.revolut.converter.ui.error.ErrorHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val getConvertedCurrencies: GetConvertedCurrencies,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    var converterState = ConverterState("EUR", "100")

    private val compositeDisposable = CompositeDisposable()

    private val _rates = MutableLiveData<List<ConvertedCurrency>>()
    val rates: LiveData<List<ConvertedCurrency>> = _rates

    private val _failure = MutableLiveData<Throwable>()
    val failure: LiveData<String> = _failure.map {
        errorHandler.getErrorMessage(it)
    }

    fun onNewExchangeAmount(currency: ConvertedCurrency, amount: String) {
        val pureDecimalAmount = DecimalFormat.getPureDecimalString(amount)
        val newState = ConverterState(currency.currency.code, pureDecimalAmount)
        receiveCurrencyUpdates(newState)
    }

    fun receiveCurrencyUpdates(state: ConverterState? = null) {
        if (state != null) {
            converterState = state
        }
        val params = GetConvertedCurrencies.Params(
            converterState.baseCurrency,
            converterState.amount.toBigDecimal()
        )
        compositeDisposable.clear()
        compositeDisposable += getConvertedCurrencies.buildObservable(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetCurrenciesSuccess, ::onGetCurrenciesError)
    }

    fun stopReceivingCurrencyUpdates() {
        compositeDisposable.clear()
    }

    private fun onGetCurrenciesSuccess(rates: List<ConvertedCurrency>) {
        _rates.value = rates
    }

    private fun onGetCurrenciesError(e: Throwable) {
        e.printStackTrace()
        _failure.value = e
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}