package com.funrisestudio.converter.domain.executor

import io.reactivex.Scheduler

interface Executor {
    val workScheduler: Scheduler
}