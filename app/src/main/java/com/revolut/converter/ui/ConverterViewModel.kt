package com.revolut.converter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolut.converter.core.plusAssign
import com.revolut.converter.domain.HUNDRED
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.domain.interactor.GetConvertedCurrencies
import com.revolut.converter.domain.toDecimal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val getConvertedCurrencies: GetConvertedCurrencies
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _rates = MutableLiveData<List<ConvertedCurrency>>()
    val rates: LiveData<List<ConvertedCurrency>> = _rates

    init {
        val defaultParams = GetConvertedCurrencies.Params("EUR", HUNDRED)
        getCurrencies(defaultParams)
    }

    fun onNewExchangeAmount(currency: ConvertedCurrency, amount: String) {
        val code = currency.currency.code
        val exchangeAmount = amount.toDecimal()
        val params = GetConvertedCurrencies.Params(code, exchangeAmount)
        getCurrencies(params)
    }

    private fun getCurrencies(params: GetConvertedCurrencies.Params) {
        compositeDisposable.clear()
        compositeDisposable += getConvertedCurrencies.buildObservable(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetCurrenciesSuccess, ::onGetCurrenciesError)
    }

    private fun onGetCurrenciesSuccess(rates: List<ConvertedCurrency>) {
        _rates.value = rates
    }

    private fun onGetCurrenciesError(e: Throwable) {
        //TODO("Add error handling")
        e.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}