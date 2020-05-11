package com.funrisestudio.converter.domain.executor

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DomainExecutor @Inject constructor(): Executor {
    override val workScheduler: Scheduler
        get() = Schedulers.io()
}