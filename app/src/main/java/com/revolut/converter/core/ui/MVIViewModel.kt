package com.revolut.converter.core.ui

import androidx.lifecycle.ViewModel
import com.revolut.converter.core.mvi.Action
import com.revolut.converter.core.mvi.Store
import com.revolut.converter.core.mvi.ViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class MVIViewModel<A: Action, S: ViewState>(
    protected val store: Store<A, S>
): ViewModel() {

    protected open val distinctStates: Boolean = true

    protected val uiActions = PublishSubject.create<A>()
    protected lateinit var actionsDisposable: Disposable
    protected lateinit var wiringDisposable: Disposable

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

    protected fun postAction(action: A) {
        uiActions.onNext(action)
    }

    open fun onDetach() {
        actionsDisposable.dispose()
    }

    override fun onCleared() {
        wiringDisposable.dispose()
        super.onCleared()
    }

}