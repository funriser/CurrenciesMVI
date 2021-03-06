package com.funrisestudio.converter.core.di

import com.funrisestudio.converter.data.source.ConverterApi
import com.funrisestudio.converter.data.source.RetrofitClient
import com.funrisestudio.converter.domain.executor.DomainExecutor
import com.funrisestudio.converter.domain.executor.Executor
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun retrofit(): Retrofit {
            return RetrofitClient.getClient()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun currencyClient(retrofit: Retrofit): ConverterApi {
            return retrofit.create(ConverterApi::class.java)
        }

    }

    @Binds
    @Singleton
    abstract fun executor(domainExecutor: DomainExecutor): Executor

}