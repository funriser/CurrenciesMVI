package com.revolut.converter.core.ui

import androidx.lifecycle.ViewModel
import com.revolut.converter.core.mvi.Action
import com.revolut.converter.core.mvi.SingleAction
import com.revolut.converter.core.mvi.Store
import com.revolut.converter.core.mvi.ViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class MVIViewModel<A: Action, SA: SingleAction, S: ViewState>(
    protected val store: Store<A, S>
): ViewModel() {

    protected open val distinctStates: Boolean = true

    private val uiActions = PublishSubject.create<A>()
    private lateinit var actionsDisposable: Disposable
    private lateinit var wiringDisposable: Disposable

    private val singleActions = PublishSubject.create<SA>()

    open fun onAttach(isFirst: Boolean) {
        if (isFirst) {
            wiringDisposable = store.wire()
        }
        actionsDisposable = store.bind(uiActions.hide())
    }

    open fun observeViewState(): Observable<S> {
        var observable = store.observeViewState()
        if (distinctStates) {
            observable = observable.distinctUntilChanged()
        }
        return observable.observeOn(AndroidSchedulers.mainThread())
    }

    fun observeSingleActions(): Observable<SA> {
        return singleActions.hide()
            .observeOn(AndroidSchedulers.mainThread())
    }

    protected fun postAction(action: A) {
        uiActions.onNext(action)
    }

    protected fun postSingleAction(action: SA) {
        singleActions.onNext(action)
    }

    open fun onDetach() {
        actionsDisposable.dispose()
    }

    override fun onCleared() {
        wiringDisposable.dispose()
        super.onCleared()
    }

}