package com.revolut.converter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolut.converter.core.plusAssign
import com.revolut.converter.domain.entity.BaseCurrency
import com.revolut.converter.domain.entity.Currency
import com.revolut.converter.domain.interactor.GetCurrencies
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val getCurrencies: GetCurrencies
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _rates = MutableLiveData<List<Currency>>()
    val rates: LiveData<List<Currency>> = _rates

    init {
        val defaultParams = GetCurrencies.Params("EUR", 100.0)
        getCurrencies(defaultParams)
    }

    fun onNewExchangeAmount(baseCurrency: BaseCurrency, amount: String) {
        val exchangeAmount = amount.toDouble()
        val params = GetCurrencies.Params(baseCurrency.code, exchangeAmount)
        getCurrencies(params)
    }

    private fun getCurrencies(params: GetCurrencies.Params) {
        compositeDisposable.clear()
        compositeDisposable += getCurrencies.buildObservable(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetCurrencyRatesSuccess, ::onGetCurrencyRatesError)
    }

    private fun onGetCurrencyRatesSuccess(rates: List<Currency>) {
        _rates.value = rates
    }

    private fun onGetCurrencyRatesError(e: Throwable) {
        //TODO("Add error handling")
        e.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}