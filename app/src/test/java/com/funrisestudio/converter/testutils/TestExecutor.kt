package com.funrisestudio.converter.testutils

import com.funrisestudio.converter.domain.executor.Executor
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestExecutor: Executor {
    override val workScheduler: Scheduler
        get() = Schedulers.trampoline()
}