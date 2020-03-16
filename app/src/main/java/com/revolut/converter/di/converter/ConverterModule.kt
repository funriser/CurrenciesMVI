package com.revolut.converter.di.converter

import com.revolut.converter.data.repository.ConverterRepositoryImpl
import com.revolut.converter.data.source.converter.ConverterLocalSource
import com.revolut.converter.data.source.converter.ConverterRemoteSource
import com.revolut.converter.data.source.converter.local.ConverterLocalSourceImpl
import com.revolut.converter.data.source.converter.remote.ConverterRemoteSourceImpl
import com.revolut.converter.domain.repository.ConverterRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ConverterModule {

    @Binds
    @ConverterScope
    abstract fun converterRepository(
        converterRepositoryImpl: ConverterRepositoryImpl
    ) : ConverterRepository

    @Binds
    @ConverterScope
    abstract fun converterRemoteSource(
        converterRemoteSourceImpl: ConverterRemoteSourceImpl
    ): ConverterRemoteSource

    @Binds
    @ConverterScope
    abstract fun converterLocalSource(
        converterLocalSourceImpl: ConverterLocalSourceImpl
    ): ConverterLocalSource

}