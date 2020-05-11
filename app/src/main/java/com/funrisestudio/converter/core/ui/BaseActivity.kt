package com.funrisestudio.converter.core.ui

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    private val stopCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val onErrorHandler: (Throwable) -> Unit by lazy {
        { throwable: Throwable -> throwable.printStackTrace() }
    }

    protected fun <T> Observable<T>.subscribeTillStop(onSuccess: (T) -> Unit = {}) {
        stopCompositeDisposable.add(subscribe(onSuccess, onErrorHandler))
    }

    override fun onStop() {
        stopCompositeDisposable.clear()
        super.onStop()
    }

}