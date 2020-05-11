package com.funrisestudio.converter.domain

import com.funrisestudio.converter.domain.executor.Executor
import io.reactivex.Observable

abstract class UseCase<Type, in Params> (
    protected val executor: Executor
) where Type : Any {

    abstract fun buildObservable(params: Params): Observable<Type>

    class None

}