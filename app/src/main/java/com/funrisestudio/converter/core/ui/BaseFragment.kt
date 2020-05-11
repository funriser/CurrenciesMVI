package com.funrisestudio.converter.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment: Fragment() {

    abstract var layoutId: Int

    private val stopCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val onErrorHandler: (Throwable) -> Unit by lazy {
        { throwable: Throwable -> throwable.printStackTrace() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    protected fun <T> Observable<T>.subscribeTillStop(onSuccess: (T) -> Unit = {}) {
        stopCompositeDisposable.add(subscribe(onSuccess, onErrorHandler))
    }

    override fun onStop() {
        stopCompositeDisposable.clear()
        super.onStop()
    }

}