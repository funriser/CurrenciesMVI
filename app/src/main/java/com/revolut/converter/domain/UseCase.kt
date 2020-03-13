package com.revolut.converter.domain

import com.revolut.converter.domain.executor.Executor
import io.reactivex.Observable

abstract class UseCase<Type, in Params> (
    protected val executor: Executor
) where Type : Any {

    abstract fun buildObservable(params: Params): Observable<Type>

    class None

}