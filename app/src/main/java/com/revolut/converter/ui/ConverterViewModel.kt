package com.revolut.converter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolut.converter.core.plusAssign
import com.revolut.converter.domain.entity.CurrencyRate
import com.revolut.converter.domain.interactor.GetCurrencyRates
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val getCurrencyRates: GetCurrencyRates
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _rates = MutableLiveData<List<CurrencyRate>>()
    val rates: LiveData<List<CurrencyRate>> = _rates

    init {
        val defaultParams = GetCurrencyRates.Params("EUR", 100.0)
        compositeDisposable += getCurrencyRates.buildObservable(defaultParams)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetCurrencyRatesSuccess, ::onGetCurrencyRatesError)
    }

    private fun onGetCurrencyRatesSuccess(rates: List<CurrencyRate>) {
        _rates.value = rates
    }

    private fun onGetCurrencyRatesError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}