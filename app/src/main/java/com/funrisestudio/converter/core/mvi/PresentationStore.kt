package com.funrisestudio.converter.core.mvi

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Store without middleware
 */
class PresentationStore<A : Action, V : ViewState> @Inject constructor(
    private val reducer: Reducer<A, V>,
    initialState: V
) : Store<A, V> {

    private val allActions: PublishSubject<A> = PublishSubject.create()
    private val states: BehaviorSubject<V> = BehaviorSubject.createDefault(initialState)

    override fun wire(): Disposable {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable +=
            allActions
                .withLatestFrom(states) { action, viewState -> reducer.reduce(viewState, action) }
                .subscribe(states::onNext)

        return compositeDisposable
    }

    override fun bind(uiAction: Observable<A>): Disposable {
        return uiAction.subscribe(allActions::onNext)
    }

    override fun observeViewState(): Observable<V> = states.hide()
}