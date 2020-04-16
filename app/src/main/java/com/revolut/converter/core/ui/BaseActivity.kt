package com.revolut.converter.core.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    private val stopCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val onErrorHandler: (Throwable) -> Unit by lazy {
        { throwable: Throwable -> throwable.printStackTrace() }
    }

    protected var firstStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstStart = savedInstanceState == null
    }

    protected fun <T> Observable<T>.subscribeTillStop(onSuccess: (T) -> Unit = {}) {
        stopCompositeDisposable.add(subscribe(onSuccess, onErrorHandler))
    }

    override fun onStop() {
        stopCompositeDisposable.clear()
        super.onStop()
    }

}